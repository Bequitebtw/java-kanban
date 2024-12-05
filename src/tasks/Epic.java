package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
        status = Status.NEW;
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void clearSubtasksId() {
        subtasks.clear();
    }


    @Override
    public String toString() {
        String result = "\n" + "EpicId: " + this.getId() + "\n" +
                "Title: " + this.getName() + "\n" +
                "Description: " + this.getDescription() + "\n" +
                "Tasks.Epic.Tasks.Status: " + this.getStatus() + "\n";
        return result;
    }
}
