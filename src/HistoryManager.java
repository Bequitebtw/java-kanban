import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    public List<Task> getHistory();
    public Task add(Task task);
}
