import tasks.Task;
import java.util.List;

public interface HistoryManager {
    public void remove(int id);

    public List<Task> getHistory();

    public void add(Task task);
}
