package manager;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    ArrayList<EpicTask> getEpicTasks();

    ArrayList<SubTask> getSubTasks();

    void clearTasks();

    void clearEpicTasks();

    void clearSubTasks();

    Task getTask(int taskId);

    EpicTask getEpicTask(int taskId);

    SubTask getSubTask(int taskId);

    boolean updateTask(Task task);

    boolean updateEpicTask(EpicTask task);

    boolean updateSubTask(SubTask task);

    Task removeTask(int taskId);

    SubTask removeSubTask(int taskId);

    EpicTask removeEpicTask(int taskId);

    ArrayList<SubTask> getSubTasksOfEpic(Integer epicTaskId);

    public List<Task> getHistory();

    ArrayList<Task> getTasks();

    Task addTask(Task task);

    EpicTask addEpicTask(EpicTask task);

    SubTask addSubTask(SubTask task);
}
