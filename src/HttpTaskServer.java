import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import handlers.typeAdapters.DurationAdapter;
import handlers.typeAdapters.LocalDateTimeAdapter;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    private static final int PORT = 8080;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        BaseHttpHandler.setFields(taskManager, gson);
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

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        System.out.println("HTTP-сервер запущен на " + 8080 + " порту!");
    }
}
