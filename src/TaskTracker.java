import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

public class TaskTracker {
    public static void main(String[] args) {

        //Как с вами можно связать, если есть вопросы?
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("TASK1","DESK1TASK");
        Task task2 = new Task("TASK2","DESK2TASK");
        Epic epic1 = new Epic("EPIC1","DESK1EPIC");
        Epic epic2 = new Epic("EPIC2","DESK2EPIC");
        Subtask subtask1 = new Subtask("SUBTASK1","DESK1SUBTASK");
        Subtask subtask2 = new Subtask("SUBTASK2","DESK2SUBTASK");
        Subtask subtask3 = new Subtask("SUBTASK3","DESK3SUBTASK");
        Task task = new Task("asdas","sdasdda");

        //создание
        taskManager.createEpic(epic1); //0
        taskManager.createEpic(epic2); //1
        taskManager.createTask(task1); //2
        taskManager.createTask(task2); //3
        taskManager.createSubtask(subtask1,0); //4
        taskManager.createSubtask(subtask2,0); //5
        taskManager.createSubtask(subtask3, 1); //6

        //Вывод
        taskManager.printAllTypesOfTasks();

        //Обновление
        //таски
        taskManager.updateTask(2,"SUPDATETASK1","TASKUP3",Status.DONE);
        taskManager.updateTask(3,"SUPDATETASK2","TASKUP3", Status.IN_PROGRESS);
        //сабтаски
        taskManager.updateTask(4,"SUPDATETASK3","TASKUP3",Status.DONE);
        taskManager.updateTask(5,"SUPDATETASK3","TASKUP3",Status.DONE);
        //эпики
        taskManager.updateTask(1,"SUPDATETASK3","TASKUP3",Status.DONE);

        System.out.println("--------------------");
        //Вывод
        taskManager.printAllTypesOfTasks();

        //удаление
        // эпиков
        taskManager.deleteEpicById(0);
        // таксков
        taskManager.deleteTaskById(2);
        // сабтасков
        taskManager.deleteSubtaskById(5);

        System.out.println("--------------------");
        taskManager.printAllTypesOfTasks();

    }

}
