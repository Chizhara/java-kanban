package manager;

import dao.TaskCSVLoader;
import dao.TaskCSVSaver;
import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String fileName;
    public FileBackedTasksManager(HistoryManager historyManager, String fileName) {
        super(historyManager);
        this.fileName = fileName;
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<EpicTask> getEpicTasks() {
        return super.getEpicTasks();
    }

    @Override
    public List<SubTask> getSubTasks() {
        return super.getSubTasks();
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = getTasks();
        tasks.addAll(getEpicTasks());
        tasks.addAll(getSubTasks());
        return  tasks;
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(Integer epicTaskId) {
        return super.getSubTasksOfEpic(epicTaskId);
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpicTasks() {
        super.clearEpicTasks();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public Task getTask(int taskId) {
        Task result = super.getTask(taskId);
        save();
        return  result;
    }

    @Override
    public EpicTask getEpicTask(int taskId) {
        EpicTask result = super.getEpicTask(taskId);
        save();
        return  result;
    }

    @Override
    public SubTask getSubTask(int taskId) {
        SubTask result = super.getSubTask(taskId);
        save();
        return  result;
    }

    @Override
    public boolean updateTask(Task taskDonor) {
        boolean result = super.updateTask(taskDonor);
        save();
        return result;
    }

    @Override
    public boolean updateEpicTask(EpicTask epicTaskDonor) {
        boolean result = super.updateEpicTask(epicTaskDonor);
        save();
        return result;
    }

    @Override
    public boolean updateSubTask(SubTask subTaskDonor) {
        boolean result = super.updateSubTask(subTaskDonor);
        save();
        return result;
    }

    @Override
    public Task removeTask(int taskId) {
        Task result = super.removeTask(taskId);
        save();
        return  result;
    }

    @Override
    public SubTask removeSubTask(int taskId) {
        SubTask result = super.removeSubTask(taskId);
        save();
        return  result;
    }

    @Override
    public EpicTask removeEpicTask(int taskId) {
        EpicTask result = super.removeEpicTask(taskId);
        save();
        return  result;
    }

    @Override
    public Task addTask(Task task) {
        Task result = super.addTask(task);
        save();
        return result;
    }

    @Override
    public EpicTask addEpicTask(EpicTask task) {
        EpicTask result = super.addEpicTask(task);
        save();
        return result;
    }

    @Override
    public SubTask addSubTask(SubTask task) {
        SubTask result = super.addSubTask(task);
        save();
        return result;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    private void save() {
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))) {
            TaskCSVSaver taskFileSaver = new TaskCSVSaver(fileWriter);
            taskFileSaver.saveTasks(getAllTasks());
            taskFileSaver.saveHistory(getHistory());
        } catch (IOException exception) {
            throw new ManagerSaveException("Произошла ошибка при попытке считать данные из файла: " + fileName);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        List<Task> tasks;
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(historyManager, file.getAbsolutePath());

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            TaskCSVLoader taskCSVLoader = new TaskCSVLoader(fileReader);
            tasks = taskCSVLoader.loadDataFromFile(historyManager);
            addUndefinedTasks(tasks, tasksManager);
        } catch (IOException exception) {
            throw new ManagerSaveException("Произошла ошибка при попытке считать данные из файла: " + file.getAbsolutePath());
        }

        return tasksManager;
    }

    private static void addUndefinedTasks(List<Task> tasks, TaskManager tasksManager) {
        for(Task task : tasks) {
            switch(task.getTaskType()) {
                case TASK:
                    tasksManager.addTask(task);
                    break;
                case EPIC_TASK:
                    tasksManager.addEpicTask((EpicTask) task);
                    break;
                case SUB_TASK:
                    tasksManager.addSubTask((SubTask) task);
                    break;
            }
        }
    }
}
