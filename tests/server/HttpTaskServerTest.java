package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kv.KVServer;
import manager.Managers;
import model.EpicTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServerTest {
    static final String url = "http://localhost:8080/";
    static HttpTaskServer taskServer;
    static HttpClient client;
    Task taskA;
    EpicTask epicTaskA;
    SubTask subTaskA;
    static final Gson gson = Managers.getGson();
    static KVServer server;

    @BeforeAll
    static void beforeAll() throws IOException {
        server = new KVServer();
        server.start();
    }

    @BeforeEach
    void beforeEach() throws IOException {
        taskServer = new HttpTaskServer("");
        client = HttpClient.newHttpClient();

        taskA = new Task(1, "TaskA", "TaskA", Instant.MAX.minusSeconds(10000),
                null);
        epicTaskA = new EpicTask(1, "EpicTaskA", "EpicTaskA",
                Instant.MAX.minusSeconds(20000), null);
        subTaskA = new SubTask(2, "SubTaskA", "SubTaskA", epicTaskA.getId(),
                Instant.MAX.minusSeconds(30000), null);
    }

    @AfterEach
    void afterEach() {
        taskServer.stop();
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }

    @Test
    void shouldAddTask() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/task/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(taskA)))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Task taskR = gson.fromJson(response.body(), Task.class);
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(taskR, taskA, "Задачи не совпадают");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }


    }

    @Test
    void shouldAddEpicTask() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/epic/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epicTaskA)))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            EpicTask epicR = gson.fromJson(response.body(), EpicTask.class);
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(epicR, epicTaskA, "Задачи не совпадают");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }


    }

    @Test
    void shouldAddSubTask() {
        shouldAddEpicTask();
        subTaskA.setEpicTaskId(1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/subtask/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTaskA)))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            SubTask subTaskR = gson.fromJson(response.body(), SubTask.class);
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(subTaskR, subTaskA, "Задачи не совпадают");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldGetTask() {
        shouldAddTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/task/?id=1"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Task taskR = gson.fromJson(response.body(), Task.class);
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(taskR, taskA, "Задачи не совпадают");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldGetEpicTask() {
        shouldAddEpicTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/epic/?id=1"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            EpicTask epicR = gson.fromJson(response.body(), EpicTask.class);
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(epicR, epicTaskA, "Задачи не совпадают");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldGetSubTask() {
        shouldAddSubTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/subtask/?id=2"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            SubTask subTaskR = gson.fromJson(response.body(), SubTask.class);
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(subTaskR, subTaskA, "Задачи не совпадают");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldGetTasks() {
        shouldAddSubTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/task/"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Task> subTaskR = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(subTaskR.size(), 1, "Неккоректная длина списка");
            assertTrue(subTaskR.contains(taskA), "Задача не найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldGetEpicTasks() {
        shouldAddSubTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/epic/"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<EpicTask> subTaskR = gson.fromJson(response.body(), new TypeToken<List<EpicTask>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(subTaskR.size(), 1, "Неккоректная длина списка");
            assertTrue(subTaskR.contains(epicTaskA), "Эпик не найден");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldGetSubTasks() {
        shouldAddSubTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/subtask/"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<SubTask> subTasksR = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(subTasksR.size(), 1, "Неккоректная длина списка");
            assertTrue(subTasksR.contains(subTaskA), "Подзадача не найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldDeleteTask() {
        shouldAddTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/task/?id=1"))
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Task taskR = gson.fromJson(response.body(), Task.class);
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(taskR, taskA, "Задачи не совпадают");
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/task/"))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Task> tasksR = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertFalse(tasksR.contains(taskA), "Подзадача найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldDeleteEpicTask() {
        shouldAddEpicTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/epic/?id=1"))
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            EpicTask epicTaskR = gson.fromJson(response.body(), EpicTask.class);
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(epicTaskR, epicTaskA, "Задачи не совпадают");
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/epic/"))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<EpicTask> epicTasksR = gson.fromJson(response.body(), new TypeToken<List<EpicTask>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertFalse(epicTasksR.contains(epicTaskA), "Эпик найден");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldDeleteSubTask() {
        shouldAddSubTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/subtask/?id=2"))
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            SubTask subTaskR = gson.fromJson(response.body(), SubTask.class);
            assertNotNull(response.body(), "Ничего не вернуло");
            assertEquals(subTaskR, subTaskA, "Задачи не совпадают");
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/subtask/"))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<SubTask> subTasksR = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertTrue(subTasksR.isEmpty(), "Подзадача найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldDeleteTasks() {
        shouldAddTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/task/"))
                .DELETE()
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/task/"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Task> tasksR = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertTrue(tasksR.isEmpty(), "Подзадача найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldDeleteEpicTasks() {
        shouldAddEpicTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/epic/"))
                .DELETE()
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/epic/"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<EpicTask> epicTasksR = gson.fromJson(response.body(), new TypeToken<List<EpicTask>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertFalse(epicTasksR.contains(epicTaskA), "Подзадача найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldDeleteSubTasks() {
        shouldAddSubTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "tasks/subtask/"))
                .DELETE()
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/subtask/"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<SubTask> subtasksR = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertTrue(subtasksR.isEmpty(), "Подзадача найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldGetPrioritizedTasks() {
        shouldAddTask();

        subTaskA.setId(3);
        subTaskA.setEpicTaskId(2);
        List<Task> tasks = new ArrayList<>();
        tasks.add(subTaskA);
        tasks.add(taskA);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/epic/"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epicTaskA)))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/subtask/"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTaskA)))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertFalse(response.body().isEmpty(), "Подзадача найдена");
            assertEquals(response.body(), gson.toJson(tasks),"Подзадача найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldGetHistory() {
        shouldAddTask();
        subTaskA.setId(3);
        subTaskA.setEpicTaskId(2);
        List<Task> tasks = new ArrayList<>();
        tasks.add(subTaskA);
        tasks.add(taskA);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/epic/"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epicTaskA)))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/subtask/"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTaskA)))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/task/?id=1"))
                    .GET()
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/subtask/?id=3"))
                    .GET()
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/history/"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertFalse(response.body().isEmpty(), "Подзадача найдена");
            assertEquals(response.body(), gson.toJson(tasks),"Подзадача найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }

    @Test
    void shouldGetSubTasksOfEpic() {
        shouldAddSubTask();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "tasks/subtask/epic/?id=1"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epicTaskA)))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<SubTask> subTaskR = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {}.getType());
            assertNotNull(response.body(), "Ничего не вернуло");
            assertFalse(response.body().isEmpty(), "Подзадача найдена");
            assertEquals(subTaskR, List.of(subTaskA),"Подзадача найдена");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка!:" + e.getMessage());
        }
    }
}
