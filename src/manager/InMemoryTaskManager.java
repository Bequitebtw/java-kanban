package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    private final TreeSet<Task> tasksStartTime;
    private int idCounter = 1;
    private final InMemoryHistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        tasksStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public Task createTask(Task task) {
        if (task.getClass().equals(Task.class) && !tasks.containsValue(task)) {
            if (task.getStartTime() != null && task.getDuration() != null) {
                // проверка не пересекаются ли таски
                if (notIntersectCheck(task)) {
                    task.setId(idCounter);
                    tasks.put(idCounter, task);
                    tasksStartTime.add(task);
                    idCounter++;
                } else {
                    System.out.println("Объект пересекается с другим по времени выполнения");
                    return null;
                }
            }
        } else {
            System.out.println("объект уже был добавлен");
            return null;
        }
        return task;
    }

    @Override
    public Epic createEpic(Task epic) {
        if (epic.getClass().equals(Epic.class) && !epics.containsValue(epic)) {
            epic.setId(idCounter);
            epics.put(idCounter, (Epic) epic);
            idCounter++;
        } else {
            System.out.println("не тот объект или он уже был добавлен");
            return null;
        }
        return (Epic) epic;
    }

    @Override
    public Subtask createSubtask(Task subtask, int epicId) {
        if (subtask.getClass().equals(Subtask.class) && epics.containsKey(epicId)) {
            Subtask subtask1 = (Subtask) subtask;
            if (subtask1.getStartTime() != null && subtask1.getDuration() != null) {
                if (notIntersectCheck(subtask)) {
                    subtask1.setEpicId(epicId);
                    subtask.setId(idCounter);
                    subtasks.put(idCounter, subtask1);
                    epics.get(epicId).getSubtasks().add(idCounter);
                    changeEpicStartAndEndTime(epics.get(epicId));
                    tasksStartTime.add(subtask);
                    checkSubtasksStatus(epicId);
                    idCounter++;
                } else {
                    return null;
                }
            }
        } else {
            System.out.println("не тот объект или нет такого epicId");
            return null;
        }
        return (Subtask) subtask;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return tasksStartTime.stream().toList();
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
        tasks.forEach((key, value) -> System.out.println(value));
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
            tasksStartTime.remove(tasks.get(id));
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
            tasksStartTime.remove(subtasks.get(id));
            subtasks.remove(id);
            inMemoryHistoryManager.remove(id);
            checkSubtasksStatus(epicId);
            changeEpicStartAndEndTime(epics.get(epicId));
            return;
        }
        System.out.println("Нет такого сабтаска" + id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Integer subtaskId : epics.get(id).getSubtasks()) {
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
        epics.values().forEach(Epic::clearSubtasksId);
        subtasks.clear();
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        if (tasks.containsKey(id)) {
            inMemoryHistoryManager.add(tasks.get(id));
            return Optional.of(tasks.get(id));
        } else {
            System.out.println("Нет такого таска");
            return Optional.empty();
        }
    }

    //Почему Optional ставится только для Task и Subtask
    @Override
    public Optional<Epic> getEpicById(int id) {
        if (epics.containsKey(id)) {
            inMemoryHistoryManager.add(epics.get(id));
            return Optional.of(epics.get(id));
        } else {
            System.out.println("Нет такого эпика");
            return Optional.empty();
        }
    }

    @Override
    public Optional<Subtask> getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            inMemoryHistoryManager.add(subtasks.get(id));
            return Optional.of(subtasks.get(id));
        } else {
            System.out.println("Нет такого сабтаска");
            return Optional.empty();
        }
    }

    @Override
    public Task updateTask(Task updateTask) {
        if (tasks.get(updateTask.getId()) == null) {
            System.out.println("Нет айди у таска");
        } else if (tasks.containsKey(updateTask.getId())) {
            //Сохраняем таск который пытаются изменить
            Task oldTask = tasks.get(updateTask.getId());
            Task testTask = new Task("ТЕСТ", "ТАСК");
            setNewFields(testTask, updateTask.getName(), updateTask.getDescription(), updateTask.getStatus(),
                    updateTask.getStartTime(), updateTask.getDuration());
            //Удаляем таск который пытаются изменить чтобы впоследствии он не сравнивался сам с собой
            if (!notIntersectCheck(testTask)) {
                //Возвращаем старый таск, так как новые параметры не прошли проверку
                System.out.println("Обновленный таск пересекается с другими");
                return null;
            } else {
                setNewFields(oldTask, updateTask.getName(), updateTask.getDescription(), updateTask.getStatus(),
                        updateTask.getStartTime(), updateTask.getDuration());
                return updateTask;
            }
        } else {
            System.out.println("Нет такого таска");
        }
        return updateTask;
    }

    @Override
    public Epic updateEpic(Epic updateEpic) {
        if (epics.get(updateEpic.getId()) == null) {
            System.out.println("Нет айди у эпика или нет такого id для обновления");
        } else if (epics.containsKey(updateEpic.getId())) {
            Epic oldEpic = epics.get(updateEpic.getId());
            Epic testEpic = new Epic(updateEpic.getName(), updateEpic.getDescription());
            setNewFields(testEpic, updateEpic.getName(), updateEpic.getDescription(), updateEpic.getStatus(),
                    updateEpic.getStartTime(), updateEpic.getDuration());
            if (!notIntersectCheck(testEpic)) {
                System.out.println("Обновленный эпик пересекается с другими");
                return null;
            } else {
                setNewFields(oldEpic, updateEpic.getName(), updateEpic.getDescription(), updateEpic.getStatus(),
                        updateEpic.getStartTime(), updateEpic.getDuration());
                checkEpicStatus(updateEpic.getId());
                changeEpicStartAndEndTime(oldEpic);
                return updateEpic;
            }
        } else {
            System.out.println("Нет такого эпика");
        }
        return updateEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask updateSubtask) {
        if (subtasks.get(updateSubtask.getId()) == null) {
            System.out.println("Нет айди у сабтаска");
        } else if (subtasks.containsKey(updateSubtask.getId())) {
            Subtask oldSubtask = subtasks.get(updateSubtask.getId());
            Task testSubtask = new Subtask(updateSubtask.getName(), updateSubtask.getDescription());
            setNewFields(testSubtask, updateSubtask.getName(), updateSubtask.getDescription(), updateSubtask.getStatus(),
                    updateSubtask.getStartTime(), updateSubtask.getDuration());
            if (!notIntersectCheck(testSubtask)) {
                System.out.println("Обновленный таск пересекается с другими");
                return null;
            } else {
                setNewFields(oldSubtask, updateSubtask.getName(), updateSubtask.getDescription(), updateSubtask.getStatus(),
                        updateSubtask.getStartTime(), updateSubtask.getDuration());
                checkSubtasksStatus(oldSubtask.getEpicId());
                return updateSubtask;
            }
        } else {
            System.out.println("Нет такого сабтаска");
        }
        return updateSubtask;
    }

    // думаю с этим методом код выглядит менее загруженно
    private void setNewFields(Task task, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
        task.setStartTime(startTime);
        task.setDuration(duration);
    }

    //Проверка cтатуса эпика для изменения статусов сабтасков т.к при изменении эпика на DONE, сабтаски должны быть DONE
    private void checkEpicStatus(int id) {
        if (epics.get(id).getStatus() == Status.DONE) {
            getEpicSubtasksById(id).forEach(element -> element.setStatus(Status.DONE));
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

    private void changeEpicStartAndEndTime(Epic epic) {
        if (getEpicSubtasksById(epic.getId()).isEmpty()) {
            epic.setEndTime(LocalDateTime.now());
            epic.setStartTime(LocalDateTime.now());
            epic.setDuration(Duration.ZERO);
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

    private boolean notIntersectCheck(Task task) {
        boolean isNotIntersection = true;
        if (tasksStartTime.isEmpty()) {
            return true;
        }
        for (Task treeTask : tasksStartTime) {
            if (treeTask.getStartTime().isBefore(task.getEndTime()) && treeTask.getEndTime().isAfter(task.getStartTime())
                    || task.getStartTime().isBefore(treeTask.getEndTime()) && task.getEndTime().isAfter(treeTask.getStartTime())
            ) {
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
        epics.get(id).getSubtasks().forEach(element -> epicSubtasks.add(subtasks.get(element)));

        return epicSubtasks;
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    // Добавил методы для получения отдельного вида тасков
    @Override
    public List<Task> getTasks() {
        return tasks.values().stream().toList();
    }

    @Override
    public List<Epic> getEpics() {
        return epics.values().stream().toList();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return subtasks.values().stream().toList();
    }
}
