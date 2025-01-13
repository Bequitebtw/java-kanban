package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description) {
        super(name, description);
        status = Status.NEW;
    }
    public Subtask(String name, String description,LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        status = Status.NEW;
    }
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return this.getId() + "," + Type.SUBTASK + "," + this.getName() +
                "," + this.getStatus() + "," + this.getDescription() + "," + this.startTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")) +
                "," + this.duration.toMinutes() + "," + this.getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")) + "," + getEpicId();
    }

}
