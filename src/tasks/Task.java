package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (duration.equals(null) || startTime.equals(null)) {
            return null;
        }
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
//        String result = "\nTaskId: " + this.id + "\n" +
//                "Title: " + this.name + "\n" +
//                "Description: " + this.description + "\n" +
//                "Tasks.Task.Tasks.Status: " + this.status + "\n";
//        System.out.println();
//        return result;
        return this.getId() + "," + Type.TASK + "," + this.getName() +
                "," + this.getStatus() + "," + this.getDescription() +
                "," + this.startTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")) +
                "," + this.duration.toMinutes() + "," + this.getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
    }

}
