package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ManagerIntersectsException;
import exceptions.ManagerSubTaskAddException;
import manager.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler implements HttpHandler {
    private String[] pathParts;
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        pathParts = exchange.getRequestURI().getPath().split("/");
        Target target = getRequestTarget(exchange.getRequestMethod());
        ResponseBuilder responseBuilder;

        switch (target) {
            case GET_PRIORITIZED_TASKS:
                responseBuilder = handleGetPrioritizedTask();
                break;
            case GET_HISTORY:
                responseBuilder = handleGetHistory();
                break;
            default:
                TaskType taskType = getTaskTypeFromLine();
                switch (target) {
                    case GET:
                        responseBuilder = handleGet(taskType, exchange.getRequestURI().getRawQuery());
                        break;
                    case DELETE:
                        responseBuilder = handleDelete(taskType, exchange.getRequestURI().getRawQuery());
                        break;
                    case POST:
                        responseBuilder = handlePost(exchange, taskType);
                        break;
                    default:
                        responseBuilder = new ResponseBuilder(400, "Некорректный эндроинт", ContentTypeHeader.TEXT);
                        break;
                }
        }

        responseBuilder.writeResponse(exchange);
    }

    private Target getRequestTarget(String requestMethod) {
        String lastPart = pathParts[pathParts.length - 1];

        if (lastPart.equals("tasks") && pathParts.length == 2 && requestMethod.equals("GET")) {
            return Target.GET_PRIORITIZED_TASKS;
        } else if (lastPart.equals("history") && pathParts.length == 3 && requestMethod.equals("GET")) {
            return Target.GET_HISTORY;
        } else if (pathParts.length == 3 || pathParts.length == 4) {
            switch (requestMethod) {
                case "POST":
                    return Target.POST;
                case "GET":
                    return Target.GET;
                case "DELETE":
                    return Target.DELETE;
            }
        }

        return Target.UNKNOWN;
    }

    private TaskType getTaskTypeFromLine() {
        String taskTypeLine = pathParts[2];

        switch (taskTypeLine) {
            case "task":
                return TaskType.TASK;
            case "subtask":
                return TaskType.SUB_TASK;
            case "epic":
                return TaskType.EPIC_TASK;
            default:
                throw new NullPointerException();
        }
    }

    private ResponseBuilder handleGetPrioritizedTask() {
        return new ResponseBuilder(200, gson.toJson(taskManager.getPrioritizedTasks()), ContentTypeHeader.JSON);
    }

    private ResponseBuilder handleGetHistory() {
        return new ResponseBuilder(200, gson.toJson(taskManager.getHistory()), ContentTypeHeader.JSON);
    }

    private ResponseBuilder handlePost(HttpExchange exchange, TaskType taskType) throws IOException {
        if (pathParts.length != 3) {
            System.out.println("Пользователь неверно указал эндпоинт: " + exchange.getRequestURI());
            return new ResponseBuilder(400, "Некорректный эндпоинт",
                    ContentTypeHeader.TEXT);
        }
        try {
            String bodyJson = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task task = getPostedTask(taskType, bodyJson);
            return new ResponseBuilder(200, gson.toJson(task), ContentTypeHeader.JSON);
        } catch (NullPointerException ex) {
            System.out.println("Пользователь неверно указал эндпоинт как тип задачи:" + exchange.getRequestURI());
            return new ResponseBuilder(400, "Неверно указан тип задачи: " + pathParts[2],
                    ContentTypeHeader.TEXT);
        } catch (JsonSyntaxException ex) {
            System.out.println("Пользователь неверно указал эндпоинт как тип задачи:" + exchange.getRequestURI());
            return new ResponseBuilder(404, "Переданы некорректные данны", ContentTypeHeader.TEXT);
        } catch (ManagerIntersectsException ex) {
            System.out.println("У добавляемой задачи есть пересечения" + exchange.getRequestURI());
            return new ResponseBuilder(404, "Переданы некорректные данны", ContentTypeHeader.TEXT);
        } catch (ManagerSubTaskAddException ex) {
            System.out.println("Не найден эпик подзадачи" + exchange.getRequestURI());
            return new ResponseBuilder(404, "Эпик передаваемой задачи не найден", ContentTypeHeader.TEXT);
        }
    }

    private Task getPostedTask(TaskType taskType, String taskJson) {
        switch (taskType) {
            case TASK:
                Task task = parseTaskFromJson(taskJson, Task.class);
                if (isTasksContainsId(taskManager.getTasks(), task.getId())) {
                    taskManager.updateTask(task);
                } else {
                    taskManager.addTask(task);
                }
                return task;
            case SUB_TASK:
                SubTask subTask = parseTaskFromJson(taskJson, SubTask.class);
                if (isTasksContainsId(taskManager.getSubTasks(), subTask.getId())) {
                    taskManager.updateSubTask(subTask);
                } else {
                    taskManager.addSubTask(subTask);
                }
                return subTask;
            case EPIC_TASK:
                EpicTask epicTask = parseTaskFromJson(taskJson, EpicTask.class);
                if (isTasksContainsId(taskManager.getEpicTasks(), epicTask.getId())) {
                    taskManager.updateEpicTask(epicTask);
                } else {
                    taskManager.addEpicTask(epicTask);
                }
                return epicTask;
            default:
                throw new NullPointerException();
        }
    }

    private <T extends Task> boolean isTasksContainsId(List<T> tasks, int taskId) {
        for (T task : tasks) {
            if (task.getId() == taskId) {
                return true;
            }
        }
        return false;
    }

    private <T extends Task> T parseTaskFromJson(String taskJson, Class<T> taskClass) throws JsonSyntaxException {
        return gson.fromJson(taskJson, taskClass);
    }

    private ResponseBuilder handleGet(TaskType taskType, String rawQuery) {
        if (!rawQuery.isEmpty()) {
            try {
                Integer id = getTaskIdFromQuery(rawQuery);
                Task task = handleGetConcreteTaskOfType(taskType, id);
                if (task == null) {
                    throw new NullPointerException();
                }
                String taskJson = gson.toJson(task);
                return new ResponseBuilder(200, taskJson, ContentTypeHeader.JSON);
            } catch (NullPointerException ex) {
                return new ResponseBuilder(404, "Неверно указан идентификатор задачи",
                        ContentTypeHeader.TEXT);
            } catch (NumberFormatException ex) {
                return new ResponseBuilder(400, "Вместо идентификатора указано неккоретное значение",
                        ContentTypeHeader.TEXT);
            }
        } else {
            return new ResponseBuilder(200, gson.toJson(handleGetTasksOfType(taskType)), ContentTypeHeader.JSON);
        }
    }

    private Integer getTaskIdFromQuery(String query) {
        return Integer.parseInt(query.substring(3));
    }

    private Task handleGetConcreteTaskOfType(TaskType taskType, int taskId) {
        switch (taskType) {
            case TASK:
                return taskManager.getTask(taskId);
            case EPIC_TASK:
                return taskManager.getEpicTask(taskId);
            case SUB_TASK:
                return taskManager.getSubTask(taskId);
            default:
                return null;
        }
    }

    private List<? extends Task> handleGetTasksOfType(TaskType taskType) {
        switch (taskType) {
            case TASK:
                return taskManager.getTasks();
            case EPIC_TASK:
                return taskManager.getEpicTasks();
            case SUB_TASK:
                return taskManager.getSubTasks();
            default:
                throw new NullPointerException();
        }
    }

    private ResponseBuilder handleDelete(TaskType taskType, String rawQuery) {
        if (!rawQuery.isEmpty()) {
            try {
                Task task = handleDeleteConcreteTaskOfType(taskType, getTaskIdFromQuery(rawQuery));
                if (task == null) {
                    throw new NullPointerException();
                }
                String taskJson = gson.toJson(task);
                return new ResponseBuilder(200, taskJson, ContentTypeHeader.JSON);
            } catch (NullPointerException ex) {
                return new ResponseBuilder(404, "Неверно указан идентификатор задачи",
                        ContentTypeHeader.TEXT);
            } catch (NumberFormatException ex) {
                return new ResponseBuilder(400, "Вместо идентификатора указано неккоретное значение",
                        ContentTypeHeader.TEXT);
            }
        } else {
            handleDeleteTasksOfType(taskType);
            return new ResponseBuilder(200, "Задачи типа " + taskType + " удалены", ContentTypeHeader.TEXT);
        }
    }

    private Task handleDeleteConcreteTaskOfType(TaskType taskType, int taskId) {
        switch (taskType) {
            case TASK:
                return taskManager.removeTask(taskId);
            case EPIC_TASK:
                return taskManager.removeEpicTask(taskId);
            case SUB_TASK:
                return taskManager.removeSubTask(taskId);
            default:
                return null;
        }
    }

    private void handleDeleteTasksOfType(TaskType taskType) {
        switch (taskType) {
            case TASK:
                taskManager.clearTasks();
                break;
            case SUB_TASK:
                taskManager.clearSubTasks();
                break;
            case EPIC_TASK:
                taskManager.clearEpicTasks();
                break;
            default:
                return;
        }
    }

    enum Target {GET, DELETE, POST, GET_HISTORY, GET_PRIORITIZED_TASKS, UNKNOWN}
}
