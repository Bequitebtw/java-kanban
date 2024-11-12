import org.junit.jupiter.api.Assertions;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class TaskTracker {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();


        Task task1 = new Task("TASK1","DESK1TASK");
        Task task2 = new Task("TASK2","DESK2TASK");
        Epic epic1 = new Epic("EPIC1","DESK1EPIC");
        Epic epic2 = new Epic("EPIC2","DESK2EPIC");
        Subtask subtask1 = new Subtask("SUBTASK1","DESK1SUBTASK");
        Subtask subtask2 = new Subtask("SUBTASK2","DESK2SUBTASK");
        Subtask subtask3 = new Subtask("SUBTASK3","DESK3SUBTASK");

        // создание
        inMemoryTaskManager.createEpic(epic1); //1
        inMemoryTaskManager.createEpic(epic2); //2
        inMemoryTaskManager.createSubtask(subtask1,epic1.getId()); //5
        inMemoryTaskManager.createSubtask(subtask2,epic1.getId()); //6
        inMemoryTaskManager.createSubtask(subtask3, epic2.getId()); //7

        // Запрос тасков
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getSubtaskById(5);
        inMemoryTaskManager.getSubtaskById(5);
        inMemoryTaskManager.getSubtaskById(5);
        inMemoryTaskManager.getSubtaskById(5);
        inMemoryTaskManager.clearSubtasks();
        System.out.println(inMemoryTaskManager.getEpicSubtasksById(1));
        System.out.println(inMemoryTaskManager.getEpicSubtasksById(2));
        inMemoryTaskManager.createSubtask(subtask1,1);

        // Истрория
        System.out.println(inMemoryTaskManager.getHistory().size());
        System.out.println(inMemoryTaskManager.getHistory());


        System.out.println(inMemoryTaskManager.getAllTypesOfTasks());
    }

}
