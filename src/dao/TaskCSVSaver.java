package dao;

import model.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class TaskCSVSaver {
    BufferedWriter fileWriter;

    public TaskCSVSaver(BufferedWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void saveTasks(List<Task> tasks) throws IOException {
        for(Task task : tasks) {
            fileWriter.write(formatTaskToLine(task));
        }
    }

    private String formatTaskToLine(Task task) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%d,%s,%s,%s,%s" ,task.getId(), task.getTaskType(), task.getName(),
                task.getStatus(), task.getDescription()));

        if(task.getTaskType() == TaskType.SUB_TASK) {
            result.append(',');
            result.append(((SubTask) task).getEpicTaskId());
        }
        result.append('\n');

        return result.toString();
    }

    public void saveHistory(List<Task> history) throws IOException {
        StringBuilder historyLine = new StringBuilder();
        historyLine.append('\n');
        for(Task task : history) {
            historyLine.append(task.getId().toString());
            historyLine.append(',');
        }
        historyLine.delete(historyLine.length() - 1, historyLine.length());

        fileWriter.write(historyLine.toString());
    }
}
