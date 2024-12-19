import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest {
    private File inputFile = new File("InputFile.txt"); // заполненный тасками
    private File emptyFile = new File("emptyFile.txt"); //пустой


    @Test
    public void uploadEmptyFileTest() {
        Exception exception = assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(emptyFile); // пустой файл
        });

        assertEquals(exception.getMessage(), "Пустой файл или строка");
    }

    @Test
    public void noSuchFileTest() {
        File file = new File("Tasks"); //несуществующий файл
        Exception exception = assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(file);
        });

        assertEquals(exception.getMessage(), "Нет такого файла");
    }

    @Test
    public void saveSomeTasksTest() {

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        Task task1 = new Task("TASK1", "DESK1TASK");//1
        Epic epic1 = new Epic("EPIC1", "DESK1EPIC");//2
        Subtask subtask1 = new Subtask("SUBTASK1", "DESK1SUBTASK");//3
        Subtask subtask2 = new Subtask("SUBTASK2", "DESK2SUBTASK");//4
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createEpic(epic1);
        fileBackedTaskManager.createSubtask(subtask1, epic1.getId());
        fileBackedTaskManager.createSubtask(subtask2, epic1.getId());
        List<Task> arr = List.of(task1, epic1, subtask1, subtask2);
        Assertions.assertEquals(arr, fileBackedTaskManager.getAllTypesOfTasks());

    }


    @Test
    public void loadSomeTasks() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        Task task1 = new Task("TASK1", "DESK1TASK");//1
        Epic epic1 = new Epic("EPIC1", "DESK1EPIC");//2
        Subtask subtask1 = new Subtask("SUBTASK1", "DESK1SUBTASK");//3
        Subtask subtask2 = new Subtask("SUBTASK2", "DESK2SUBTASK");//4
        task1.setId(1);
        epic1.setId(2);
        subtask1.setId(3);
        subtask1.setEpicId(2);
        subtask2.setId(4);
        subtask2.setEpicId(2);
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(inputFile);
        List<Task> arr = List.of(task1, epic1, subtask1, subtask2);
        Assertions.assertEquals(arr, fileBackedTaskManager1.getAllTypesOfTasks());
    }
}
