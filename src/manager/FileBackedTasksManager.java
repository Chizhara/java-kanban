package manager;

import dao.TaskCSVLoader;
import dao.TaskCSVSaver;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager implements TaskManager {
    private final String fileName;
    private final InMemoryTaskManager inMemoryTaskManager;
    public FileBackedTasksManager(HistoryManager historyManager, String fileName) {
        inMemoryTaskManager = new InMemoryTaskManager(historyManager);
        this.fileName = fileName;
    }

    @Override
    public List<Task> getTasks() {
        return inMemoryTaskManager.getTasks();
    }

    @Override
    public List<EpicTask> getEpicTasks() {
        return inMemoryTaskManager.getEpicTasks();
    }

    @Override
    public List<SubTask> getSubTasks() {
        return inMemoryTaskManager.getSubTasks();
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = getTasks();
        tasks.addAll(getEpicTasks());
        tasks.addAll(getSubTasks());
        return  tasks;
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(Integer epicTaskId) {
        return inMemoryTaskManager.getSubTasksOfEpic(epicTaskId);
    }

    @Override
    public void clearTasks() {
        inMemoryTaskManager.clearTasks();
        save();
    }

    @Override
    public void clearEpicTasks() {
        inMemoryTaskManager.clearEpicTasks();
        save();
    }

    @Override
    public void clearSubTasks() {
        inMemoryTaskManager.clearSubTasks();
        save();
    }

    @Override
    public Task getTask(int taskId) {
        Task result = inMemoryTaskManager.getTask(taskId);
        save();
        return  result;
    }

    @Override
    public EpicTask getEpicTask(int taskId) {
        EpicTask result = inMemoryTaskManager.getEpicTask(taskId);
        save();
        return  result;
    }

    @Override
    public SubTask getSubTask(int taskId) {
        SubTask result = inMemoryTaskManager.getSubTask(taskId);
        save();
        return  result;
    }

    @Override
    public boolean updateTask(Task taskDonor) {
        boolean result = inMemoryTaskManager.updateTask(taskDonor);
        save();
        return result;
    }

    @Override
    public boolean updateEpicTask(EpicTask epicTaskDonor) {
        boolean result = inMemoryTaskManager.updateEpicTask(epicTaskDonor);
        save();
        return result;
    }

    @Override
    public boolean updateSubTask(SubTask subTaskDonor) {
        boolean result = inMemoryTaskManager.updateSubTask(subTaskDonor);
        save();
        return result;
    }

    @Override
    public Task removeTask(int taskId) {
        Task result = inMemoryTaskManager.removeTask(taskId);
        save();
        return  result;
    }

    @Override
    public SubTask removeSubTask(int taskId) {
        SubTask result = inMemoryTaskManager.removeSubTask(taskId);
        save();
        return  result;
    }

    @Override
    public EpicTask removeEpicTask(int taskId) {
        EpicTask result = inMemoryTaskManager.removeEpicTask(taskId);
        save();
        return  result;
    }

    @Override
    public Task addTask(Task task) {
        Task result = inMemoryTaskManager.addTask(task);
        save();
        return result;
    }

    @Override
    public EpicTask addEpicTask(EpicTask task) {
        EpicTask result = inMemoryTaskManager.addEpicTask(task);
        save();
        return result;
    }

    @Override
    public SubTask addSubTask(SubTask task) {
        SubTask result = inMemoryTaskManager.addSubTask(task);
        save();
        return result;
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryTaskManager.getHistory();
    }

    private void save() {
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))) {
            TaskCSVSaver taskFileSaver = new TaskCSVSaver(fileWriter);
            taskFileSaver.saveTasks(getAllTasks());
            taskFileSaver.saveHistory(getHistory());
        } catch (IOException exception) {
            System.out.println("Произошла ошибка при попытке сохарнить данные в файл: " + fileName);
            System.out.println(exception.getMessage());
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
            System.out.println("Произошла ошибка при попытке считать данные из файла: " + file.getAbsolutePath());
            System.out.println(exception.getMessage());
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
