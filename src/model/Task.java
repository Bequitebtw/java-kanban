package model;

import exception.ManagerSaveException;

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

    public Task(String name, String description,LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
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

    // Сделал выброс исключения если не установлены поля времени
    public LocalDateTime getEndTime() {
        try {
            return startTime.plus(duration);
        } catch (NullPointerException e){
            throw new ManagerSaveException("Не установлена дата начала задачи и время на выполнение");
        }
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
        return this.getId() + "," + Type.TASK + "," + this.getName() +
                "," + this.getStatus() + "," + this.getDescription() +
                "," + this.startTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")) +
                "," + this.duration.toMinutes() + "," + this.getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
    }

}
