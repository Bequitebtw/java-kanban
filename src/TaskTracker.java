import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

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


        //Таски для обновления

        Task task3 = new Task("NEWTASK3","NEWNEW3");
        Task task4 = new Task("NEWTASK4","NEWNEW4");
        Epic epic3 = new Epic("NEWEPIC3","NEWNEW3");
        Epic epic4 = new Epic("NEWEPIC4","NEWNEW4");
        Subtask subtask4 = new Subtask("NEWSUB4","NEWNEW4");
        Subtask subtask5 = new Subtask("NEWSUB5","NEWNEW5");
        task3.setStatus(Status.IN_PROGRESS);
        subtask4.setStatus(Status.IN_PROGRESS);
        subtask5.setStatus(Status.DONE);
        epic3.setStatus(Status.DONE);

        //создание
        taskManager.createEpic(epic1); //0
        taskManager.createEpic(epic2); //1
        taskManager.createTask(task1); //2
        taskManager.createTask(task2); //3
        taskManager.createSubtask(subtask1,0); //4
        taskManager.createSubtask(subtask2,0); //5
        taskManager.createSubtask(subtask3, 1); //6
        taskManager.createSubtask(subtask5, 1); //7


        //Вывод
        taskManager.printAllTypesOfTasks();

        //Обновление

        //таски
        taskManager.updateTask(task3,2);
        taskManager.updateTask(task4,3);
        //сабтаски
        taskManager.updateSubtask(subtask4,4);
        taskManager.updateSubtask(subtask5,7);
        //эпики
        taskManager.updateEpic(epic3,1);

        System.out.println("--------------------");
        //Вывод
        taskManager.printAllTypesOfTasks();

        //удаление
        // эпиков
        taskManager.deleteEpicById(0);
        // таксков
        taskManager.deleteSubtaskById(6);
        // сабтасков
        taskManager.deleteSubtaskById(5);

        System.out.println("--------------------");
        taskManager.printAllTypesOfTasks();

    }

}
