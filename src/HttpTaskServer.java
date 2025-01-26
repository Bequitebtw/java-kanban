import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import handlers.typeAdapters.DurationAdapter;
import handlers.typeAdapters.DurationAdapterEpic;
import handlers.typeAdapters.LocalDateTimeAdapter;
import handlers.typeAdapters.LocalDateTimeAdapterEpic;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    private static final int PORT = 8080;

    private final Gson epicGson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterEpic())
            .registerTypeAdapter(Duration.class, new DurationAdapterEpic())
            .create();
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        BaseHttpHandler.setFields(taskManager, gson, epicGson);
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/subtasks", new SubtasksHandler());
        httpServer.createContext("/epics", new EpicsHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.createContext("/", new NotFoundHandler());
    }

    public Gson getGson() {
        return gson;
    }

    public Gson getEpicGson() {
        return epicGson;
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getDefault());
        httpTaskServer.start();
        System.out.println("HTTP-сервер запущен на " + 8080 + " порту!");
    }
}
