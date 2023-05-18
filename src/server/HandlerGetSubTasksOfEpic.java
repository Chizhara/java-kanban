package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.FileBackedTasksManager;
import manager.TaskManager;
import model.SubTask;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class HandlerGetSubTasksOfEpic implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;
    public HandlerGetSubTasksOfEpic(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ResponseBuilder responseBuilder;
        try{
            int epicId = getEpicIdFromQuery(httpExchange.getRequestURI().getQuery());
            List<SubTask> subTasksOfEpic = taskManager.getSubTasksOfEpic(epicId);
            responseBuilder = new ResponseBuilder(200, gson.toJson(subTasksOfEpic), ContentTypeHeader.JSON);
        } catch (NumberFormatException | NullPointerException ex) {
            System.out.println("Ошибка выборки подзадач эпика:\nНеккоректное значение идентификатора эпика");
            responseBuilder = new ResponseBuilder(404, "Неккоректное значение идентификатора эпика", ContentTypeHeader.TEXT);
        }

        responseBuilder.writeResponse(httpExchange);
    }

    private int getEpicIdFromQuery(String attribute) {
        return Integer.parseInt(attribute.substring(3));
    }
}
