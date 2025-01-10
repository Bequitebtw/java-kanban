import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManagerTest {

    InMemoryHistoryManager inMemoryHistoryManager;
    InMemoryTaskManager inMemoryTaskManager;
    Task task;
    Epic epic;
    Subtask subtask;


    @BeforeEach
    public void createInMemoryHistoryManagerTest() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        inMemoryTaskManager = new InMemoryTaskManager();
        task = new Task("1T", "2T");
        task.setId(1);
        epic = new Epic("1E", "2E");
        epic.setId(2);
        subtask = new Subtask("1S", "2S");
        subtask.setId(3);
    }

    @Test
    public void checkHistoryOrderTest() {
        //ожидаемый порядок
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(subtask);
        historyTask.add(epic);
        historyTask.add(task);
        //иммитация запроса одинковых тасков из inMemoryTaskManager

        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(task);
        Assertions.assertEquals(historyTask, inMemoryHistoryManager.getHistory());
    }

    //В общем нет смысла этой проверки так как метод просто добавляет через такой же метод add
    @Test
    public void addTest() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task);
        historyTask.add(epic);
        historyTask.add(subtask);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        Assertions.assertEquals(historyTask, inMemoryHistoryManager.getHistory());
    }

    @Test
    public void removeTest() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task);
        historyTask.add(epic);

        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.remove(3);
        Assertions.assertEquals(historyTask, inMemoryHistoryManager.getHistory());

    }
    @Test
    public void duplicateTest() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task);

        Assertions.assertEquals(historyTask, inMemoryHistoryManager.getHistory());
    }

    @Test
    public void removeFromStartTest() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(epic); // id 2
        historyTask.add(subtask); // id 3

        inMemoryHistoryManager.add(task); // id 1
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.remove(1);
        Assertions.assertEquals(historyTask, inMemoryHistoryManager.getHistory());
    }

    @Test
    public void removeFromCenterTest() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task); // 1
        historyTask.add(subtask); // 3
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic); // 2
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.remove(2);

        Assertions.assertEquals(historyTask, inMemoryHistoryManager.getHistory());
    }

    @Test
    public void removeFromEndTest() {
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task); // 1
        historyTask.add(epic); // 2
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask); // 3
        inMemoryHistoryManager.remove(3);

        Assertions.assertEquals(historyTask, inMemoryHistoryManager.getHistory());
    }
}
