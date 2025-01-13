import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;

public class ManagersTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @Test
    public void getDefaultShouldReturnInMemoryTaskManager() {
        Assertions.assertEquals(Managers.getDefault().getClass(), InMemoryTaskManager.class);
    }

    @Test
    public void getDefaultHistoryShouldReturnInMemoryHistoryManager() {
        Assertions.assertEquals(Managers.getDefaultHistory().getClass(), InMemoryHistoryManager.class);
    }
}
