import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;

public class TaskTracker {

    public static void main(String[] args) throws IOException {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("TASK1", "DESK1TASK");
        Task task2 = new Task("TASK2", "DESK2TASK");
        Epic epic1 = new Epic("EPIC1", "DESK1EPIC");
        Epic epic2 = new Epic("EPIC2", "DESK2EPIC");
        Subtask subtask1 = new Subtask("SUBTASK1", "DESK1SUBTASK");
        Subtask subtask2 = new Subtask("SUBTASK2", "DESK2SUBTASK");
        Subtask subtask3 = new Subtask("SUBTASK3", "DESK3SUBTASK");


        // Проверка на изменение порядка одинаковых элементов в истории
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.deleteTaskById(1);
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getSubtaskById(5);
        inMemoryTaskManager.getSubtaskById(7);
        inMemoryTaskManager.getSubtaskById(6);

        System.out.println(inMemoryTaskManager.getHistory());

    }

}
