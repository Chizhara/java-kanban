package taskmanager;

import kv.KVServer;
import manager.HttpTaskManager;
import manager.Managers;
import model.EpicTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskServerTest extends TaskManagerTest<HttpTaskManager> {
    private final static String fileName = "resources/fileText.txt";

    static Task taskA;
    static EpicTask epicTaskA;
    static SubTask subTaskA;
    static KVServer server;

    @BeforeAll
    static void beforeAll() throws IOException {
        server = new KVServer();
        server.start();
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }

    @Override
    protected void init() {
        taskManager = new HttpTaskManager("http://localhost:8078/" , Managers.getGson(), Managers.getDefaultHistory());
        taskManager.save();
        taskA = new Task(1, "TaskA", "TaskA", Instant.MAX.minusSeconds(10000),
                null);
        epicTaskA = new EpicTask(2, "EpicTaskA", "EpicTaskA",
                Instant.MAX.minusSeconds(20000), null);
        subTaskA = new SubTask(3, "SubTaskA", "SubTaskA", epicTaskA.getId(),
                Instant.MAX.minusSeconds(30000), null);
    }

    @Test
    void loadCurrentDataFromServer() {
        taskManager.addTask(taskA);
        taskManager.addEpicTask(epicTaskA);
        taskManager.addSubTask(subTaskA);

        taskManager = new HttpTaskManager("http://localhost:8078/" , Managers.getGson(), Managers.getDefaultHistory());
        taskManager.load();

        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Пустой список задач");
        epicTaskA.addSubTask(subTaskA);
        assertTrue(tasks.containsAll(List.of(taskA, epicTaskA, subTaskA)), "Неверное считывание задач");
    }

}
