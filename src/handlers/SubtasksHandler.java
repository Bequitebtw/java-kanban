package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        switch (method) {
            case "GET" -> handleGetSubtasks(exchange, pathParts);
            case "POST" -> handlePostSubtasks(exchange);
            case "DELETE" -> handleDeleteSubtasks(exchange, pathParts);
            default -> sendCode404(exchange, "Метод " + method + "недоступен");
        }
    }

    private void handleGetSubtasks(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            sendCode200(exchange, gson.toJson(taskManager.getSubtasks()));
        } else if (pathParts.length == 3) {
            try {
                Optional<Subtask> subtask = taskManager.getSubtaskById(Integer.parseInt(pathParts[2]));
                if (subtask.isEmpty()) {
                    sendCode404(exchange, gson.toJson("такой подзадачи нет"));
                } else {
                    sendCode200(exchange, gson.toJson(subtask.get()));
                }
            } catch (NumberFormatException e) {
                sendCode404(exchange, gson.toJson("введите id подзадачи, которую вы запрашиваете"));
            }
        }

    }

    private void handlePostSubtasks(HttpExchange exchange) throws IOException {
        String subtask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Subtask newSubtask = gson.fromJson(subtask, Subtask.class);
            Optional<Subtask> optionalSubtask = taskManager.getSubtaskById(newSubtask.getId());
            if (optionalSubtask.isPresent()) {
                taskManager.updateSubtask(newSubtask);
                sendCode201(exchange, gson.toJson("сабтаск обновлен"));
                return;
            }
            if (optionalSubtask.isEmpty() && taskManager.getEpicById(newSubtask.getEpicId()).isPresent()) {
                taskManager.createSubtask(newSubtask, newSubtask.getEpicId());
                sendCode201(exchange, gson.toJson("новый сабтаск добавлен"));
            } else {
                sendCode404(exchange, gson.toJson("нет такого эпика или он не был передан"));
            }
        } catch (JsonSyntaxException e) {
            sendCode404(exchange, gson.toJson("неправильно передан сабтаск"));
        }
    }

    private void handleDeleteSubtasks(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 3) {
            try {
                Optional<Subtask> subtask = taskManager.getSubtaskById(Integer.parseInt(pathParts[2]));
                if (subtask.isEmpty()) {
                    sendCode404(exchange, gson.toJson("такой подзадачи нет"));
                } else {
                    taskManager.deleteSubtaskById(subtask.get().getId());
                    sendCode200(exchange, "сабтаск успешно удален");
                }
            } catch (NumberFormatException e) {
                sendCode404(exchange, gson.toJson("введите id подзадачи, которую вы запрашиваете"));
            }

        }
    }
}
