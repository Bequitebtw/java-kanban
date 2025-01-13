import exception.ManagerSaveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Task;
import manager.FileBackedTaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File uploadEmptyFileTest = new File("src/files/uploadEmptyFileTest.txt"); // пустой
    private File loadSomeTasksTest = new File("src/files/loadSomeTasksTest.txt");
    private File saveSomeTasksTest = new File("src/files/saveSomeTasksTest.txt");

    @Override
    protected FileBackedTaskManager getTaskManager() {
        return new FileBackedTaskManager(new File("src/files/test.txt"));
    }

    @BeforeEach
    public void setFieldsNotIntersection() {
        task.setStartTime(LocalDateTime.of(2024, 10, 10, 1, 0));
        task.setDuration(Duration.ofMinutes(10));
        task2.setStartTime(LocalDateTime.of(2024, 10, 10, 2, 0));
        task2.setDuration(Duration.ofMinutes(10));
        epic.setStartTime(LocalDateTime.of(2024, 10, 10, 3, 0));
        epic.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.of(2024, 10, 10, 4, 0));
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask2.setStartTime(LocalDateTime.of(2024, 10, 10, 5, 0));
        subtask2.setDuration(Duration.ofMinutes(10));
        subtask3.setStartTime(LocalDateTime.of(2024, 10, 10, 6, 0));
        subtask3.setDuration(Duration.ofMinutes(10));
    }

    @Test
    public void uploadEmptyFileTest() {
        Exception exception = assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(uploadEmptyFileTest); // пустой файл
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
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(saveSomeTasksTest);
        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubtask(subtask1, epic.getId());
        fileBackedTaskManager.createSubtask(subtask2, epic.getId());
        List<Task> arr = List.of(task, epic, subtask1, subtask2);
        Assertions.assertEquals(arr, fileBackedTaskManager.getAllTypesOfTasks());
    }

    @Test
    public void loadSomeTasksTest() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(loadSomeTasksTest);
//        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubtask(subtask1, epic.getId());
        fileBackedTaskManager.createSubtask(subtask2, epic.getId());

        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(loadSomeTasksTest);

        Assertions.assertEquals(fileBackedTaskManager.getAllTypesOfTasks(), fileBackedTaskManager1.getAllTypesOfTasks());
    }

}
