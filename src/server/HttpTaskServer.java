package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    TaskManager tasksManager;
    private final HttpServer httpServer;

    public HttpTaskServer(String path) throws IOException {
        tasksManager = Managers.getDefault();

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);

        Gson gson = Managers.getGson();

        httpServer.createContext("/tasks", new TaskHandler(tasksManager, gson));
        httpServer.createContext("/tasks/subtask/epic/", new HandlerGetSubTasksOfEpic(tasksManager, gson));
        httpServer.start();

    }

    public void stop() {
        httpServer.stop(0);
    }
}
