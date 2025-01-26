package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/*Еще вопрос насчет передачи объекта в json формате. Почему я могу передать только duration и startTime
и этот объект создастся с полями name-null description-null status-null, как сделать их обязательными? Ведь нельзя
создать объект без полей назания и описания по логике задания. Получется конструкторы никак не влияют на десериализацию
объекта через Gson */

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        switch (method) {
            case "GET" -> handleGetTasks(exchange, pathParts);
            case "POST" -> handlePostTasks(exchange);
            case "DELETE" -> handleDeleteTasks(exchange, pathParts);
            default -> sendCode404(exchange, "Метод " + method + "недоступен");
        }
    }

    private void handleGetTasks(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            sendCode200(exchange, gson.toJson(taskManager.getTasks()));
        } else if (pathParts.length == 3) {
            try {
                Optional<Task> task = taskManager.getTaskById(Integer.parseInt(pathParts[2]));
                if (task.isEmpty()) {
                    sendCode404(exchange, gson.toJson("такой задачи нет"));
                } else {
                    sendCode200(exchange, gson.toJson(task.get()));
                }
            } catch (NumberFormatException e) {
                sendCode404(exchange, gson.toJson("введите id задачи, которую вы запрашиваете"));
            }

        }

    }

    private void handlePostTasks(HttpExchange exchange) throws IOException {
        String task = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Task newTask = gson.fromJson(task, Task.class);
            Optional<Task> optionalTask = taskManager.getTaskById(newTask.getId());
            if (optionalTask.isEmpty()) {
                taskManager.createTask(newTask);
                sendCode201(exchange, gson.toJson("новый таск добавлен"));
            }
            if (optionalTask.isPresent()) {
                taskManager.updateTask(newTask);
                sendCode201(exchange, gson.toJson("таск обновлен"));
            }
        } catch (JsonSyntaxException e) {
            sendCode404(exchange, gson.toJson("неправильно передан таск"));
        }
    }

    private void handleDeleteTasks(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 3) {
            try {
                Optional<Task> task = taskManager.getTaskById(Integer.parseInt(pathParts[2]));
                if (task.isEmpty()) {
                    sendCode404(exchange, gson.toJson("такой задачи нет"));
                } else {
                    taskManager.deleteTaskById(task.get().getId());
                    sendCode200(exchange, "Таск успешно удален");
                }
            } catch (NumberFormatException e) {
                sendCode404(exchange, gson.toJson("введите id задачи, которую вы запрашиваете"));
            }

        }
    }
}