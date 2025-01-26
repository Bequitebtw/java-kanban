package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/* У меня изначально логика была в том, что таски можно добавлять даже с пересекающимися датами и временем в hash мапы,
 но они не будут добавляться в дерево приоритетов. Вы приняли мою логику, она так и осталась.
 Если нужно переделать напишите.
 */
public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        switch (method) {
            case "GET" -> handleGetHistory(exchange, pathParts);
            default -> sendCode404(exchange, "Метод " + method + "недоступен");
        }
    }

    private void handleGetHistory(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            sendCode200(exchange, epicGson.toJson(taskManager.getPrioritizedTasks()));
        } else {
            sendCode404(exchange, gson.toJson("такого пути не существует"));
        }

    }
}
