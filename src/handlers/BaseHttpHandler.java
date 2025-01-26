package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/* Пришлось сделать 2 конвертера Duration и LocalDateTime так как с одним не получалось добавить либо таски
и сабтаски, либо эпики, поэтому Gson объекта 2. Не до конца понимаю логику их работы, потому что метод get в итоге
работает с любым объектом, а post, только с определенными (к примеру получить все типы объектов получилось через один
Gson в хэндлере истории, а отправить с клиента получается только с определенного Gson. Надеюсь понятно объяснил.
Если сможете что нибудь подсказать буду благодарен, очень мало информации по этому поводу, также в пачке мне никто
не ответил.
*/
public class BaseHttpHandler {
    protected static Gson gson;
    protected static Gson epicGson;
    protected final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected static TaskManager taskManager;

    public static void setFields(TaskManager taskManager, Gson gson, Gson epicGson) {
        BaseHttpHandler.taskManager = taskManager;
        BaseHttpHandler.gson = gson;
        BaseHttpHandler.epicGson = epicGson;
    }

    protected void sendCode200(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendCode201(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(201, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendCode404(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendCode406(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}