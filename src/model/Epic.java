package model;

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
        String duration = this.duration == null ? "Продолжительность не определена" : String.valueOf(this.duration.toMinutes());
        String startTime = this.startTime == null ? "Время начала не определено" : this.startTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
        String endTime = this.endTime == null ? "Время окончания не определено" : this.endTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));

        if (this.startTime == null || this.endTime == null || this.duration == null) {
            return this.getId() + "," + Type.EPIC + "," + this.getName() +
                    "," + this.getStatus() + "," + this.getDescription() + "," + "Время начала не определено" +
                    "," + "Продолжительность не определена" + "," + "Время окончания не определено";
        }
        return this.getId() + "," + Type.EPIC + "," + this.getName() +
                "," + this.getStatus() + "," + this.getDescription() + "," + startTime +
                "," + duration + "," + endTime;
    }
}
