public class Task {

    private int id;
    private String name;
    private String description;
    private TaskStatus status;

    public Task(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }
}
