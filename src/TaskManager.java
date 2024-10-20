import java.util.*;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private int idCounter;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        idCounter = 1;
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
        epics.remove(id);
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtasks().values()) {
                if (subtask.getId() == id) {
                    epic.getSubtasks().remove(id);
                    epic.checkStatus();
                    return;
                }
            }
        }
    }

    public void clearTasks() {
        tasks.clear();
        epics.clear();
    }

    public void createTask(TaskType type, String name, String description) {
        switch (type) {
            case TASK -> tasks.put(idCounter, new Task(name, description, idCounter));
            case EPIC -> epics.put(idCounter, new Epic(name, description, idCounter));
            default -> System.out.println("Нет такого варианта");
        }
        idCounter++;
    }

    public void getTaskById(int id) {  // Отдельно выводит все обычные такски, эпики вместе с подзадачами, подзадачи
        for (Map.Entry<Integer, Epic> taskEntry : epics.entrySet()) {
            if (taskEntry.getKey().equals(id)) {
                System.out.println(taskEntry.getValue());
                return;
            }
        }

        for (Map.Entry<Integer, Task> taskEntry : tasks.entrySet()) {
            if (taskEntry.getKey().equals(id)) {
                System.out.println(taskEntry.getValue());
                return;
            }
        }
        for (Map.Entry<Integer, Epic> taskEntry : epics.entrySet()) {
            for (Map.Entry<Integer, Subtask> subtaskEntry : taskEntry.getValue().getSubtasks().entrySet()) {
                if (subtaskEntry.getKey().equals(id)) {
                    System.out.println(subtaskEntry.getValue());
                    return;
                }

            }
        }
    }

    public void createSubtask(int epicId, String name, String description) {
        try {
            epics.get(epicId).addSubtask(name, description, idCounter);
        } catch (NullPointerException e) {
            System.out.println("Нет такого эпика: " + epicId);
        }
        idCounter++;
    }

    public void getTasks() {
        for (Map.Entry<Integer, Task> taskEntry : tasks.entrySet()) {
            System.out.println(taskEntry.getValue());
        }

        for (Map.Entry<Integer, Epic> taskEntry : epics.entrySet()) {
            System.out.println(taskEntry.getValue());
        }
    }

    public void updateTask(int id, String name, String description, Status status) {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtasks().values()) {
                if (subtask.getId() == id) {
                    subtask.setStatus(status);
                    subtask.setDescription(description);
                    subtask.setName(name);
                    epic.checkStatus();
                    return;
                }
            }
        }

        for (Map.Entry<Integer, Task> taskEntry : tasks.entrySet()) {
            if (taskEntry.getKey().equals(id)) {
                taskEntry.getValue().setName(name);
                taskEntry.getValue().setDescription(description);
                taskEntry.getValue().setStatus(status);

            }
        }

        for (Map.Entry<Integer, Epic> epicEntry : epics.entrySet()) {
            if (epicEntry.getKey().equals(id)) {
                epicEntry.getValue().setName(name);
                epicEntry.getValue().setDescription(description);
                epicEntry.getValue().setStatus(status);
                epicEntry.getValue().checkStatus();
            }
        }
    }

}
