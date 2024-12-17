import tasks.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("OutputFile.txt"));
        Task task1 = new Task("TASK1", "DESK1TASK");//1
        Epic epic1 = new Epic("EPIC1", "DESK1EPIC");//2
        Subtask subtask1 = new Subtask("SUBTASK1", "DESK1SUBTASK");//3
        Subtask subtask2 = new Subtask("SUBTASK2", "DESK2SUBTASK");//4
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createSubtask(subtask1, epic1.getId());
        fileBackedTaskManager.createSubtask(subtask2, epic1.getId());


    }

    // При выбрасывании своего исключения приходится менять сигнатуры методов
    public void save() /*throws ManagerSaveException */ {
        try (Writer fileWriter = new FileWriter(file)) {
            /* сортирую массив по id так как если этого не делать, таски будут сохраняться также как в файле,
               и может случиится ситуация при создании сабтаска без эпика
            */
            ArrayList<Task> arr = super.getAllTypesOfTasks();
            Comparator<Task> sorted = Comparator.comparing(Task::getId);
            arr.sort(sorted);
            for (Task task : arr) {
                fileWriter.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    // немного не понял как связать эти 2 метода и почему метод loadFromFile должен возвращать FileBackedTaskManager
    private Task fromString(String value) throws IOException {
        String[] arr = value.split(",");
        if (arr[1].trim().equals("SUBTASK")) {
            Subtask subtask = new Subtask(arr[2], arr[4]);
            subtask.setId(Integer.parseInt(arr[0]));
            subtask.setEpicId(Integer.parseInt(arr[5]));
            if (arr[3].trim().equals("DONE")) {
                subtask.setStatus(Status.DONE);
            } else if (arr[3].trim().equals("NEW")) {
                subtask.setStatus(Status.NEW);
            } else if (arr[3].trim().equals("IN_PROGRESS")) {
                subtask.setStatus(Status.IN_PROGRESS);
            }
            return subtask;
        }
        if (arr[1].trim().equals("EPIC")) {
            Epic epic = new Epic(arr[2], arr[4]);
            epic.setId(Integer.parseInt(arr[0]));
            if (arr[3].trim().equals("DONE")) {
                epic.setStatus(Status.DONE);
            } else if (arr[3].trim().equals("NEW")) {
                epic.setStatus(Status.NEW);
            } else if (arr[3].trim().equals("IN_PROGRESS")) {
                epic.setStatus(Status.IN_PROGRESS);
            }
            return epic;
        } else {
            Task task = new Task(arr[2], arr[4]);
            task.setId(Integer.parseInt(arr[0]));
            if (arr[3].trim().equals("DONE")) {
                task.setStatus(Status.DONE);
            } else if (arr[3].trim().equals("NEW")) {
                task.setStatus(Status.NEW);
            } else if (arr[3].trim().equals("IN_PROGRESS")) {
                task.setStatus(Status.IN_PROGRESS);
            }
            return task;
        }
    }

    public FileBackedTaskManager loadFromFile(File loadFile) throws IOException, ArrayIndexOutOfBoundsException {

        try {
            FileReader reader = new FileReader(loadFile);
            BufferedReader br = new BufferedReader(reader);

            while (br.ready()) {
                Task task = (fromString(br.readLine()));
                if (task.getClass().equals(Task.class)) {
                    createTask(task);
                } else if (task.getClass().equals(Epic.class)) {
                    createEpic((Epic) task);
                } else if (task.getClass().equals(Subtask.class)) {
                    createSubtask((Subtask) task, ((Subtask) task).getEpicId());
                }

            }
            return new FileBackedTaskManager(file);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Пустой файл или строка");
        } catch (IOException e) {
            System.out.println("Нет такого файла");
        }
        return null;
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
