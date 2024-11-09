package tasks;

import java.util.Objects;

public class Subtask extends Task{
    private int epicId;

    public Subtask(String name, String description) {
        super(name, description);
        status = Status.NEW;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        String result = "\n" + "       SubtaskId: " + this.getId() + "\n" +
                "       EpicId: " + getEpicId() + "\n" +
                "       Title: " + this.getName() + "\n" +
                "       Description: " + this.getDescription() + "\n" +
                "       Tasks.Subtask.Tasks.Status: " + this.getStatus() + "\n";

        return result;
    }

}
