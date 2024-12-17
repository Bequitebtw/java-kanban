import com.sun.source.util.TaskListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTaskManagerTest {
    private File outputFile = new File("OutputFile.txt"); //пустой (для конструктора класса, для записи)
    private File inputFile = new File("InputFile.txt"); // заполненный тасками
    private File emptyFile = new File("emptyFile.txt"); //пустой


    // можно было сделать геттер для файла чтобы сравнить старые данные и перезаписанные
    @Test
    public void uploadEmptyFileTest() throws IOException {
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(outputFile);
            Assertions.assertNotEquals(fileBackedTaskManager.loadFromFile(emptyFile), fileBackedTaskManager);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void noSuchFileTest() throws IOException {
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(outputFile);
            Assertions.assertNull(fileBackedTaskManager.loadFromFile(new File("asdjaisfjiojdfojaso")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void saveSomeTasksTest() {

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(outputFile);
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
    public void loadSomeTasks() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(outputFile);
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
        try {
            fileBackedTaskManager.loadFromFile(inputFile);
            List<Task> arr = List.of(task1, epic1, subtask1, subtask2);
            Assertions.assertEquals(arr, fileBackedTaskManager.getAllTypesOfTasks());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
