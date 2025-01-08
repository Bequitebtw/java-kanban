import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    private TreeSet<Task> taskTreeSet;
    private int idCounter = 1;
    private InMemoryHistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        taskTreeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public Task createTask(Task task) {
        /*Проверка нужна чтобы при возобновлении старых задач с прошлого менеджера, айдишники не обновлялись на новые
        (так проще для понимания и написания тестов)
        */
        if (task.getId() == 0) {
            task.setId(idCounter);
        }


        if (task.getClass().equals(Task.class)) {
            tasks.put(idCounter, task);
            if (task.getStartTime() != null) {
                if (intersectionCheck(task)) {
                    taskTreeSet.add(task);
                }
            }
            idCounter++;
        } else {
            System.out.println("не тот объект");
            return null;
        }
        return task;
    }

    @Override
    public Epic createEpic(Task epic) {
        if (epic.getId() == 0) {
            epic.setId(idCounter);
        }
        if (epic.getClass().equals(Epic.class)) {
            epics.put(idCounter, (Epic) epic);
            idCounter++;
        } else {
            System.out.println("не тот объект");
            return null;
        }
        return (Epic) epic;
    }

    @Override
    public Subtask createSubtask(Task subtask, int epicId) {
        if (subtask.getClass().equals(Subtask.class) && epics.containsKey(epicId)) {
            Subtask subtask1 = (Subtask) subtask;
            subtask1.setEpicId(epicId);
            if (subtask.getId() == 0) {
                subtask.setId(idCounter);
            }
            subtasks.put(idCounter, subtask1);
            epics.get(epicId).getSubtasks().add(idCounter);
            checkEpicStartAndEndTime(epics.get(epicId));
            if (subtask1.getStartTime() != null) {
                taskTreeSet.add(subtask1);
            }
            checkSubtasksStatus(epicId);
            idCounter++;
        } else {
            System.out.println("не тот объект или нет такого epicId");
            return null;
        }
        return (Subtask) subtask;

    }

    public List<Task> getPrioritizedTasks() {
        return taskTreeSet.stream().toList();
    }

    public ArrayList<Task> getAllTypesOfTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    public void printAllTypesOfTasks() {
        System.out.println("TASKS");
        for (Map.Entry<Integer, Task> taskEntry : tasks.entrySet()) {
            System.out.println(taskEntry.getValue());
        }
        System.out.println();
        System.out.println("EPICS");
        for (Map.Entry<Integer, Epic> epicEntry : epics.entrySet()) {
            System.out.println(epicEntry.getValue());
            for (int x : epicEntry.getValue().getSubtasks()) {
                for (Map.Entry<Integer, Subtask> subtaskEntry : subtasks.entrySet()) {
                    if (subtaskEntry.getKey() == x) {
                        System.out.println("   " + subtaskEntry.getValue());
                    }
                }
            }
            System.out.println();
        }

    }

    @Override
    public void deleteTaskById(int id) {
        /* нужна проверка если передается не существующий айдишник для удаления из LinkedList,
         * так как без проверки при одном элементе в LikedList будет удаляться не тот элемент
         */
        if (tasks.containsKey(id)) {
            taskTreeSet.remove(tasks.get(id));
            tasks.remove(id);
            inMemoryHistoryManager.remove(id);
            return;
        }
        System.out.println("Нет такого таска");
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            ArrayList<Integer> epicSubtasks = epics.get(epicId).getSubtasks();
            for (int x = 0; x < epicSubtasks.size(); x++) {
                if (id == epicSubtasks.get(x)) {
                    epicSubtasks.remove(x);
                }
            }
            taskTreeSet.remove(subtasks.get(id));
            subtasks.remove(id);
            inMemoryHistoryManager.remove(id);
            checkSubtasksStatus(epicId);
            checkEpicStartAndEndTime(epics.get(epicId));
            return;
        }
        System.out.println("Нет такого сабтаска" + id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Integer subtaskId : epics.get(id).getSubtasks()) {
                taskTreeSet.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                inMemoryHistoryManager.remove(subtaskId);
            }
            epics.get(id).getSubtasks().clear();
            inMemoryHistoryManager.remove(id);
            epics.remove(id);
            return;
        }
        System.out.println("Нет такого эпика");
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Epic e : epics.values()) {
            e.clearSubtasksId();
        }
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            inMemoryHistoryManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            System.out.println("Нет такого таска");
            return null;
        }
    }

    @Override
    public Task getEpicById(int id) {
        if (epics.containsKey(id)) {
            inMemoryHistoryManager.add(epics.get(id));
            return epics.get(id);
        } else {
            System.out.println("Нет такого эпика");
            return null;
        }
    }

    @Override
    public Task getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            inMemoryHistoryManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            System.out.println("Нет такого сабтаска");
            return null;
        }
    }


    /* Единственный варинт как я понял это создавать новый объект(c новыми значениями)
        и присаивать ему айдишник объекта, который хочешь обновить
    */
    @Override
    public void updateTask(Task updateTask) {
        if (tasks.get(updateTask.getId()) == null) {
            System.out.println("Нет айди у таска");
            return;
        }
        if (tasks.containsKey(updateTask.getId())) {
            Task task = tasks.get(updateTask.getId());
            setNewFields(task, updateTask.getName(), updateTask.getDescription(), updateTask.getStatus());
        } else {
            System.out.println("Нет такого таска");
        }

    }

    @Override
    public void updateEpic(Epic updateEpic) {
        if (epics.get(updateEpic.getId()) == null) {
            System.out.println("Нет айди у эпика");
            return;
        }
        if (epics.containsKey(updateEpic.getId())) {
            Epic epic = epics.get(updateEpic.getId());
            setNewFields(epic, updateEpic.getName(), updateEpic.getDescription(), updateEpic.getStatus());
            checkEpicStatus(updateEpic.getId());
        } else {
            System.out.println("Нет такого эпика");
        }
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        if (subtasks.get(updateSubtask.getId()) == null) {
            System.out.println("Нет айди у сабтаска");
            return;
        }
        if (subtasks.containsKey(updateSubtask.getId())) {
            Subtask subtask = subtasks.get(updateSubtask.getId());
            setNewFields(subtask, updateSubtask.getName(), updateSubtask.getDescription(), updateSubtask.getStatus());
            checkSubtasksStatus(subtask.getEpicId());
        } else {
            System.out.println("Нет такого сабтаска");
        }
    }

    // думаю с этим методом код выглядит менее загруженно
    private void setNewFields(Task task, String name, String description, Status status) {
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
    }

    //Проверка cтатуса эпика для изменения статусов сабтасков т.к при изменении эпика на DONE, сабтаски должны быть DONE
    private void checkEpicStatus(int id) {
        if (epics.get(id).getStatus() == Status.DONE) {
            for (Subtask subtask : getEpicSubtasksById(id)) {
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

    private void checkEpicStartAndEndTime(Epic epic) {
        if (getEpicSubtasksById(epic.getId()).isEmpty()) {
            epic.setEndTime(null);
            epic.setStartTime(null);
            epic.setDuration(null);
            return;
        }
        LocalDateTime startTime = getEpicSubtasksById(epic.getId()).getFirst().getStartTime();
        LocalDateTime endTime = getEpicSubtasksById(epic.getId()).getFirst().getEndTime();
        Duration duration = getEpicSubtasksById(epic.getId()).getFirst().getDuration();
        int per = 0;
        for (Subtask subtask : getEpicSubtasksById(epic.getId())) {
            if (startTime.isAfter(subtask.getStartTime())) {
                startTime = subtask.getStartTime();
            }
            if (endTime.isBefore(subtask.getEndTime())) {
                endTime = subtask.getEndTime();
            }
            if (per != 0) duration = duration.plus(subtask.getDuration());
            per++;
        }
        epic.setEndTime(endTime);
        epic.setStartTime(startTime);
        epic.setDuration(duration);
    }

    private boolean intersectionCheck2(Task task1, Task task2) {
        if (task1.getStartTime().isBefore(task2.getStartTime()) && task1.getEndTime().isAfter(task2.getStartTime())
                || task1.getStartTime().isAfter(task2.getStartTime()) && task1.getEndTime().isBefore(task2.getStartTime())) {
            return false;
        }
        return true;
    }

    private boolean intersectionCheck(Task task) {
        boolean isNotIntersection = true;
        if (taskTreeSet.isEmpty()) {
            return true;
        }

        for (Task treeTask : taskTreeSet) {
            if (treeTask.getStartTime().isBefore(task.getStartTime()) && treeTask.getEndTime().isAfter(task.getStartTime())
                    || treeTask.getStartTime().isAfter(task.getStartTime()) && treeTask.getEndTime().isBefore(task.getStartTime())) {
                isNotIntersection = false;
                break;
            }
        }
        return isNotIntersection;
    }

    @Override
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

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

}
