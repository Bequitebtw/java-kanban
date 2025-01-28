package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        switch (method) {
            case "GET" -> handleGetEpics(exchange, pathParts);
            case "POST" -> handlePostEpics(exchange);
            case "DELETE" -> handleDeleteEpics(exchange, pathParts);
            default -> sendCode404(exchange, "Метод " + method + "недоступен");
        }
    }

    private void handleGetEpics(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            sendCode200(exchange, gson.toJson(taskManager.getEpics()));
        } else if (pathParts.length == 3) {
            try {
                Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(pathParts[2]));
                if (epic.isEmpty()) {
                    sendCode404(exchange, gson.toJson("такого эпика нет"));
                } else {
                    sendCode200(exchange, gson.toJson(epic.get()));
                }
            } catch (NumberFormatException e) {
                sendCode404(exchange, gson.toJson("введите id эпика, который вы запрашиваете"));
            }

        } else if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
            try {
                Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(pathParts[2]));
                if (epic.isEmpty()) {
                    sendCode404(exchange, gson.toJson("такого эпика нет"));
                } else {
                    sendCode200(exchange, gson.toJson(taskManager.getEpicSubtasksById(epic.get().getId())));
                }
            } catch (NumberFormatException e) {
                sendCode404(exchange, gson.toJson("введите id эпика, который вы запрашиваете"));
            }
        }

    }

    private void handlePostEpics(HttpExchange exchange) throws IOException {
        String epic = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Epic newEpic = gson.fromJson(epic, Epic.class);
            if (newEpic.getStartTime() == null || newEpic.getEndTime() == null || newEpic.getDuration() == null) {
                throw new JsonSyntaxException("не переданы поля времени");
            }
            Optional<Epic> optionalEpic = taskManager.getEpicById(newEpic.getId());
            if (optionalEpic.isEmpty()) {
                taskManager.createEpic(newEpic);
                sendCode201(exchange, gson.toJson("новый эпик добавлен"));
            }
            if (optionalEpic.isPresent()) {
                taskManager.updateEpic(newEpic);
                sendCode201(exchange, gson.toJson("эпик обновлен"));
            }
        } catch (JsonSyntaxException e) {
            sendCode404(exchange, gson.toJson("неправильно передан эпик(сорее всего время)"));
        }
    }

    private void handleDeleteEpics(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 3) {
            try {
                Optional<Epic> epic = taskManager.getEpicById(Integer.parseInt(pathParts[2]));
                if (epic.isEmpty()) {
                    sendCode404(exchange, gson.toJson("такого эпика нет"));
                } else {
                    taskManager.deleteEpicById(epic.get().getId());
                    sendCode200(exchange, "эпик успешно удален");
                }
            } catch (NumberFormatException e) {
                sendCode404(exchange, gson.toJson("введите id эпика, который вы запрашиваете"));
            }

        }
    }
}
