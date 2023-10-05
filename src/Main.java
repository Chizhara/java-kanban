import kv.KVServer;
import manager.HttpTaskManager;
import manager.Managers;
import model.Task;
import server.HttpTaskServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            KVServer server = new KVServer();
            server.start();
            HttpTaskManager taskManager = new HttpTaskManager("http://localhost:8078/", Managers.getGson(), Managers.getDefaultHistory());
            Task task = new Task(null, "Task", "Task", null, null);
            taskManager.addTask(task);
            taskManager.getAllTasks();
            HttpTaskManager taskManager2 = new HttpTaskManager("http://localhost:8078/", Managers.getGson(), Managers.getDefaultHistory());
            taskManager2.load();
            System.out.println(taskManager2.getTasks());
        } catch (IOException ex) {
            System.out.println("Ошибка запуска KVS");
        }


        try {
            HttpTaskServer httpTaskServer = new HttpTaskServer("fileText.txt");
        } catch (Exception ex) {
            System.out.println("ERRRRRRRROR");
        }
    }
}
