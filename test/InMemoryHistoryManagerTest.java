import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManagerTest {

    InMemoryHistoryManager inMemoryHistoryManager;
    Task task;
    Epic epic;
    Subtask subtask;
    @BeforeEach
    public void createInMemoryHistoryManager(){
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task = new Task("1T","2T");
        epic = new Epic("1E","2E");
        subtask = new Subtask("1S","2S");
    }

    @Test
    public void getHistoryTest(){
        List<Task> historyTask = new ArrayList<>();

        historyTask.add(task);
        historyTask.add(epic);
        historyTask.add(subtask);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        System.out.println(historyTask);
        Assertions.assertEquals(historyTask,inMemoryHistoryManager.getHistory());
    }

    //В общем нет смысла этой проверки так как метод просто добавляет через такой же метод add
    @Test
    public void addTest(){
        List<Task> historyTask = new ArrayList<>();
        historyTask.add(task);
        historyTask.add(epic);
        historyTask.add(subtask);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        Assertions.assertEquals(historyTask,inMemoryHistoryManager.getHistory());
    }
}
