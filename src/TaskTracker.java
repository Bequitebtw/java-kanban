import model.Epic;
import model.Subtask;
import model.Task;
import manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskTracker {
    private static Task task = new Task("TASK", "DEKSTASK",
            LocalDateTime.of(2024, 11, 11, 11, 11), Duration.ofHours(20));
    private static Epic epic = new Epic("EPIC", "DESKEPIC");
    private static Subtask subtask = new Subtask("SUBTASK", "DESKSUB",
            LocalDateTime.of(2024, 10, 10, 10, 10), Duration.ofHours(10));

    public static void main(String[] args) {

    }

}
