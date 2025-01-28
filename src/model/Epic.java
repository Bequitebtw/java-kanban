package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
        status = Status.NEW;
        startTime = LocalDateTime.now();
        duration = Duration.ZERO;
        endTime = LocalDateTime.now();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void clearSubtasksId() {
        subtasks.clear();
    }

    @Override
    public String toString() {
        return this.getId() + "," + Type.EPIC + "," + this.getName() +
                "," + this.getStatus() + "," + this.getDescription() + "," + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")) +
                "," + duration.toMinutes() + "," + endTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
    }
}
