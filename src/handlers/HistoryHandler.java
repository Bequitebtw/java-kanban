package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        switch (exchange.getRequestMethod()) {
            case "GET" -> handleGetHistory(exchange, pathParts);
            default -> sendCode404(exchange, "Метод " + method + "недоступен");
        }
    }

    private void handleGetHistory(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            sendCode200(exchange, epicGson.toJson(taskManager.getHistory()));
        } else {
            sendCode404(exchange, gson.toJson("такого пути не существует"));
        }

    }

}
