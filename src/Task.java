public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
        status = Status.NEW;
    }

    @Override
    public String toString() {
        String result = "TaskId: " + this.id + "\n" +
                "Title: " + this.name + "\n" +
                "Description: " + this.description + "\n" +
                "Status: " + this.status;
        System.out.println();
        return result;
    }

}
