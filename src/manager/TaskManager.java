package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TaskManager {
    Task createTask(Task task);

    Epic createEpic(Task epic);

    Subtask createSubtask(Task subtask, int epicId);

    ArrayList<Task> getAllTypesOfTasks();

    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    void clearAllTasks();

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    Optional<Task> getTaskById(int id);

    Optional<Epic> getEpicById(int id);

    Optional<Subtask> getSubtaskById(int id);
    
    void updateTask(Task updateTask);

    void updateEpic(Epic updateEpic);

    void updateSubtask(Subtask updateSubtask);

    ArrayList<Subtask> getEpicSubtasksById(int id);

    List<Task> getHistory();

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Task> getPrioritizedTasks();
}
