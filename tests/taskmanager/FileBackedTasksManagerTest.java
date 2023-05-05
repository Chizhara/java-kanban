package taskmanager;

import manager.FileBackedTasksManager;
import manager.InMemoryHistoryManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final static String fileName = "resources/fileText.txt";

    static Task taskA;
    static EpicTask epicTaskA;
    static SubTask subTaskA;

    @BeforeEach
    void beforeEach() {
        taskA = new Task(1, "TaskA", "TaskA", Instant.MAX.minusSeconds(10000),
                null);
        epicTaskA = new EpicTask(2, "EpicTaskA", "EpicTaskA",
                Instant.MAX.minusSeconds(20000), null);
        subTaskA = new SubTask(3, "SubTaskA", "SubTaskA", epicTaskA.getId(),
                Instant.MAX.minusSeconds(30000), null);
    }

    @Override
    protected void init() {
        taskManager = new FileBackedTasksManager(new InMemoryHistoryManager(), fileName);
    }

    @Test
    void writingCurrentTaskDataToFile() {
        taskManager.addTask(taskA);
        taskManager.addEpicTask(epicTaskA);
        taskManager.addSubTask(subTaskA);

        String fileText = "";

        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            while(fileReader.ready()) {
                fileText += fileReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка! Файл не найден");
        }

        String fileResult = "1,TASK,TaskA,NEW,TaskA," + taskA.getStartTime() + ",0" +
                "2,EPIC_TASK,EpicTaskA,NEW,EpicTaskA," + epicTaskA.getStartTime() + ",0" +
                "3,SUB_TASK,SubTaskA,NEW,SubTaskA," + subTaskA.getStartTime() + ",0,2";

        assertEquals(fileResult, fileText, "Неверная запись в файл");
    }

    @Test
    void readingCurrentTaskDataFromFile() {
        taskManager.addTask(taskA);
        taskManager.addEpicTask(epicTaskA);
        taskManager.addSubTask(subTaskA);

        String fileResult = "1,TASK,TaskA,NEW,TaskA," + taskA.getStartTime() + ",0\n" +
                "2,EPIC_TASK,EpicTaskA,NEW,EpicTaskA," + epicTaskA.getStartTime() + ",0\n" +
                "3,SUB_TASK,SubTaskA,NEW,SubTaskA," + subTaskA.getStartTime() + ",0,2";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(fileResult);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка! Файл не найден");
        }

        taskManager = FileBackedTasksManager.loadFromFile(new File(fileName));

        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Пустой список задач");
        assertTrue(tasks.containsAll(List.of(taskA, epicTaskA, subTaskA)), "Неверное считывание задач");
    }

    @Test
    void writingCurrentHistoryDataToFile() {
        taskManager.addTask(taskA);
        taskManager.addEpicTask(epicTaskA);
        taskManager.addSubTask(subTaskA);

        taskManager.getTask(taskA.getId());
        taskManager.getEpicTask(epicTaskA.getId());
        taskManager.getSubTask(subTaskA.getId());
        taskManager.getTask(taskA.getId());

        String fileText = "";

        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            while(fileReader.ready()) {
                fileText = fileReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка! Файл не найден");
        }

        String fileResult = "1,3,2";

        assertEquals(fileResult, fileText, "Неверная запись в файл");
    }

    @Test
    void readingCurrentHistoryDataFromFile() {
        String fileResult = "1,TASK,TaskA,NEW,TaskA," + taskA.getStartTime() + ",0\n" +
                "2,EPIC_TASK,EpicTaskA,NEW,EpicTaskA," + epicTaskA.getStartTime() + ",0\n" +
                "3,SUB_TASK,SubTaskA,NEW,SubTaskA," + subTaskA.getStartTime() + ",0,2\n\n" + "1,3,2";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(fileResult);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка! Файл не найден");
        }

        taskManager = FileBackedTasksManager.loadFromFile(new File(fileName));
        epicTaskA.addSubTask(subTaskA);

        List<Task> tasksHistory = taskManager.getHistory();
        assertNotNull(tasksHistory, "Пустая история");
        assertEquals(tasksHistory, List.of(taskA, subTaskA, epicTaskA), "Неверное считывание задач");
    }

    @Test
    void readingSubTasksOfEpicFromFile() {
        String fileResult = "1,TASK,TaskA,NEW,TaskA," + taskA.getStartTime() + ",0\n" +
                "2,EPIC_TASK,EpicTaskA,NEW,EpicTaskA," + epicTaskA.getStartTime() + ",0\n" +
                "3,SUB_TASK,SubTaskA,NEW,SubTaskA," + subTaskA.getStartTime() + ",0,2\n\n" + "1,3,2";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(fileResult);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка! Файл не найден");
        }

        taskManager = FileBackedTasksManager.loadFromFile(new File(fileName));

        List<SubTask> subTasks = taskManager.getSubTasksOfEpic(epicTaskA.getId());
        assertNotNull(subTasks, "Список подзадач пустой");
        assertFalse(subTasks.isEmpty(), "Список подзадач пустой");
        assertTrue(subTasks.contains(subTaskA), "Подзадача не связалась с эпиком");
    }
}
