import tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private static File file = new File("OutputFile.txt");

    // нет смысла передавать в конструктор файл, в который переносятся данные,
    // так как эта часть и так скрыта от пользователя

    public FileBackedTaskManager() {

    }

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(new File("emptyFile.txt"));
        System.out.println(fileBackedTaskManager1.getAllTypesOfTasks());
    }

    public void save() throws ManagerSaveException {

        /* При переносе данных, если статусы задач написаны неверно(конфликт DONE c IN_PROGRESS или NEW),
           они перезапишутся в соответствии с логикой изменения статуса
        */

        try (Writer fileWriter = new FileWriter(file)) {
            /* сортирую массив по id, так как если этого не делать, таски будут сохраняться также как в файле,
               и может случиится ситуация при создании сабтаска без эпика (можно было отлавливать и добавлять
               определенные таски, при этом пришлось бы 2 раза проходится по файлу, думаю что сортировка по id
               не такая затратная, а также все такси написаны по порядку(если это некорректно,я изменю))
            */
            ArrayList<Task> arr = super.getAllTypesOfTasks();
            Comparator<Task> sorted = Comparator.comparing(Task::getId);
            arr.sort(sorted);
            for (Task task : arr) {
                fileWriter.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(";;;");
        }
    }

    private static Task fromString(String value) {
        String[] TaskObject = value.split(",");
        String id = TaskObject[0];
        String type = TaskObject[1];
        String name = TaskObject[2];
        String status = TaskObject[3];
        String description = TaskObject[4];
        final String DONE = "DONE";
        final String NEW = "NEW";
        final String IN_PROGRESS = "IN_PROGRESS";

        if (type.trim().equals("SUBTASK")) {
            String epicId = TaskObject[5];
            Subtask subtask = new Subtask(name, description);
            subtask.setId(Integer.parseInt(id));
            subtask.setEpicId(Integer.parseInt(epicId));
            if (status.trim().equals(DONE)) {
                subtask.setStatus(Status.DONE);
            } else if (status.trim().equals(NEW)) {
                subtask.setStatus(Status.NEW);
            } else if (status.trim().equals(IN_PROGRESS)) {
                subtask.setStatus(Status.IN_PROGRESS);
            }
            return subtask;
        }
        if (type.trim().equals("EPIC")) {
            Epic epic = new Epic(name, description);
            epic.setId(Integer.parseInt(id));
            if (status.trim().equals(DONE)) {
                epic.setStatus(Status.DONE);
            } else if (status.trim().equals(NEW)) {
                epic.setStatus(Status.NEW);
            } else if (status.trim().equals(IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
            }
            return epic;
        } else {
            Task task = new Task(name, description);
            task.setId(Integer.parseInt(id));
            if (status.trim().equals(DONE)) {
                task.setStatus(Status.DONE);
            } else if (status.trim().equals(NEW)) {
                task.setStatus(Status.NEW);
            } else if (status.trim().equals(IN_PROGRESS)) {
                task.setStatus(Status.IN_PROGRESS);
            }
            return task;
        }
    }

    public static FileBackedTaskManager loadFromFile(File loadFile) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        try {
            FileReader reader = new FileReader(loadFile);
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                Task task = (fromString(br.readLine()));
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
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        super.updateSubtask(updateSubtask);
        save();
    }

}
