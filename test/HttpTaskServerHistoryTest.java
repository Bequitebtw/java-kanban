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

public class HttpTaskServerHistoryTest {
    private final TaskManager manager = Managers.getDefault();
    private final HttpTaskServer taskServer = new HttpTaskServer(manager);
    private final Gson gson = taskServer.getGson();

    private Task task1 = new Task("TASK1", "DEKSTASK1",
            LocalDateTime.of(2024, 11, 11, 11, 11), Duration.ofHours(20));
    private Task task2 = new Task("TASK2", "DEKSTASK2",
            LocalDateTime.of(2024, 10, 11, 11, 11), Duration.ofHours(20));
    private Task task3 = new Task("TASK3", "DEKSTASK3",
            LocalDateTime.of(2024, 9, 11, 11, 11), Duration.ofHours(20));

    public HttpTaskServerHistoryTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.clearAllTasks();
        taskServer.start();
    }

    /* При конвертации возвращается правильная история, но проверить на всех тасках вместе это не получается,
     так как typeToken переводит любую задачу в один из типов epic subtask или task, поэтому тест проводится на одном
     из типов*/
    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        manager.createTask(task1); //1
        manager.createTask(task2); //2
        manager.createTask(task3); //3
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        manager.getTaskById(3);
        manager.getTaskById(2);
        manager.getTaskById(1);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> HistoryFromManager = manager.getHistory();

        List<Task> expectedEpics = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertNotNull(HistoryFromManager, "Задачи не возвращаются");
        assertEquals(3, HistoryFromManager.size(), "Некорректное количество задач");
        assertEquals(HistoryFromManager, expectedEpics);
    }

}