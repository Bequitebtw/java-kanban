import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import manager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


abstract class TaskManagerTest<T extends TaskManager> {

    protected abstract T getTaskManager();

    T taskManager;
    protected Task task = new Task("TASK1", "DESK1TASK");
    protected Task task2 = new Task("TASK2", "DESK2TASK");
    protected Epic epic = new Epic("EPIC1", "DESK1EPIC");
    protected Epic epic2 = new Epic("EPIC2", "DESK2EPIC");
    protected Subtask subtask1 = new Subtask("SUBTASK1", "DESK1SUBTASK");
    protected Subtask subtask2 = new Subtask("SUBTASK2", "DESK2SUBTASK");
    protected Subtask subtask3 = new Subtask("SUBTASK2", "DESK2SUBTASK");

    @BeforeEach
    public void setTaskManager() {
        taskManager = getTaskManager();
    }

    @Test
    public void createTaskTest() {
        taskManager.createTask(task);

        Assertions.assertEquals(task, taskManager.getTaskById(1).get());
    }

    @Test
    public void createEpicTest() {
        taskManager.createEpic(epic);

        Assertions.assertEquals(epic, taskManager.getEpicById(1).get());
    }

    @Test
    public void createSubtaskTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1, 1);

        Assertions.assertEquals(subtask1, taskManager.getSubtaskById(2).get());
    }

    @Test
    public void getTaskByIdTest() {
        taskManager.createTask(task);

        Assertions.assertEquals(task, taskManager.getTaskById(1).get());
    }

    @Test
    public void getEpicByIdTest() {
        taskManager.createEpic(epic);

        Assertions.assertEquals(epic, taskManager.getEpicById(1).get());
    }

    @Test
    public void getSubtaskByIdTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1, 1);

        Assertions.assertEquals(subtask1, taskManager.getSubtaskById(2).get());
    }

    @Test
    public void updateTaskTest() {
        taskManager.createTask(task);
        Task task1 = new Task("U1", "U2");
        task1.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        task1.setDuration(Duration.ofHours(10));
        task1.setId(task.getId());
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);

        Assertions.assertEquals(task1, taskManager.getTaskById(1).get());
    }

    @Test
    public void updateEpicTest() {
        taskManager.createEpic(epic);
        Epic epic1 = new Epic("U1", "U2");
        epic1.setId(epic.getId());
        epic1.setStatus(Status.DONE);
        taskManager.updateEpic(epic1);

        Assertions.assertEquals(epic1, taskManager.getEpicById(1).get());
    }

    @Test
    public void updateSubtaskTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1, 1);
        subtask1.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        subtask1.setDuration(Duration.ofHours(10));
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setName("NEWSUBTASK");
        taskManager.updateSubtask(subtask1);

        Assertions.assertEquals(subtask1, taskManager.getSubtaskById(2).get());
    }

    @Test
    public void deleteTaskByIdTest() {
        taskManager.createTask(task);
        taskManager.deleteTaskById(1);

        Assertions.assertTrue(taskManager.getTaskById(1).isEmpty());
    }

    @Test
    public void deleteEpicByIdTest() {
        taskManager.createEpic(epic);
        taskManager.deleteEpicById(1);

        Assertions.assertTrue(taskManager.getEpicById(1).isEmpty());
    }

    @Test
    public void deleteSubtaskByIdTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1, 1);
        taskManager.deleteSubtaskById(2);

        Assertions.assertTrue(taskManager.getSubtaskById(2).isEmpty());
    }

    @Test
    public void clearALlTasksTest() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1, 2);
        taskManager.clearAllTasks();

        Assertions.assertEquals(0, taskManager.getAllTypesOfTasks().size());
    }

    @Test
    public void getHistoryTest() {
        List<Task> historyList = new ArrayList<>();
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1, epic.getId());

        historyList.add(task);
        historyList.add(epic);
        historyList.add(subtask1);

        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        Assertions.assertEquals(historyList, taskManager.getHistory());
    }

    @Test
    public void clearTasksTest() {
        taskManager.createTask(task); // 1
        taskManager.createTask(task2); // 2
        taskManager.clearTasks();

        Assertions.assertTrue(taskManager.getTaskById(1).isEmpty());
        Assertions.assertTrue(taskManager.getTaskById(2).isEmpty());
    }

    @Test
    public void clearEpicsTest() {
        taskManager.createEpic(epic); // 1
        taskManager.createEpic(epic2); // 2
        taskManager.clearEpics();

        Assertions.assertTrue(taskManager.getEpicById(1).isEmpty());
        Assertions.assertTrue(taskManager.getEpicById(2).isEmpty());
    }

    @Test
    public void clearSubtasksTest() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1, epic.getId()); // 2
        taskManager.createSubtask(subtask2, epic.getId()); // 3
        taskManager.createSubtask(subtask3, epic.getId()); // 4
        taskManager.clearSubtasks();

        Assertions.assertTrue(taskManager.getSubtaskById(2).isEmpty());
        Assertions.assertTrue(taskManager.getSubtaskById(3).isEmpty());
        Assertions.assertTrue(taskManager.getSubtaskById(4).isEmpty());
    }

    @Test
    public void getEpicSubtasksByIdTest() {
        taskManager.createEpic(epic); //1
        taskManager.createSubtask(subtask1, epic.getId());//2
        taskManager.createSubtask(subtask2, epic.getId());//3
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        Assertions.assertEquals(subtasks, taskManager.getEpicSubtasksById(1));

    }

    @Test
    public void getAllTypesOFTasksTest() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task2);
        tasks.add(epic);
        tasks.add(epic2);
        tasks.add(subtask1);
        tasks.add(subtask3);

        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1, epic.getId());
        taskManager.createSubtask(subtask3, epic.getId());
        Assertions.assertEquals(tasks, taskManager.getAllTypesOfTasks());
    }
}
