import java.util.HashMap;
import java.util.Map;

public class Epic {
    private int id;
    private Status status;

    private String name;
    private String description;

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description, int id) {
        status = Status.NEW;
        this.name = name;
        this.id = id;
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void checkStatus() {
        if (this.status == Status.DONE) {
            for (Subtask subtask : subtasks.values()) {
                subtask.setStatus(Status.DONE);
            }
        }

        for (Map.Entry<Integer, Subtask> taskEntry : subtasks.entrySet()) {
            if (taskEntry.getValue().getStatus() != Status.DONE) {
                for (Subtask subtask : subtasks.values()) {
                    if (subtask.getStatus() == Status.IN_PROGRESS) {
                        this.status = Status.IN_PROGRESS;
                        return;
                    }
                }
                return;
            }
        }
        status = Status.DONE;
    }

    public void addSubtask(String name, String description, int id) {
        subtasks.put(id, new Subtask(name, description, id));
        checkStatus();
    }

    @Override
    public String toString() {
        String result = "\n" + "EpicId: " + this.id + "\n" +
                "Title: " + this.name + "\n" +
                "Description: " + this.description + "\n" +
                "Status: " + this.status + "\n";

        for (Map.Entry<Integer, Subtask> taskEntry : subtasks.entrySet()) {
            result += taskEntry.getValue();
        }
        return result;
    }
}
