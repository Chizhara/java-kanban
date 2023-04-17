package manager;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final String fileName;

    public FileBackedTasksManager(HistoryManager historyManager, String fileName) {
        super(historyManager);
        this.fileName = fileName;
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

    private void save() {
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))) {
            saveTasks(fileWriter);
        } catch (IOException exception) {
            System.out.println("Произошла ошибка при попытке сохарнить данные в файл: " + fileName);
            System.out.println(exception.getMessage());
        }
    }

    private void saveTasks(BufferedWriter fileWriter) throws IOException {
        List<Task> tasks = super.getTasks();
        tasks.addAll(super.getEpicTasks());
        tasks.addAll(super.getSubTasks());

        for(Task task : tasks) {
            fileWriter.write(formatTaskToLine(task));
        }
    }

    private String formatTaskToLine(Task task) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%d,%s,%s,%s" ,task.getId(), task.getTaskType(), task.getStatus(), task.getDescription()));

        if(task.getTaskType() == TaskType.SUB_TASK) {
            result.append(',');
            result.append(((SubTask) task).getEpicTaskId());
        }
        result.append('\n');

        return result.toString();
    }
}
