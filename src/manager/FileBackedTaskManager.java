package manager;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    private static final String DONE = "DONE";
    private static final String NEW = "NEW";
    private static final String IN_PROGRESS = "IN_PROGRESS";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

        Task task = new Task("TASK", "DESK");
        Task task1 = new Task("TASK1", "DESK");
        Epic epic = new Epic("EPIC", "DESK");
        Epic epic1 = new Epic("EPIC1", "DESK");
        Subtask subtask = new Subtask("SUBTASK", "DESK");
        Subtask subtask1 = new Subtask("SUBTASK1", "DESK");
        task.setStartTime(LocalDateTime.of(2024, 10, 10, 10, 10));
        task.setDuration(Duration.ofHours(10));
        task1.setStartTime(LocalDateTime.of(2024, 10, 10, 10, 10));
        task1.setDuration(Duration.ofHours(10));
        subtask.setStartTime(LocalDateTime.of(2024, 10, 10, 10, 10));
        subtask.setDuration(Duration.ofHours(10));
        subtask1.setStartTime(LocalDateTime.of(2024, 10, 10, 10, 10));
        subtask1.setDuration(Duration.ofHours(10));


        //Можно добавить 1 и тот же сабтаск в 1 эпик несколько раз, но нельзя в несколько разных эпиков
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("src/files/inputFile.txt"));

        //выдаст исключение
        fileBackedTaskManager.createEpic(epic); // 1
        fileBackedTaskManager.createEpic(epic1); // 2
        fileBackedTaskManager.createSubtask(subtask, epic.getId()); // 3 -> 1
        fileBackedTaskManager.createSubtask(subtask1, epic1.getId()); // 4 -> 2
        fileBackedTaskManager.createSubtask(subtask1, epic1.getId());
        epic1.setId(10);
        fileBackedTaskManager.printAllTypesOfTasks();
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(new File("src/files/inputFile.txt"));
        fileBackedTaskManager1.printAllTypesOfTasks();

    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            ArrayList<Task> arr = super.getAllTypesOfTasks();
//            Comparator<Task> sorted = Comparator.comparing(Task::getId);
//            arr.sort(sorted);
            int counter = 0;
            for (Task task : arr) {
                if (counter == arr.size() - 1) fileWriter.write(task.toString());
                else fileWriter.write(task.toString() + "\n");
                counter++;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("IOException");
        } catch (NullPointerException e) {
            throw new ManagerSaveException("null fields, fill the fields");
        }
    }

    // Добавил вспомогательный метод, так как было много дублирования кода
    private Task setStatus(Task task, String status) {
        if (status.trim().equals(DONE)) {
            task.setStatus(Status.DONE);
        } else if (status.trim().equals(NEW)) {
            task.setStatus(Status.NEW);
        } else if (status.trim().equals(IN_PROGRESS)) {
            task.setStatus(Status.IN_PROGRESS);
        }
        return task;
    }

    private Task fromString(String value) {
        String[] taskObject = value.split(",");
        String id = taskObject[0];
        String type = taskObject[1];
        String name = taskObject[2];
        String status = taskObject[3];
        String description = taskObject[4];
        String startTime = taskObject[5];
        String duration = taskObject[6];
        String endTime = taskObject[7];
        if (type.trim().equals("SUBTASK")) {
            String epicId = taskObject[8];
            Subtask subtask = new Subtask(name, description);
            subtask.setId(Integer.parseInt(id));
            subtask.setEpicId(Integer.parseInt(epicId));
            subtask.setStartTime(LocalDateTime.parse(startTime, formatter));
            subtask.setDuration(Duration.ofMinutes(Long.parseLong(duration)));
            return (Subtask) setStatus(subtask, status);
        }
        if (type.trim().equals("EPIC")) {
            Epic epic = new Epic(name, description);
            epic.setId(Integer.parseInt(id));
            if (!Objects.equals(startTime, "Время начала не определено")) {
                epic.setStartTime(LocalDateTime.parse(startTime, formatter));
            }
            if (!Objects.equals(duration, "Продолжительность не определена")) {
                epic.setDuration(Duration.ofMinutes(Long.parseLong(duration)));
            }
            if (!Objects.equals(endTime, "Время окончания не определено")) {
                epic.setEndTime(LocalDateTime.parse(startTime, formatter));
            }
            return (Epic) setStatus(epic, status);
        } else {
            Task task = new Task(name, description);
            task.setId(Integer.parseInt(id));
            task.setStartTime(LocalDateTime.parse(startTime, formatter));
            task.setDuration(Duration.ofMinutes(Long.parseLong(duration)));
            return (Task) setStatus(task, status);
        }
    }

    public static FileBackedTaskManager loadFromFile(File loadFile) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(loadFile);
        try {
            FileReader reader = new FileReader(loadFile);
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                Task task = (fileBackedTaskManager.fromString(br.readLine()));
                if (task.getClass().equals(Task.class)) {
                    fileBackedTaskManager.createTask(task);
                } else if (task.getClass().equals(Epic.class)) {
                    fileBackedTaskManager.createEpic((Epic) task);
                } else if (task.getClass().equals(Subtask.class)) {
                    fileBackedTaskManager.createSubtask((Subtask) task, ((Subtask) task).getEpicId());
                }
            }
            return fileBackedTaskManager;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ManagerSaveException("Пустой файл или строка");
        } catch (IOException e) {
            throw new ManagerSaveException("Нет такого файла");
        }
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Task epic) {
        super.createEpic(epic);
        save();
        return (Epic) epic;
    }

    @Override
    public Subtask createSubtask(Task subtask, int epicId) {
        super.createSubtask(subtask, epicId);
        save();
        return (Subtask) subtask;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public Task updateTask(Task updateTask) {
        super.updateTask(updateTask);
        save();
        return updateTask;
    }

    @Override
    public Epic updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
        return updateEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask updateSubtask) {
        super.updateSubtask(updateSubtask);
        save();
        return updateSubtask;
    }

}
