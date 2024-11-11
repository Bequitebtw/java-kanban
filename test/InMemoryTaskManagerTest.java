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
        inMemoryTaskManager.clearTasks();

        Assertions.assertEquals(0,inMemoryTaskManager.getAllTypesOfTasks().size());
    }

    @Test
    public void getHistoryTest(){
        List<Task> historyList = new ArrayList<>();
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask,2);

        historyList.add(task);
        historyList.add(epic);
        historyList.add(subtask);
        historyList.add(subtask);
        historyList.add(subtask);

        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(2);
        inMemoryTaskManager.getSubtaskById(3);
        inMemoryTaskManager.getSubtaskById(3);
        inMemoryTaskManager.getSubtaskById(3);

        Assertions.assertEquals(historyList,inMemoryTaskManager.getHistory());
    }

    //Проверки равенства по айди нет, потому что методы equals сравнивают все поля и проверяются в тестах добавления
    @Test
    public void addTaskWithSameId(){
        task.setId(1);
        epic.setId(1);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        Assertions.assertEquals(1,inMemoryTaskManager.getTaskById(1).getId());
        Assertions.assertEquals(2,inMemoryTaskManager.getEpicById(2).getId());
    }
    @Test
    public void addGenerateAndAutomaticId(){
        task.setId(1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createSubtask(subtask,1);
        Assertions.assertEquals(1,inMemoryTaskManager.getEpicById(1).getId());
        Assertions.assertEquals(2,inMemoryTaskManager.getTaskById(2).getId());
        Assertions.assertEquals(3,inMemoryTaskManager.getSubtaskById(3).getId());
    }
    @Test
    public void TaskImmutabilityAfterAdd(){
        task.setStatus(Status.DONE);
        task.setName("111");
        task.setDescription("222");
        inMemoryTaskManager.createTask(task);
        Assertions.assertEquals(task,inMemoryTaskManager.getTaskById(1));
    }


}
