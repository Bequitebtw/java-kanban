import java.util.ArrayList;


public class Epic extends Task {
    private Status status;
    private ArrayList<Integer>subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
        status = Status.NEW;
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtaskId(int id) {
        subtasks.add(id);
    }

    @Override
    public String toString() {
        String result = "\n" + "EpicId: " + this.getId() + "\n" +
                "Title: " + this.getName() + "\n" +
                "Description: " + this.getDescription() + "\n" +
                "Epic.Status: " + this.getStatus() + "\n";
        return result;
    }
}
