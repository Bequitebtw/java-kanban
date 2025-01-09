import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


abstract class TaskManagerTest<T extends TaskManager> {

    protected abstract T getTaskManager();

    protected Task task = new Task("TASK1", "DESK1TASK");
    protected Task task2 = new Task("TASK2", "DESK2TASK");
    protected Epic epic = new Epic("EPIC1", "DESK1EPIC");
    protected Epic epic2 = new Epic("EPIC2", "DESK2EPIC");
    protected Subtask subtask1 = new Subtask("SUBTASK1", "DESK1SUBTASK");
    protected Subtask subtask2 = new Subtask("SUBTASK2", "DESK2SUBTASK");
    protected Subtask subtask3 = new Subtask("SUBTASK2", "DESK2SUBTASK");


    @Test
    public void createTaskTest() {
        T taskManger = getTaskManager();
        taskManger.createTask(task);

        Assertions.assertEquals(task, taskManger.getTaskById(1).get());
    }

    @Test
    public void createEpicTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic);

        Assertions.assertEquals(epic, taskManger.getEpicById(1));
    }

    @Test
    public void createSubtaskTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic);
        taskManger.createSubtask(subtask1, 1);

        Assertions.assertEquals(subtask1, taskManger.getSubtaskById(2).get());
    }

    @Test
    public void getTaskByIdTest() {
        T taskManger = getTaskManager();
        taskManger.createTask(task);

        Assertions.assertEquals(task, taskManger.getTaskById(1).get());
    }

    @Test
    public void getEpicByIdTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic);

        Assertions.assertEquals(epic, taskManger.getEpicById(1));
    }

    @Test
    public void getSubtaskByIdTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic);
        taskManger.createSubtask(subtask1, 1);

        Assertions.assertEquals(subtask1, taskManger.getSubtaskById(2).get());
    }

    @Test
    public void updateTaskTest() {
        T taskManger = getTaskManager();
        taskManger.createTask(task);
        Task task1 = new Task("U1", "U2");
        task1.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        task1.setDuration(Duration.ofHours(10));
        task1.setId(task.getId());
        task1.setStatus(Status.DONE);
        taskManger.updateTask(task1);

        Assertions.assertEquals(task1, taskManger.getTaskById(1).get());
    }

    @Test
    public void updateEpicTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic);
        Epic epic1 = new Epic("U1", "U2");
        epic1.setId(epic.getId());
        epic1.setStatus(Status.DONE);
        taskManger.updateEpic(epic1);

        Assertions.assertEquals(epic1, taskManger.getEpicById(1));
    }

    @Test
    public void updateSubtaskTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic);
        taskManger.createSubtask(subtask1, 1);
        subtask1.setStartTime(LocalDateTime.of(2000, 1, 1, 1, 1));
        subtask1.setDuration(Duration.ofHours(10));
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setName("NEWSUBTASK");
        taskManger.updateSubtask(subtask1);

        Assertions.assertEquals(subtask1, taskManger.getSubtaskById(2).get());
    }

    @Test
    public void deleteTaskByIdTest() {
        T taskManger = getTaskManager();
        taskManger.createTask(task);
        taskManger.deleteTaskById(1);

        Assertions.assertNull(taskManger.getTaskById(1));
    }

    @Test
    public void deleteEpicByIdTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic);
        taskManger.deleteEpicById(1);

        Assertions.assertNull(taskManger.getEpicById(1));
    }

    @Test
    public void deleteSubtaskByIdTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic);
        taskManger.createSubtask(subtask1, 1);
        taskManger.deleteSubtaskById(2);

        Assertions.assertNull(taskManger.getSubtaskById(2));
    }

    @Test
    public void clearALlTasksTest() {
        T taskManger = getTaskManager();
        taskManger.createTask(task);
        taskManger.createEpic(epic);
        taskManger.createSubtask(subtask1, 2);
        taskManger.clearAllTasks();

        Assertions.assertEquals(0, taskManger.getAllTypesOfTasks().size());
    }

    @Test
    public void getHistoryTest() {
        T taskManger = getTaskManager();
        List<Task> historyList = new ArrayList<>();
        taskManger.createTask(task);
        taskManger.createEpic(epic);
        taskManger.createSubtask(subtask1, epic.getId());

        historyList.add(task);
        historyList.add(epic);
        historyList.add(subtask1);

        taskManger.getTaskById(1);
        taskManger.getEpicById(2);
        taskManger.getSubtaskById(3);
        Assertions.assertEquals(historyList, taskManger.getHistory());
    }

    @Test
    public void clearTasksTest() {
        T taskManger = getTaskManager();
        taskManger.createTask(task); // 1
        taskManger.createTask(task2); // 2
        taskManger.clearTasks();

        Assertions.assertNull(taskManger.getTaskById(1));
        Assertions.assertNull(taskManger.getTaskById(2));
    }

    @Test
    public void clearEpicsTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic); // 1
        taskManger.createEpic(epic2); // 2
        taskManger.clearEpics();

        Assertions.assertNull(taskManger.getEpicById(1));
        Assertions.assertNull(taskManger.getEpicById(2));
    }

    @Test
    public void clearSubtasksTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic);
        taskManger.createSubtask(subtask1, epic.getId()); // 2
        taskManger.createSubtask(subtask2, epic.getId()); // 3
        taskManger.createSubtask(subtask3, epic.getId()); // 4
        taskManger.clearSubtasks();

        Assertions.assertNull(taskManger.getSubtaskById(2));
        Assertions.assertNull(taskManger.getSubtaskById(3));
        Assertions.assertNull(taskManger.getSubtaskById(4));
    }

    @Test
    public void getEpicSubtasksByIdTest() {
        T taskManger = getTaskManager();
        taskManger.createEpic(epic); //1
        taskManger.createSubtask(subtask1, epic.getId());//2
        taskManger.createSubtask(subtask2, epic.getId());//3
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        Assertions.assertEquals(subtasks, taskManger.getEpicSubtasksById(1));

    }

    @Test
    public void getAllTypesOFTasksTest() {
        T taskManger = getTaskManager();
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task2);
        tasks.add(epic);
        tasks.add(epic2);
        tasks.add(subtask1);
        tasks.add(subtask3);

        taskManger.createTask(task);
        taskManger.createTask(task2);
        taskManger.createEpic(epic);
        taskManger.createEpic(epic2);
        taskManger.createSubtask(subtask1, epic.getId());
        taskManger.createSubtask(subtask3, epic.getId());
        Assertions.assertEquals(tasks, taskManger.getAllTypesOfTasks());
    }
}
