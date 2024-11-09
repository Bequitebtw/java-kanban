import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private List<Task>historyList = new ArrayList<>();

    @Override
    public List<Task> getHistory(){
        return historyList;
    }
    @Override
    public Task add(Task task){
        historyList.add(task);
        return task;
    }
}
