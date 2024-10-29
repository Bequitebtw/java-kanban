package tasks;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;

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


    @Override
    public String toString() {
        String result = "\nTaskId: " + this.id + "\n" +
                "Title: " + this.name + "\n" +
                "Description: " + this.description + "\n" +
                "Tasks.Task.Tasks.Status: " + this.status + "\n";
        System.out.println();
        return result;
    }

}
