import TypeTokens.EpicListTypeToken;
import TypeTokens.SubtaskListTypeToken;
import com.google.gson.Gson;
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


public class HttpTaskServerEpicsTest {
    private final TaskManager manager = Managers.getDefault();
    private final HttpTaskServer taskServer = new HttpTaskServer(manager);
    private final Gson gson = taskServer.getGson();
    private Epic epic1 = new Epic("EPIC1", "DESKEPIC1");
    private Epic epic2 = new Epic("EPIC2", "DESKEPIC2");
    private Epic epic3 = new Epic("EPIC3", "DESKEPIC3");

    private Subtask subtask1 = new Subtask("SUBTASK1", "DESKSUB",
            LocalDateTime.of(2024, 10, 10, 10, 10), Duration.ofHours(10));
    private Subtask subtask2 = new Subtask("SUBTASK2", "DESKSUB",
            LocalDateTime.of(2024, 10, 10, 10, 10), Duration.ofHours(10));
    private Subtask subtask3 = new Subtask("SUBTASK3", "DESKSUB",
            LocalDateTime.of(2024, 10, 10, 10, 10), Duration.ofHours(10));


    public HttpTaskServerEpicsTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.clearAllTasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
        manager.clearAllTasks();
    }

    @Test
    public void addEpicTest() throws IOException, InterruptedException {
        String taskJson = gson.toJson(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> tasksFromManager = manager.getEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("EPIC1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void deleteEpicByIdTest() throws IOException, InterruptedException {
        manager.createEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertEquals(0, manager.getEpics().size(), "Некорректное количество задач");

        assertTrue(manager.getEpicById(1).isEmpty());
    }

    @Test
    public void updateEpicTest() throws IOException, InterruptedException {
        //cоздаем таск
        manager.createEpic(epic1);
        //меняем статус
        epic1.setStatus(Status.DONE);
        String taskJson = gson.toJson(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        //отправляем запрос на изменение
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = manager.getEpics();
        //проверяем что размер не поменялся
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        //проверяем изменился ли статус у задачи
        assertEquals(Status.DONE, epicsFromManager.getFirst().getStatus(), "Некорректный Cтатус");
    }

    @Test
    public void getEpicsTest() throws IOException, InterruptedException {
        manager.createTask(epic1);
        manager.createTask(epic2);
        manager.createTask(epic3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        //отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //конвертируем таски в массив
        List<Epic> expectedEpics = gson.fromJson(response.body(), new EpicListTypeToken().getType());

        assertEquals(200, response.statusCode());
        assertEquals(expectedEpics, manager.getEpics());
    }

    @Test
    public void getEpicByIdTest() throws IOException, InterruptedException {
        manager.createEpic(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic requestEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals(200, response.statusCode());

        assertEquals(requestEpic, manager.getEpicById(1).get());
    }

    @Test
    public void getEpicSubtasksById() throws IOException, InterruptedException {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, epic1.getId());
        manager.createSubtask(subtask2, epic1.getId());
        manager.createSubtask(subtask3, epic1.getId());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> expectedSubtasks = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());
        assertEquals(200, response.statusCode());
        assertEquals(expectedSubtasks, manager.getEpicSubtasksById(1));
    }
}