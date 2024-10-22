import java.util.ArrayList;


public class Epic extends Task {
    private Status status;
    private ArrayList<Integer>subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
        status = Status.NEW;
    }
    //Не знаю почему, но без переопределения метод не работал
    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtaskId(int id) {
        subtasks.add(id);
    }

//    @Override
//    public void setStatus(Status status) {
//        this.status = status;
//    }

    @Override
    public String toString() {
        String result = "\n" + "EpicId: " + this.getId() + "\n" +
                "Title: " + this.getName() + "\n" +
                "Description: " + this.getDescription() + "\n" +
                "Epic.Status: " + this.status + "\n";
        return result;
    }
}
