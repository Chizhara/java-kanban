package manager;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer ,Task> tasks = new HashMap<>();
    ManagerSeq seq;

    private HistoryManager historyManager;
    private EpicTaskManager epicTaskManager;

    public InMemoryTaskManager(HistoryManager historyManager){
        this.historyManager = historyManager;
        seq = new ManagerSeq();
        epicTaskManager = new EpicTaskManager(seq);
    }

    @Override
    public HashMap<Integer ,Task> getTasks() {
        HashMap<Integer, Task> result = new HashMap<>();

        result.putAll(tasks);
        result.putAll(epicTaskManager.getEpicTasks());
        result.putAll(epicTaskManager.getSubTasks());

        return result;
    }

    @Override
    public void clearTasks() {
        tasks.clear();
        epicTaskManager.clear();
    }

    @Override
    public Task getTask(int taskId) {
        Task task = getTasks().get(taskId);

        if(task == null)
            return null;

        historyManager.add(task);
        return task;
    }

    @Override
    public boolean updateTask(Task task) {
        HashMap<Integer, Task> tasks = getTasks();

        if(!tasks.containsKey(task.getId()))
            return false;
        if(tasks.get(task.getId()).getTaskType() != task.getTaskType())
            return false;

        Task concreteTask = tasks.get(task.getId());

        concreteTask.setName(task.getName());
        concreteTask.setDescription(task.getDescription());
        concreteTask.setStatus(task.getStatus());

        return true;
    }

    @Override
    public Task removeTask(int taskId) {
        Task result = getTask(taskId);

        switch(result.getTaskType()){
            case TASK:
                tasks.remove(taskId);
                break;
            case SUB_TASK:
                epicTaskManager.removeSubTask((SubTask) result);
                break;
            case EPIC_TASK:
                epicTaskManager.removeEpicTask((EpicTask) result);
                break;
            default:
                result = null;
        }

        return result;
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(Integer epicTaskId) {
        Task epicTask = getTasks().get(epicTaskId);

        if(epicTask != null && epicTask.getTaskType() == TaskType.EPIC_TASK)
            return ((EpicTask) epicTask).getSubTasks();

        return null;
    }

    @Override
    public Task tryCreateTask(Task task) {
        if (task == null)
            return null;

        Task result;

        switch(task.getTaskType()) {
            case TASK:
                result = addTask(task);
                break;
            case EPIC_TASK:
                result = epicTaskManager.addTask(task);
                break;
            case SUB_TASK:
                result = epicTaskManager.tryCreateSubTask((SubTask) task);
                break;
            default:
                result = null;
        }

        return result;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private Task addTask(Task task) {
        Task taskToAdd = new Task(seq.getNextSeq() ,task);
        tasks.put(taskToAdd.getId(), taskToAdd);
        return taskToAdd;
    }

    @Override
    public String toString() {
        return "manager.TaskManager{" +
                "tasks.size()=" + getTasks().size() +
                '}';
    }
}
