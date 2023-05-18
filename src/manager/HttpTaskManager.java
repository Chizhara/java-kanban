package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kv.KVTaskClient;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvClient;
    private final Gson gson;
    public HttpTaskManager(String url, Gson gson, HistoryManager historyManager) {
        super(historyManager, "");
        this.gson = gson;
        kvClient = new KVTaskClient(url, 8078);
        kvClient.register();
    }

    @Override
    public void save() {
        kvClient.save("tasks", gson.toJson(getTasks()));
        kvClient.save("epics", gson.toJson(getEpicTasks()));
        kvClient.save("subtasks", gson.toJson(getSubTasks()));
        kvClient.save("history", gson.toJson(getHistory().stream().map(Task::getId).collect(Collectors.toList())));
    }

    public void load() {
        String jsonTasks = kvClient.load("tasks");
        List<Task> tasks = gson.fromJson(jsonTasks, new TypeToken<List<Task>>() {}.getType());
        String jsonEpics = kvClient.load("epics");
        tasks.addAll(gson.fromJson(jsonEpics, new TypeToken<List<EpicTask>>() {}.getType()));
        String jsonSubTasks = kvClient.load("subtasks");
        tasks.addAll(gson.fromJson(jsonSubTasks, new TypeToken<List<SubTask>>() {}.getType()));
        addUndefinedTasks(tasks, this);
        String jsonHistory = kvClient.load("history");
        List<Integer> history = gson.fromJson(jsonHistory, new TypeToken<List<Integer>>() {}.getType());
        historyManager = historyFromList(history);
    }

    private HistoryManager historyFromList(List<Integer> tasksId) {
        HistoryManager historyManager = new InMemoryHistoryManager();
        for(int i = tasksId.size() - 1; i >= 0; i--) {
            for(Task task : getAllTasks()) {
                if(task.getId().equals(tasksId.get(i))) {
                    historyManager.add(task);
                }
            }
        }
        return historyManager;
    }
}
