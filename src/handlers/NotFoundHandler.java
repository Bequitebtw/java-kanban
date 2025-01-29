package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

// это если вообще не указан путь, видел так делают в некоторых api
public class NotFoundHandler extends BaseHttpHandler implements HttpHandler {
    private String[] ways = {"Возможные пути", "/tasks", "/epics", "/subtasks", "/history", "/prioritized"};

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String jsonWays = gson.toJson(ways);
        sendCode404(exchange, jsonWays);
    }
}
