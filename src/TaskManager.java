import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    public Task createTask(Task task);
    public Epic createEpic(Epic epic);
    public Subtask createSubtask(Subtask subtask, int epicId);
    public ArrayList<Task> getAllTypesOfTasks();
    public void deleteTaskById(int id);
    public void deleteSubtaskById (int id);
    public void deleteEpicById(int id);
    public void clearTasks();
    public Task getTaskById(int id);
    public Task getEpicById(int id);
    public Task getSubtaskById(int id);
    public void updateTask(Task updateTask);
    public void updateEpic (Epic updateEpic);
    public void updateSubtask (Subtask updateSubtask);
    public ArrayList<Subtask> getEpicSubtasksById(int id);
    public List<Task>getHistory();
}
