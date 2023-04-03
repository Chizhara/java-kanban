package manager;
import model.*;

import java.sql.Array;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();

    void remove(int id);
}