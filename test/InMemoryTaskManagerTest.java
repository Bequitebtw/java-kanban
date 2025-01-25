import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.Status;
import model.Task;
import manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    protected InMemoryTaskManager inMemoryTaskManager;

    InMemoryTaskManagerTest() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @BeforeEach
    public void setFieldsNotIntersection() {
        task.setStartTime(LocalDateTime.of(2024, 10, 10, 1, 0));
        task.setDuration(Duration.ofMinutes(10));
        task2.setStartTime(LocalDateTime.of(2024, 10, 10, 2, 0));
        task2.setDuration(Duration.ofMinutes(10));
        epic.setStartTime(LocalDateTime.of(2024, 10, 10, 3, 0));
        epic.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.of(2024, 10, 10, 4, 0));
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask2.setStartTime(LocalDateTime.of(2024, 10, 10, 5, 0));
        subtask2.setDuration(Duration.ofMinutes(10));
        subtask3.setStartTime(LocalDateTime.of(2024, 10, 10, 6, 0));
        subtask3.setDuration(Duration.ofMinutes(10));
    }

    @Override
    protected InMemoryTaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    public void getHistoryTest() {
        List<Task> historyList = new ArrayList<>();
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask1, epic.getId());

        historyList.add(task);
        historyList.add(epic);
        historyList.add(subtask1);

        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(2);
        inMemoryTaskManager.getSubtaskById(3);
        Assertions.assertEquals(historyList, inMemoryTaskManager.getHistory());
    }

    //Проверки равенства по айди нет, потому что методы equals сравнивают все поля и проверяются в тестах добавления
    @Test
    public void addTaskWithSameIdTest() {
        task.setId(1);
        epic.setId(1);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        Assertions.assertEquals(1, inMemoryTaskManager.getTaskById(1).get().getId());
        Assertions.assertEquals(2, inMemoryTaskManager.getEpicById(2).get().getId());
    }

    @Test
    public void addGenerateAndAutomaticIdTest() {
        task.setId(1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createSubtask(subtask1, epic.getId());
        Assertions.assertEquals(1, inMemoryTaskManager.getEpicById(1).get().getId());
        Assertions.assertEquals(2, inMemoryTaskManager.getTaskById(2).get().getId());
        Assertions.assertEquals(3, inMemoryTaskManager.getSubtaskById(3).get().getId());
    }

    @Test
    public void TaskImmutabilityAfterAddTest() {
        task.setStatus(Status.DONE);
        task.setName("111");
        task.setDescription("222");
        inMemoryTaskManager.createTask(task);
        Assertions.assertEquals(task, inMemoryTaskManager.getTaskById(1).get());
    }

    //Раньше метод equals сравнивал все поля, теперь только айди, так написано в тз
    @Test
    public void equalityTasksByIdTest() {
        Task task1 = new Task("Task1", "desk");
        Task task2 = new Task("Task2", "desk");
        task2.setId(1);
        inMemoryTaskManager.createTask(task1);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    public void equalityEpicsByIdTest() {
        Epic epic1 = new Epic("Epic1", "desk");
        Epic epic2 = new Epic("Epic2", "desk");
        epic2.setId(1);
        inMemoryTaskManager.createEpic(epic1);
        Assertions.assertEquals(epic1, epic2);
    }

    @Test
    public void equalitySubtasksByIdTest() {
        inMemoryTaskManager.createEpic(epic);
        subtask2.setId(2);
        inMemoryTaskManager.createSubtask(subtask1, 1);
        Assertions.assertEquals(subtask1, subtask2);
    }

    @Test
    public void addingAnEpicToAnEpicTest() {
        inMemoryTaskManager.createEpic(epic);
        Epic epic1 = new Epic("Epic1", "desk");
        Assertions.assertNull(inMemoryTaskManager.createSubtask(epic1, 1));

    }

    @Test
    public void createSubtaskWithinASubtaskTest() {
        inMemoryTaskManager.createEpic(epic); // id 1
        inMemoryTaskManager.createSubtask(subtask1, 1); //id 2 добавления сабтаска в эпик
        Assertions.assertNull(inMemoryTaskManager.createSubtask(subtask2, 2)); // попытка добавить сабтаск в сабтаск
    }

    // Сохранение предыдущей версии и ее данных
    @Test
    public void savingPreviousVersionHistoryTest() {
        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask1, 2);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getEpicById(2);
        Assertions.assertEquals(task.getName(), inMemoryTaskManager.getHistory().getFirst().getName());
        Assertions.assertEquals(task.getDescription(), inMemoryTaskManager.getHistory().getFirst().getDescription());
        Assertions.assertEquals(task.getStatus(), inMemoryTaskManager.getHistory().getFirst().getStatus());
        Assertions.assertEquals(task.getId(), inMemoryTaskManager.getHistory().getFirst().getId());

    }

    // Массив айдишников сабтасков внутри эпика не удалялся(исправил)
    @Test
    public void checkSubtasksRemoveWhenEpicWasDeletedTest() {
        ArrayList<Integer> subtasksId = new ArrayList<>();
        inMemoryTaskManager.createEpic(epic); // 1
        inMemoryTaskManager.createSubtask(subtask1, epic.getId()); // 2
        inMemoryTaskManager.createSubtask(subtask2, epic.getId()); // 3
        inMemoryTaskManager.createSubtask(subtask3, epic.getId()); // 4
        inMemoryTaskManager.deleteEpicById(1);
        Assertions.assertEquals(subtasksId, epic.getSubtasks());
    }

    @Test
    public void checkSubtaskIdRelevanceWithinTheEpicTest() {
        inMemoryTaskManager.createEpic(epic); // 1
        inMemoryTaskManager.createSubtask(subtask1, epic.getId()); // 2
        inMemoryTaskManager.createSubtask(subtask2, epic.getId()); // 3
        inMemoryTaskManager.createSubtask(subtask3, epic.getId()); // 4
        inMemoryTaskManager.deleteSubtaskById(3);
        ArrayList<Integer> subtasksId = new ArrayList<>();
        subtasksId.add(2);
        subtasksId.add(4);
        Assertions.assertEquals(subtasksId, epic.getSubtasks());
    }

    /* Ничего не меняется так как получение объекта проиходит по айди в хэш мапе, котороое сеттер не изменяет.
     * Если запрашивать таск по его конкретному измененинному айди, ничего не найдет. Как вариант можно дать
     * возможность сеттеру изменять ключи(айдишники) hashmap на такие же, как у объекта.
     */
    @Test
    public void changeTaskIdFieldWithSetterTest() {
        Task task1 = new Task("Task1", "DESKTASK1");
        inMemoryTaskManager.createTask(task1);// id 1
        task1.setId(2); // id 2
        Assertions.assertEquals(inMemoryTaskManager.getTaskById(1).get(), task1);
    }

    @Test
    public void changeTaskFieldWithoutIdSetterTest() {
        Task task1 = new Task("Task1", "DESKTASK1");
        inMemoryTaskManager.createTask(task1);
        task1.setName("Task2");
        task1.setDescription("DESKTASK2");
        task1.setStatus(Status.DONE);
        Assertions.assertEquals(inMemoryTaskManager.getTaskById(1).get().getName(), "Task2");
        Assertions.assertEquals(inMemoryTaskManager.getTaskById(1).get().getDescription(), "DESKTASK2");
        Assertions.assertEquals(inMemoryTaskManager.getTaskById(1).get().getStatus(), Status.DONE);
    }

    @Test
    public void checkEpicStatusNEWTest() {
        // у всех новых тасков статус NEW
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask1, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2, epic.getId());
        inMemoryTaskManager.createSubtask(subtask3, epic.getId());
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkEpicStatusDONETest() {
        inMemoryTaskManager.createEpic(epic);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);
        inMemoryTaskManager.createSubtask(subtask1, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2, epic.getId());
        inMemoryTaskManager.createSubtask(subtask3, epic.getId());
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void checkEpicStatusNEWandDONETest() {
        inMemoryTaskManager.createEpic(epic);
        subtask1.setStatus(Status.DONE);
        inMemoryTaskManager.createSubtask(subtask1, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2, epic.getId());
        inMemoryTaskManager.createSubtask(subtask3, epic.getId());
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void checkEpicStatusINPROGRESSTest() {
        inMemoryTaskManager.createEpic(epic);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);
        inMemoryTaskManager.createSubtask(subtask1, epic.getId());
        inMemoryTaskManager.createSubtask(subtask2, epic.getId());
        inMemoryTaskManager.createSubtask(subtask3, epic.getId());

        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void intersectionAndPrioritizedTest(){
        task.setStartTime(LocalDateTime.of(2024, 10, 10, 10, 0));
        task.setDuration(Duration.ofHours(2));
        task2.setStartTime(LocalDateTime.of(2024, 10, 10, 11, 0));
        task2.setDuration(Duration.ofHours(10));
        subtask1.setStartTime(LocalDateTime.of(2024, 10, 10, 4, 0));
        subtask1.setDuration(Duration.ofHours(5));
        subtask2.setStartTime(LocalDateTime.of(2024, 10, 10, 12, 0));
        subtask2.setDuration(Duration.ofHours(5));

        inMemoryTaskManager.createTask(task);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask1,epic.getId());
        inMemoryTaskManager.createSubtask(subtask2,epic.getId());
        List<Task>notIntersectionTasks = new ArrayList<>();
        notIntersectionTasks.add(subtask1);
        notIntersectionTasks.add(task);
        notIntersectionTasks.add(subtask2);

        Assertions.assertEquals(notIntersectionTasks,inMemoryTaskManager.getPrioritizedTasks());

    }
}
