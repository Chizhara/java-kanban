package manager;

import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    void clearTasks();

    Task getTask(int taskId);

    boolean updateTask(Task task);

    Task removeTask(int taskId);

    ArrayList<SubTask> getSubTasksOfEpic(Integer epicTaskId);

    public List<Task> getHistory();

    Task tryCreateTask(Task task);

    HashMap<Integer ,Task> getTasks();
}
