package manager;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    Set<Task> getPrioritizedTasks();

    List<EpicTask> getEpicTasks();

    List<SubTask> getSubTasks();

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

    List<SubTask> getSubTasksOfEpic(Integer epicTaskId);

    public List<Task> getHistory();

    List<Task> getTasks();

    Task addTask(Task task);

    EpicTask addEpicTask(EpicTask task);

    SubTask addSubTask(SubTask task);
}
