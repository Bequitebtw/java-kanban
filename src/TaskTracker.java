
public class TaskTracker {
    public static void main(String[] args) {
        // Я так и не разобрался куда вам можно писать если есть вопросы

        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("EPIC1","DESK1EPIC");
//        Epic epic2 = new Epic("EPIC2","DESK2EPIC");
//        Epic epic3 = new Epic("EPIC3","DESK3EPIC");
//        Task task1 = new Task("TASK1","DESK1TASK");
//        Task task2 = new Task("TASK2","DESK2TASK");
//        Task task3 = new Task("TASK3","DESK3TASK");
        Subtask subtask1 = new Subtask("SUBTASK1","DESK1SUBTASK");
        Subtask subtask2 = new Subtask("SUBTASK2","DESK2SUBTASK");
        Subtask subtask3 = new Subtask("SUBTASK3","DESK3SUBTASK");
//
//        taskManager.createTask(task1);
//        taskManager.createTask(task2);
//        taskManager.createTask(task3);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1,0);
        taskManager.createSubtask(subtask2,0);
        taskManager.createSubtask(subtask3,0);

//        System.out.println();
//        System.out.println(taskManager.getTasks());

        taskManager.updateTask(0,"UPDATETASK3","TASKUP3",Status.DONE);
        System.out.println(taskManager.getTasks());
    }

}
