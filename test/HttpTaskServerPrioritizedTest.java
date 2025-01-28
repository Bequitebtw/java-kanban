import TypeTokens.TaskListTypeToken;
import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerPrioritizedTest {
    private final TaskManager manager = Managers.getDefault();
    private final HttpTaskServer taskServer = new HttpTaskServer(manager);
    private final Gson gson = taskServer.getGson();

    private Task task1 = new Task("TASK1", "DEKSTASK1",
            LocalDateTime.of(2024, 11, 11, 11, 11), Duration.ofHours(20));
    private Task task2 = new Task("TASK1", "DEKSTASK1",
            LocalDateTime.of(2024, 11, 11, 11, 11), Duration.ofHours(20));
    private Task task3 = new Task("TASK2", "DEKSTASK2",
            LocalDateTime.of(2015, 11, 11, 11, 11), Duration.ofHours(20));
    private Task task4 = new Task("TASK3", "DEKSTASK3",
            LocalDateTime.of(2012, 11, 11, 11, 11), Duration.ofHours(20));

    public HttpTaskServerPrioritizedTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.clearAllTasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void getPrioritizedTest() throws IOException, InterruptedException {
        manager.createTask(task3); //
        manager.createTask(task2); //2 Пробую добавить таск с такой же датой
        manager.createTask(task4); //
        manager.createTask(task1); //

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> PrioritizedFromManager = manager.getPrioritizedTasks();
        List<Task> expectedTasks = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertEquals(3, PrioritizedFromManager.size(), "Некорректное количество задач");
        assertEquals(PrioritizedFromManager, expectedTasks);
    }

}