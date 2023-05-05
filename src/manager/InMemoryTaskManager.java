package manager;

import java.time.Instant;
import java.util.*;

import model.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer ,Task> tasks;

    private Set<Task> prioritizedTasks;

    private final ManagerSeq seq;

    private final HistoryManager historyManager;
    private final EpicTaskManager epicTaskManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        seq = new ManagerSeq();
        epicTaskManager = new EpicTaskManager();
        tasks = new HashMap<>();
        prioritizedTasks = new TreeSet<>((task1, task2) -> {
            if(task2.getStartTime() == null)
                return -1;
            if(task1.getStartTime() == null)
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
        for(EpicTask epicTask : getEpicTasks()) {
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

        if(task == null)
            return null;

        historyManager.add(task);
        return task;
    }

    @Override
    public EpicTask getEpicTask(int taskId) {
        EpicTask task = epicTaskManager.getEpicTasks().get(taskId);

        if(task == null)
            return null;

        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTask(int taskId) {
        SubTask task = epicTaskManager.getSubTasks().get(taskId);

        if(task == null)
            return null;

        historyManager.add(task);
        return task;
    }

    @Override
    public boolean updateTask(Task taskDonor) {
        if(isHaveIntersects(taskDonor)) {
            return false;
        }

        Map<Integer, Task> tasks = new HashMap<>(this.tasks);
        Task taskUpdated = tasks.get(taskDonor.getId());

        if(taskUpdated == null)
            return false;

        taskUpdated.updateTaskValues(taskDonor);

        return true;
    }

    @Override
    public boolean updateEpicTask(EpicTask epicTaskDonor) {
        if(isHaveIntersects(epicTaskDonor)) {
            return false;
        }
        return epicTaskManager.updateEpicTask(epicTaskDonor);
    }

    @Override
    public boolean updateSubTask(SubTask subTaskDonor) {
        if(isHaveIntersects(subTaskDonor)) {
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
        task.setId(seq.getNextSeq());
        if(isHaveIntersects(task)) {
            return null;
        }
        tasks.put(seq.getSeq(), task);
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public EpicTask addEpicTask(EpicTask task) {
        task.setId(seq.getNextSeq());
        if(isHaveIntersects(task)) {
            return null;
        }
        epicTaskManager.addEpicTask(task);
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public SubTask addSubTask(SubTask task) {
        task.setId(seq.getNextSeq());
        if(isHaveIntersects(task)) {
            return null;
        }
        SubTask subTask = epicTaskManager.addSubTask(task);
        if(subTask == null) {
            return null;
        }
        prioritizedTasks.add(subTask);
        return subTask;
    }

     public boolean isHaveIntersects(Task checkedTask) {
        boolean result = false;

        if(checkedTask instanceof EpicTask || checkedTask.getStartTime() == null) {
            return false;
        }

        for(Task task : prioritizedTasks) {
            if(!(task instanceof EpicTask || task.getStartTime() == null
                    || !task.getId().equals(checkedTask.getId()))) {
                if(!checkedTask.getStartTime().isAfter(task.getEndTime())
                        && !checkedTask.getEndTime().isBefore(task.getStartTime())) {
                        result = true;
                        break;
                    }
                }
        }
        return result;
    }

    @Override
    public String toString() {
        return "manager.TaskManager{" +
                "tasks.size()=" + getTasks().size() +
                '}';
    }
}
