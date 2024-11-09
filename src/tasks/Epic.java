package tasks;

import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {
    private ArrayList<Integer>subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
        status = Status.NEW;
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
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
