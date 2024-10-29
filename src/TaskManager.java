import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int idCounter = 1;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public Task createTask(Task task) {
        task.setId(idCounter);

        if (task.getClass().equals(Task.class)) {
            tasks.put(idCounter, task);
            idCounter++;
        } else {
            System.out.println("не тот объект");
            return null;
        }
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(idCounter);
        epics.put(idCounter, epic);
        idCounter++;
        return epic;
    }

    public Subtask createSubtask(Subtask subtask, int epicId) {
        if (!epics.containsKey(epicId)) {
            System.out.println("Нет такого эпика!");
            return null;
        }
        subtask.setEpicId(epicId);
        subtask.setId(idCounter);
        subtasks.put(idCounter, subtask);
        epics.get(epicId).getSubtasks().add(idCounter);
        checkSubtasksStatus(epicId);
        idCounter++;
        return subtask;
    }

    public ArrayList<Task> getAllTypesOfTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    public void printAllTypesOfTasks() {
        System.out.println(tasks);
        for (Map.Entry<Integer, Epic> epicEntry : epics.entrySet()){
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
            return;
        }
        System.out.println("Нет такого таска");
    }

    public void deleteSubtaskById (int id) {
        if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            ArrayList<Integer> epicSubtasks = epics.get(epicId).getSubtasks();
            for (int x = 0; x < epicSubtasks.size(); x++) {
                if (id == epicSubtasks.get(x)) {
                    epicSubtasks.remove(x);
                }
            }
            subtasks.remove(id);
            checkSubtasksStatus(epicId);
            return;
        }
        System.out.println("Нет такого сабтаска" + id);
    }

    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Integer subtaskId : epics.get(id).getSubtasks()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
            return;
        }
        System.out.println("Нет такого эпика");
    }

    public void clearTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else {
            System.out.println("Нет такого таска");
            return null;
        }
    }

    public Task getEpicById(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            System.out.println("Нет такого эпика");
            return null;
        }
    }

    public Task getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            System.out.println("Нет такого сабтаска");
            return null;
        }
    }


    /* Единственный варинт как я понял это создавать новый объект(c новыми значениями)
        и присаивать ему айдишник объекта который хочешь обновить
    */
    public void updateTask(Task updateTask) {
        if(tasks.get(updateTask.getId()) == null) {
            System.out.println("Нет айди у таска");
            return;
        }
        if(tasks.containsKey(updateTask.getId())){
            Task task = tasks.get(updateTask.getId());
            setNewFields(task,updateTask.getName(), updateTask.getDescription(),updateTask.getStatus());
        } else {
            System.out.println("Нет такого таска");
        }

    }

    public void updateEpic (Epic updateEpic) {
        if(epics.get(updateEpic.getId()) == null) {
            System.out.println("Нет айди у эпика");
            return;
        }
        if (epics.containsKey(updateEpic.getId())) {
            Epic epic = epics.get(updateEpic.getId());
            setNewFields(epic,updateEpic.getName(), updateEpic.getDescription(),updateEpic.getStatus());
            checkEpicStatus(updateEpic.getId());
        } else {
            System.out.println("Нет такого эпика");
        }
    }

    public void updateSubtask (Subtask updateSubtask) {
        if(subtasks.get(updateSubtask.getId()) == null) {
            System.out.println("Нет айди у сабтаска");
            return;
        }
        if (subtasks.containsKey(updateSubtask.getId())) {
            Subtask subtask = subtasks.get(updateSubtask.getId());
            setNewFields(subtask,updateSubtask.getName(), updateSubtask.getDescription(),updateSubtask.getStatus());
            checkSubtasksStatus(subtask.getEpicId());
        } else {
            System.out.println("Нет такого сабтаска");
        }
    }

    // думаю с этим методом код выглядит менее загруженно
    private void setNewFields(Task task,String name,String description,Status status) {
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
    }

    //Проверка cтатуса эпика для изменения статусов сабтасков т.к при изменение эпика на DONE, сабтаски должны быть DONE
    private void checkEpicStatus (int id) {
        if(epics.get(id).getStatus() == Status.DONE) {
           for(Subtask subtask : getEpicSubtasksById(id)){
               subtask.setStatus(Status.DONE);
           }
        }
    }

    //Проверка статусов сабтасков для изменения статуса эпика
    private void checkSubtasksStatus(int epicId) {
        boolean isNew = true;
        boolean isDone = true;

        for (Subtask subtask : getEpicSubtasksById(epicId)) {
            Status subtaskStatus = subtask.getStatus();
            if (subtaskStatus != Status.DONE) {
                isDone = false;
            }
            if (subtaskStatus != Status.NEW) {
                isNew = false;
            }
        }

        if (isDone) {
            epics.get(epicId).setStatus(Status.DONE);
        } else if (isNew) {
            epics.get(epicId).setStatus(Status.NEW);
        } else if (!isDone && !isNew) {
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
