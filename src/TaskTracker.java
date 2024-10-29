import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class TaskTracker {
    public static void main(String[] args) {


        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("TASK1","DESK1TASK");
        Task task2 = new Task("TASK2","DESK2TASK");
        Epic epic1 = new Epic("EPIC1","DESK1EPIC");
        Epic epic2 = new Epic("EPIC2","DESK2EPIC");
        Subtask subtask1 = new Subtask("SUBTASK1","DESK1SUBTASK");
        Subtask subtask2 = new Subtask("SUBTASK2","DESK2SUBTASK");
        Subtask subtask3 = new Subtask("SUBTASK3","DESK3SUBTASK");


        //создание
        taskManager.createEpic(epic1); //1
        taskManager.createEpic(epic2); //2
        taskManager.createTask(task1); //3
        taskManager.createTask(task2); //4
        taskManager.createSubtask(subtask1,epic1.getId()); //5
        taskManager.createSubtask(subtask2,epic1.getId()); //6
        taskManager.createSubtask(subtask3, epic2.getId()); //7

        //Таски для обновления

        Task updateTask1 = new Task("UPDATETASK1","NEWNEW1");
        updateTask1.setId(task1.getId());
        Task updateTask2 = new Task("UPDATETASK2","NEWNEW2");
        updateTask2.setId(task2.getId());
        Epic updateEpic1 = new Epic("UPDATEEPIC1","NEWNEW1");
        updateEpic1.setId(epic1.getId());
        Epic updateEpic2 = new Epic("UPDATEEPIC2","NEWNEW2");
        updateEpic2.setId(epic2.getId());
        Subtask updateSubtask1 = new Subtask("UPDATESUBTASK1","NEWNEW1");
        updateSubtask1.setId(subtask1.getId());
        Subtask updateSubtask3 = new Subtask("UPDATESUBTASK3","NEWNEW2=3");
        updateSubtask3.setId(subtask3.getId());


        //Как с вами можно связать, если есть вопросы?

        //Вывод
        taskManager.printAllTypesOfTasks();

        //Обновление
        updateTask1.setStatus(Status.DONE);
        updateSubtask3.setStatus(Status.DONE);
        updateSubtask1.setStatus(Status.DONE);

        //таски
        taskManager.updateTask(updateTask1);
        taskManager.updateTask(updateTask2);
        //сабтаски
        taskManager.updateSubtask(updateSubtask1);
        taskManager.updateSubtask(updateSubtask3);


        //Вывод
        taskManager.printAllTypesOfTasks();
        System.out.println("--------------------");

        //удаление
        // эпиков
        taskManager.deleteEpicById(epic2.getId());
        // таксков
        taskManager.deleteTaskById(task2.getId());
        // сабтасков
        taskManager.deleteSubtaskById(subtask2.getId());

        //Вывод
        taskManager.printAllTypesOfTasks();

    }

}
