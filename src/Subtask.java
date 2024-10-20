public class Subtask {
    private int id;
    private String name;
    private String description;
    private Status status;

    public Subtask(String name, String description, int id) {
        this.id = id;
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    @Override
    public String toString() {
        String result = "\n" + "       SubtaskId: " + this.id + "\n" +
                "       Title: " + this.name + "\n" +
                "       Description: " + this.description + "\n" +
                "       Status: " + this.status + "\n";

        return result;
    }

    public Status getStatus() {
        return status;
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

    public int getId() {
        return id;
    }


}
