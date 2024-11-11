import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final int HISTORY_LIST_SIZE = 10;
    private List<Task>historyList = new ArrayList<>(HISTORY_LIST_SIZE);

    @Override
    public List<Task> getHistory(){
        return historyList;
    }
    @Override
    public Task add(Task task){
        checkHistoryList();
        historyList.add(task);
        return task;
    }
    private void checkHistoryList(){
        if(historyList.size() == HISTORY_LIST_SIZE){
            historyList.removeFirst();
        }
    }
}
