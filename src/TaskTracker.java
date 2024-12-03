import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class TaskTracker {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();


        Task task1 = new Task("TASK1", "DESK1TASK");
        Task task2 = new Task("TASK2", "DESK2TASK");
        Epic epic1 = new Epic("EPIC1", "DESK1EPIC");
        Epic epic2 = new Epic("EPIC2", "DESK2EPIC");
        Subtask subtask1 = new Subtask("SUBTASK1", "DESK1SUBTASK");
        Subtask subtask2 = new Subtask("SUBTASK2", "DESK2SUBTASK");
        Subtask subtask3 = new Subtask("SUBTASK3", "DESK3SUBTASK");

        // создание
        inMemoryTaskManager.createTask(task1); //1
        inMemoryTaskManager.createTask(task2); //2
        inMemoryTaskManager.createEpic(epic1); //3
        inMemoryTaskManager.createEpic(epic2); //4
        inMemoryTaskManager.createSubtask(subtask1, epic1.getId()); //5
        inMemoryTaskManager.createSubtask(subtask2, epic1.getId()); //6
        inMemoryTaskManager.createSubtask(subtask3, epic1.getId()); //7


        // Проверка на изменение порядка одинаковых элементов в истории
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getEpicById(4);
        inMemoryTaskManager.getSubtaskById(6);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getSubtaskById(5);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getSubtaskById(7);

        inMemoryTaskManager.deleteTaskById(1);
        inMemoryTaskManager.deleteEpicById(3);
        System.out.println(inMemoryTaskManager.getHistory());
    }

}
