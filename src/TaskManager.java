import java.util.*;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int idCounter;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        idCounter = 0;
    }

    public void createTask(Task task) {
        task.setId(idCounter);

        if (task.getClass().equals(Task.class)) {
            tasks.put(idCounter, task);
            idCounter++;
        } else {
            System.out.println("не тот объект");
        }

    }

    public void createEpic(Epic epic) {
        epic.setId(idCounter);
        epics.put(idCounter, epic);
        idCounter++;
    }

    public void createSubtask(Subtask subtask, int epicId) {
        if (!epics.containsKey(epicId)) {
            System.out.println("Нет такого эпика!");
            return;
        }
        subtask.setEpicId(epicId);
        subtask.setId(idCounter);
        subtasks.put(idCounter, subtask);
        epics.get(epicId).addSubtaskId(idCounter);
        checkSubtasksStatus(epicId);
        idCounter++;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    public void getTasksSOUT() {
        System.out.println(tasks);
        for (Map.Entry<Integer,Epic> epicEntry : epics.entrySet()){
            System.out.println(epicEntry.getValue());
            for (int x : epicEntry.getValue().getSubtasks()) {
                for (Map.Entry<Integer,Subtask> subtaskEntry : subtasks.entrySet()) {
                    if (subtaskEntry.getKey() == x) {
                        System.out.println(subtaskEntry.getValue());
                    }
                }
            }
        }
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            for(Integer subtaskId : epics.get(id).getSubtasks()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            ArrayList<Integer> epicSubtasks = epics.get(epicId).getSubtasks();
            for(int x = 0;x < epicSubtasks.size();x++){
                if(id == epicSubtasks.get(x)) {
                    epicSubtasks.remove(x);
                }
            }
            subtasks.remove(id);
            checkSubtasksStatus(epicId);
        } else {
            System.out.println("Нет такого таска");
        }
    }

    public void clearTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    // Можно было разделить на несколько методов, но пришлось бы проверять что передан нужный класс
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            System.out.println("Нет такого таска");
            return null;
        }
    }

    // Тоже самое как при получении таска
    public void updateTask(int taskId,String name,String description,Status status) {
        if (tasks.containsKey(taskId)) {
            setNewFields(tasks.get(taskId),name,description,status);
        } else if (epics.containsKey(taskId)) {
            setNewFields(epics.get(taskId),name,description,status);
            checkEpicStatus(taskId);
        } else if (subtasks.containsKey(taskId)) {
            setNewFields(subtasks.get(taskId),name,description,status);
            checkSubtasksStatus(subtasks.get(taskId).getEpicId());
        } else {
            System.out.println("Нет такого таска");
        }
    }

    // думаю с этим методом код выглядит менее загруженно
    private void setNewFields(Task task,String name,String description,Status status) {
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
    }

    private void checkEpicStatus(int epicId) {
        if (epics.get(epicId).getStatus().equals(Status.DONE)) {
            for (Subtask subtask : getEpicSubtasksById(epicId)) {
                subtask.setStatus(Status.DONE);
            }
        }
        if (epics.get(epicId).getStatus().equals(Status.IN_PROGRESS)) {
            for (Subtask subtask : getEpicSubtasksById(epicId)) {
                subtask.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    private void checkSubtasksStatus(int epicId) {

        boolean isDone = true;
        boolean inProgress = false;
        for (Subtask subtask : getEpicSubtasksById(epicId)) {
            if (!subtask.getStatus().equals(Status.DONE) ) {
                isDone = false;
            } if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                inProgress = true;
                break;
            }
        }

        if (isDone) {
            epics.get(epicId).setStatus(Status.DONE);
        } else if (inProgress) {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    public ArrayList<Subtask> getEpicSubtasksById(int id) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();

        if (!epics.containsKey(id)) {
            System.out.println("Нет такого эпика");
            return null;
        }
        for (Integer x : epics.get(id).getSubtasks()) {
            epicSubtasks.add(subtasks.get(x));
        }

        return epicSubtasks;
    }

}
