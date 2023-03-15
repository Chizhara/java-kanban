package manager;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final LinkedList<Task> taskHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        taskHistory.add(task);

        if(taskHistory.size() > 10)
            taskHistory.removeFirst();
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}
