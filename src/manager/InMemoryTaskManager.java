package manager;

import java.util.*;

import exceptions.ManagerIntersectsException;
import manager.history.HistoryManager;
import model.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Set<Task> prioritizedTasks;

    private int seq;

    protected HistoryManager historyManager;
    private final EpicTaskManager epicTaskManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        seq = 0;
        epicTaskManager = new EpicTaskManager();
        tasks = new HashMap<>();
        prioritizedTasks = new TreeSet<>((task1, task2) -> {
            if (task2.getStartTime() == null)
                return -1;
            if (task1.getStartTime() == null)
                return 1;
            return task1.getStartTime().compareTo(task2.getStartTime());
        });
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<EpicTask> getEpicTasks() {
        return new ArrayList<>(epicTaskManager.getEpicTasks().values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(epicTaskManager.getSubTasks().values());
    }

    @Override
    public void clearTasks() {
        prioritizedTasks.removeAll(getTasks());
        tasks.clear();
    }

    @Override
    public void clearEpicTasks() {
        for (EpicTask epicTask : getEpicTasks()) {
            prioritizedTasks.remove(epicTask);
        }
        prioritizedTasks.removeAll(getTasks());
        epicTaskManager.clearEpicTasks();
    }

    @Override
    public void clearSubTasks() {
        prioritizedTasks.removeAll(getSubTasks());
        epicTaskManager.clearSubTasks();
    }

    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);

        if (task == null) {
            return null;
        }

        historyManager.add(task);
        return task;
    }

    @Override
    public EpicTask getEpicTask(int taskId) {
        EpicTask task = epicTaskManager.getEpicTasks().get(taskId);

        if (task == null)
            return null;

        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTask(int taskId) {
        SubTask task = epicTaskManager.getSubTasks().get(taskId);

        if (task == null) {
            return null;
        }

        historyManager.add(task);
        return task;
    }

    @Override
    public boolean updateTask(Task taskDonor) {
        Task taskUpdated = tasks.get(taskDonor.getId());

        if (taskUpdated == null) {
            return false;
        }
        if (isHaveIntersects(taskDonor)) {
            return false;
        }

        taskUpdated.updateTaskValues(taskDonor);
        return true;
    }

    @Override
    public boolean updateEpicTask(EpicTask epicTaskDonor) {
        if (isHaveIntersects(epicTaskDonor)) {
            return false;
        }
        return epicTaskManager.updateEpicTask(epicTaskDonor);
    }

    @Override
    public boolean updateSubTask(SubTask subTaskDonor) {
        if (isHaveIntersects(subTaskDonor)) {
            return false;
        }
        return epicTaskManager.updateSubTask(subTaskDonor);
    }

    @Override
    public Task removeTask(int taskId) {
        Task removedTask = tasks.remove(taskId);
        prioritizedTasks.remove(removedTask);
        return removedTask;
    }

    @Override
    public SubTask removeSubTask(int taskId) {
        SubTask removedSubTask = epicTaskManager.removeSubTask(taskId);
        prioritizedTasks.remove(removedSubTask);
        return removedSubTask;
    }

    @Override
    public EpicTask removeEpicTask(int taskId) {
        EpicTask removedEpicTask = epicTaskManager.removeEpicTask(taskId);
        prioritizedTasks.remove(removedEpicTask);
        prioritizedTasks.removeAll(removedEpicTask.getSubTasks());
        return removedEpicTask;
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(Integer epicTaskId) {
        return epicTaskManager.getEpicTasks().get(epicTaskId).getSubTasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task addTask(Task task) {
        task.setId(++seq);
        tasks.put(task.getId(), task);
        add(task);
        return task;
    }

    @Override
    public EpicTask addEpicTask(EpicTask task) {
        task.setId(++seq);
        epicTaskManager.addEpicTask(task);
        return task;
    }

    @Override
    public SubTask addSubTask(SubTask task) {
        task.setId(++seq);
        epicTaskManager.addSubTask(task);
        add(task);
        return task;
    }

    private void add(Task task) {
        if (isHaveIntersects(task)) {
            throw new ManagerIntersectsException("Найдено пересечение");
        }
        prioritizedTasks.add(task);
    }

    public boolean isHaveIntersects(Task checkedTask) {
        for (Task task : prioritizedTasks) {
            if (task.getId().equals(checkedTask.getId())) {
                continue;
            }
            if (!checkedTask.getStartTime().isAfter(task.getEndTime())
                    && !checkedTask.getEndTime().isBefore(task.getStartTime())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "manager.TaskManager{" +
                "tasks.size()=" + getTasks().size() +
                '}';
    }
}
