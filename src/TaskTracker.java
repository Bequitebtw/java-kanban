import model.Epic;
import model.Subtask;
import model.Task;
import manager.InMemoryTaskManager;


import java.time.Duration;
import java.time.LocalDateTime;

public class TaskTracker {
    private static final Task task = new Task("TASK","DEKSTASK",
            LocalDateTime.of(2024,11,11,11,11), Duration.ofHours(20));
    private static final Epic epic = new Epic("EPIC","DESKEPIC");
    private static final Subtask subtask = new Subtask("SUBTASK","DESKSUB",
            LocalDateTime.of(2024,10,10,10,10),Duration.ofHours(10));

    public static void main(String[] args) {


        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask, epic.getId());
        epic.setId(10);
        inMemoryTaskManager.printAllTypesOfTasks();

    }

}
