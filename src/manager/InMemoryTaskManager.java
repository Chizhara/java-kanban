package manager;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer ,Task> tasks = new HashMap<>();
    private final ManagerSeq seq;

    private final HistoryManager historyManager;
    private final EpicTaskManager epicTaskManager;

    public InMemoryTaskManager(HistoryManager historyManager){
        this.historyManager = historyManager;
        seq = new ManagerSeq();
        epicTaskManager = new EpicTaskManager(seq);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<EpicTask> getEpicTasks() {
        return new ArrayList<>(epicTaskManager.getEpicTasks().values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(epicTaskManager.getSubTasks().values());
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpicTasks() {
        epicTaskManager.clearEpicTasks();
    }

    @Override
    public void clearSubTasks() {
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
    public boolean updateTask(Task TaskDonor) {
        HashMap<Integer, Task> tasks = this.tasks;

        if(!tasks.containsKey(TaskDonor.getId()))
            return false;
        if(TaskDonor.getTaskType() != TaskType.TASK)
            return false;

        Task taskUpdated = tasks.get(TaskDonor.getId());
        updateTaskValues(taskUpdated, TaskDonor);

        return true;
    }

    @Override
    public boolean updateEpicTask(EpicTask epicTaskDonor) {
        HashMap<Integer, EpicTask> epicTasks = epicTaskManager.getEpicTasks();

        if(!epicTasks.containsKey(epicTaskDonor.getId()))
            return false;
        if(epicTaskDonor.getTaskType() != TaskType.EPIC_TASK)
            return false;

        EpicTask taskUpdated = epicTasks.get(epicTaskDonor.getId());

        updateTaskValues(taskUpdated, epicTaskDonor);
        epicTaskManager.updateSubTasksListOfEpic(taskUpdated, epicTaskDonor.getSubTasks());

        return true;
    }

    @Override
    public boolean updateSubTask(SubTask subTaskDonor) {
        HashMap<Integer, SubTask> subTasks = epicTaskManager.getSubTasks();

        if(!subTasks.containsKey(subTaskDonor.getId()))
            return false;
        if(subTaskDonor.getTaskType() != TaskType.SUB_TASK)
            return false;

        SubTask taskUpdated = subTasks.get(subTaskDonor.getId());

        updateTaskValues(taskUpdated, subTaskDonor);
        epicTaskManager.updateEpicSubTask(taskUpdated, epicTaskManager.getEpicTasks().get(subTaskDonor.getEpicTaskId()));

        return true;
    }

    @Override
    public Task removeTask(int taskId) {
        return tasks.remove(taskId);
    }

    @Override
    public SubTask removeSubTask(int taskId) {
        return epicTaskManager.removeSubTask(taskId);
    }

    @Override
    public EpicTask removeEpicTask(int taskId) {
        return epicTaskManager.removeEpicTask(taskId);
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(Integer epicTaskId) {
        return epicTaskManager.getEpicTasks().get(epicTaskId).getSubTasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task addTask(Task task) {
        task.setId(seq.getNextSeq());
        tasks.put(seq.getSeq(), task);
        return task;
    }

    @Override
    public EpicTask addEpicTask(EpicTask task) {
        return epicTaskManager.addEpicTask(task);
    }

    @Override
    public SubTask addSubTask(SubTask task) {
        return epicTaskManager.addSubTask(task);
    }

    private void updateTaskValues(Task taskUpdated,Task taskDonor){
        taskUpdated.setName(taskDonor.getName());
        taskUpdated.setDescription(taskDonor.getDescription());
        taskUpdated.setStatus(taskDonor.getStatus());
    }

    @Override
    public String toString() {
        return "manager.TaskManager{" +
                "tasks.size()=" + getTasks().size() +
                '}';
    }
}
