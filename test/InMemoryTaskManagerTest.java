import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager inMemoryTaskManager;
    private Task task = new Task("1T","2T");
    private Epic epic = new Epic("1E","2E");
    private Subtask subtask = new Subtask("1S","2S");


    @BeforeEach
    public void createInMemoryTaskManager(){
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    public void createTaskTest(){
        inMemoryTaskManager.createTask(task);

        Assertions.assertEquals(task,inMemoryTaskManager.getTaskById(1));
    }
    @Test
    public void createEpicTest(){
        inMemoryTaskManager.createEpic(epic);

        Assertions.assertEquals(epic,inMemoryTaskManager.getEpicById(1));
    }
    @Test
    public void createSubtaskTest(){
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask,1);

        Assertions.assertEquals(subtask,inMemoryTaskManager.getSubtaskById(2));
    }

    @Test
    public void getTaskByIdTest(){
        inMemoryTaskManager.createTask(task);

        Assertions.assertEquals(task,inMemoryTaskManager.getTaskById(1));
    }
    @Test
    public void getEpicByIdTest(){
        inMemoryTaskManager.createEpic(epic);

        Assertions.assertEquals(epic,inMemoryTaskManager.getEpicById(1));
    }
    @Test
    public void getSubtaskByIdTest(){
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask,1);

        Assertions.assertEquals(subtask,inMemoryTaskManager.getSubtaskById(2));
    }

    @Test
    public void updateTaskTest(){
        inMemoryTaskManager.createTask(task);
        Task task1 = new Task("U1","U2");
        task1.setId(task.getId());
        task1.setStatus(Status.DONE);
        inMemoryTaskManager.updateTask(task1);

        Assertions.assertEquals(task1,inMemoryTaskManager.getTaskById(1));
    }
    @Test
    public void updateEpicTest(){
        inMemoryTaskManager.createEpic(epic);
        Epic epic1 = new Epic("U1","U2");
        epic1.setId(epic.getId());
        epic1.setStatus(Status.DONE);
        inMemoryTaskManager.updateEpic(epic1);

        Assertions.assertEquals(epic1,inMemoryTaskManager.getEpicById(1));
    }
    @Test
    public void updateSubtaskTest(){
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask,1);
        Subtask subtask1 = new Subtask("U1","U2");
        subtask1.setId(subtask.getId());
        subtask1.setEpicId(subtask.getEpicId());
        subtask1.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(subtask1);

        Assertions.assertEquals(subtask1,inMemoryTaskManager.getSubtaskById(2));
    }
    @Test
    public void deleteTaskByIdTest(){
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.deleteTaskById(1);

        Assertions.assertNull(inMemoryTaskManager.getTaskById(1));
    }

    @Test
    public void deleteEpicByIdTest(){
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.deleteEpicById(1);

        Assertions.assertNull(inMemoryTaskManager.getEpicById(1));
    }

    @Test
    public void deleteSubtaskByIdTest(){
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask,1);
        inMemoryTaskManager.deleteSubtaskById(2);

        Assertions.assertNull(inMemoryTaskManager.getSubtaskById(2));
    }

    @Test
    public void clearTasksTest(){
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask,2);
        inMemoryTaskManager.clearAllTasks();

        Assertions.assertEquals(0,inMemoryTaskManager.getAllTypesOfTasks().size());
    }

    @Test
    public void getHistoryTest(){
        List<Task> historyList = new ArrayList<>();
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask,epic.getId());

        historyList.add(task);
        historyList.add(epic);
        historyList.add(subtask);

        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(2);
        inMemoryTaskManager.getSubtaskById(3);
        Assertions.assertEquals(historyList,inMemoryTaskManager.getHistory());
    }

    //Проверки равенства по айди нет, потому что методы equals сравнивают все поля и проверяются в тестах добавления
    @Test
    public void addTaskWithSameIdTest(){
        task.setId(1);
        epic.setId(1);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        Assertions.assertEquals(1,inMemoryTaskManager.getTaskById(1).getId());
        Assertions.assertEquals(2,inMemoryTaskManager.getEpicById(2).getId());
    }
    @Test
    public void addGenerateAndAutomaticIdTest(){
        task.setId(1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createSubtask(subtask,1);
        Assertions.assertEquals(1,inMemoryTaskManager.getEpicById(1).getId());
        Assertions.assertEquals(2,inMemoryTaskManager.getTaskById(2).getId());
        Assertions.assertEquals(3,inMemoryTaskManager.getSubtaskById(3).getId());
    }
    @Test
    public void TaskImmutabilityAfterAddTest(){
        task.setStatus(Status.DONE);
        task.setName("111");
        task.setDescription("222");
        inMemoryTaskManager.createTask(task);
        Assertions.assertEquals(task,inMemoryTaskManager.getTaskById(1));
    }

    //Раньше метод equals сравнивал все поля, теперь только айди, так написано в тз
    @Test
    public void equalityTasksByIdTest(){
        Task task1 = new Task("Task1","desk");
        Task task2 = new Task("Task2","desk");
        task2.setId(1);
        inMemoryTaskManager.createTask(task1);
        Assertions.assertEquals(task1,task2);
    }

    @Test
    public void equalityEpicsByIdTest(){
        Epic epic1 = new Epic("Epic1","desk");
        Epic epic2 = new Epic("Epic2","desk");
        epic2.setId(1);
        inMemoryTaskManager.createEpic(epic1);
        Assertions.assertEquals(epic1,epic2);
    }

    @Test
    public void equalitySubtasksByIdTest(){
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask1","desk");
        Subtask subtask2 = new Subtask("Subtask2","desk");
        subtask2.setId(2);
        inMemoryTaskManager.createSubtask(subtask1,1);
        Assertions.assertEquals(subtask1,subtask2);
    }

    @Test
    public void addingAnEpicToAnEpicTest(){
        inMemoryTaskManager.createEpic(epic);
        Epic epic1 = new Epic("Epic1","desk");
        Assertions.assertNull(inMemoryTaskManager.createSubtask(epic1,1));

    }

    @Test
    public void createSubtaskWithinASubtask(){
        inMemoryTaskManager.createEpic(epic); // id1
        Subtask subtask1 = new Subtask("Subtask1","desk");
        Subtask subtask2 = new Subtask("Subtask2","desk");

        inMemoryTaskManager.createSubtask(subtask1,1); //id2  добавления сабтаска в эпик
        Assertions.assertNull(inMemoryTaskManager.createSubtask(subtask2,2)); // попытка добавить сабтаск в сабтаск
    }

    // Сохранение предыдущей версии и ее данных
    @Test
    public void savingPreviousVersionHistoryTest(){
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask,2);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(2);
        Assertions.assertEquals(task.getName(),inMemoryTaskManager.getHistory().getFirst().getName());
        Assertions.assertEquals(task.getDescription(),inMemoryTaskManager.getHistory().getFirst().getDescription());
        Assertions.assertEquals(task.getStatus(),inMemoryTaskManager.getHistory().getFirst().getStatus());
        Assertions.assertEquals(task.getId(),inMemoryTaskManager.getHistory().getFirst().getId());

    }
    @Test
    public void epicassss(){

        ArrayList<Integer>subtasksId = new ArrayList<>();
        subtasksId.add(4);
        inMemoryTaskManager.createTask(task); //1
        inMemoryTaskManager.createEpic(epic);//2
        Subtask subtask1 = new Subtask("Subtask1","desk");
        Subtask subtask2 = new Subtask("Subtask2","desk");
        inMemoryTaskManager.createSubtask(subtask1,epic.getId());//3
        inMemoryTaskManager.createSubtask(subtask2, epic.getId());//4
        inMemoryTaskManager.deleteSubtaskById(3);
        Assertions.assertEquals(subtasksId,epic.getSubtasks());

    }

}
