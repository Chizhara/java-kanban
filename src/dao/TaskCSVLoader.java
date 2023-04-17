package dao;

import manager.HistoryManager;
import model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskCSVLoader {
    BufferedReader fileReader;
    public TaskCSVLoader(BufferedReader fileReader) {
        this.fileReader = fileReader;
    }

    public List<Task> loadDataFromFile(HistoryManager historyManager) throws IOException {
        List<Task> tasks = new ArrayList<>();

        while(fileReader.ready()) {
            String line = fileReader.readLine();
            if(!line.isBlank()) {
                tasks.add(taskFromCSVString(line));
            } else {
                line = fileReader.readLine();
                historyFromCSVString(line, historyManager, tasks);
            }
        }

        return tasks;
    }

    private Task taskFromCSVString(String line) {
        Task result;

        String[] taskAttributes = line.split(",");
        int id = Integer.parseInt(taskAttributes[0]);
        TaskType type = TaskType.valueOf(taskAttributes[1]);
        String name = taskAttributes[2];
        TaskStatus status = TaskStatus.valueOf(taskAttributes[3]);
        String description = taskAttributes[4];

        switch (type) {
            case TASK:
                result = new Task(id, name, description);
                result.setStatus(status);
                break;
            case EPIC_TASK:
                result = new EpicTask(id, name, description);
                break;
            case SUB_TASK:
                int epicId = Integer.parseInt(taskAttributes[5]);
                result = new SubTask(id, name, description, epicId);
                result.setStatus(status);
                break;
            default:
                result = null;
        }

        return result;
    }

    private void historyFromCSVString(String line, HistoryManager historyManager, List<Task> tasks) {
        String[] taskIdLines = line.split(",");
        for(int i = taskIdLines.length - 1; i >= 0; i--) {
            int taskId = Integer.parseInt(taskIdLines[i]);
            for(Task task : tasks) {
                if(task.getId() == taskId)
                    historyManager.add(task);
            }
        }
    }
}
