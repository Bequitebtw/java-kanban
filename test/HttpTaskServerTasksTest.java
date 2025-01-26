import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import model.Status;
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

class TaskListTypeToken extends TypeToken<List<Task>> {
    //класс для получения списка тасков из json
}

public class HttpTaskServerTasksTest {
    private final TaskManager manager = Managers.getDefault();
    private final HttpTaskServer taskServer = new HttpTaskServer(manager);
    private final Gson gson = taskServer.getGson();
    private Task task1 = new Task("TASK1", "DEKSTASK1",
            LocalDateTime.of(2024, 11, 11, 11, 11), Duration.ofHours(20));
    private Task task2 = new Task("TASK2", "DEKSTASK2",
            LocalDateTime.of(2024, 11, 11, 11, 11), Duration.ofHours(20));
    private Task task3 = new Task("TASK3", "DEKSTASK3",
            LocalDateTime.of(2024, 11, 11, 11, 11), Duration.ofHours(20));

    public HttpTaskServerTasksTest() throws IOException {
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
    public void addTaskTest() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("TASK1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void deleteTaskTest() throws IOException, InterruptedException {
        manager.createTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertEquals(0, manager.getTasks().size(), "Некорректное количество задач");

        assertTrue(manager.getTaskById(1).isEmpty());
    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        //cоздаем таск
        manager.createTask(task1);
        //меняем статус
        task1.setStatus(Status.DONE);
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        //отправляем запрос на изменение
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();
        //проверяем что размер не поменялся
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        //проверяем изменился ли статус у задачи
        assertEquals(Status.DONE, tasksFromManager.getFirst().getStatus(), "Некорректное имя задачи");
    }

    @Test
    public void getTasksTest() throws IOException, InterruptedException {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        //отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //конвертируем таски в массив
        List<Task> expectedTasks = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, manager.getTasks());
    }

    @Test
    public void getTaskByIdTest() throws IOException, InterruptedException {
        manager.createTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task requestTask = gson.fromJson(response.body(), Task.class);
        assertEquals(200, response.statusCode());

        assertEquals(requestTask, manager.getTaskById(1).get());

    }
}