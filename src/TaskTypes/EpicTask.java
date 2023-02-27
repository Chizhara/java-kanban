import java.util.ArrayList;
public class EpicTask extends Task {
    ArrayList<SubTask> subTasks = new ArrayList<>();
    public EpicTask(int id, String name, String description) {
        super(id, name, description);
    }
}
