import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
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

class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
    //класс для получения списка сабтасков из json
}

public class HttpTaskServerSubtasksTest {
    private final TaskManager manager = Managers.getDefault();
    private final HttpTaskServer taskServer = new HttpTaskServer(manager);
    private final Gson gson = taskServer.getGson();
    private Epic epic1 = new Epic("EPIC1", "DESKEPIC1");
    private Subtask subtask1 = new Subtask("SUBTASK1", "DESKSUB",
            LocalDateTime.of(2024, 10, 10, 10, 10), Duration.ofHours(10));
    private Subtask subtask2 = new Subtask("SUBTASK2", "DESKSUB",
            LocalDateTime.of(2024, 10, 10, 10, 10), Duration.ofHours(10));
    private Subtask subtask3 = new Subtask("SUBTASK3", "DESKSUB",
            LocalDateTime.of(2024, 10, 10, 10, 10), Duration.ofHours(10));

    public HttpTaskServerSubtasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.clearAllTasks();
        manager.createEpic(epic1);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void addSubtaskTest() throws IOException, InterruptedException {
        subtask1.setEpicId(epic1.getId());
        String SubtaskJson = gson.toJson(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(SubtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getSubtasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("SUBTASK1", subtasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void deleteSubtaskTest() throws IOException, InterruptedException {
        manager.createSubtask(subtask1, 1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertEquals(0, manager.getSubtasks().size(), "Некорректное количество задач");

        assertTrue(manager.getSubtaskById(1).isEmpty());
    }

    @Test
    public void updateSubtaskTest() throws IOException, InterruptedException {
        manager.createSubtask(subtask1, 1);
        //меняем статус
        subtask1.setStatus(Status.DONE);
        String SubtaskJson = gson.toJson(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(SubtaskJson)).build();
        //отправляем запрос на изменение
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Subtask> SubtasksFromManager = manager.getSubtasks();
        //проверяем что размер не поменялся
        assertEquals(1, SubtasksFromManager.size(), "Некорректное количество задач");
        //проверяем изменился ли статус у задачи
        assertEquals(Status.DONE, SubtasksFromManager.getFirst().getStatus(), "Некорректное имя задачи");
    }

    @Test
    public void getSubtasksTest() throws IOException, InterruptedException {
        manager.createSubtask(subtask1, 1);
        manager.createSubtask(subtask2, 1);
        manager.createSubtask(subtask3, 1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        //отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //конвертируем таски в массив
        List<Subtask> expectedTasks = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());

        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, manager.getSubtasks());
    }

    @Test
    public void getSubtaskByIdTest() throws IOException, InterruptedException {
        manager.createSubtask(subtask1, 1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask requestSubtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(200, response.statusCode());
        assertEquals(requestSubtask, manager.getSubtaskById(2).get());

    }
}