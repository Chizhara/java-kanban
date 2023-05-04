package taskmanager;

import manager.HistoryManager;
import manager.InMemoryTaskManager;
import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected void init() {
        taskManager = new InMemoryTaskManager(new EmptyHistoryManager());
    }

    @Test
    void shouldReturnHistoryManagerFromHistoryManager() {
        Task task = new Task(null ,"Task", "Task", null, null);

        InMemoryTaskManager taskManager = new InMemoryTaskManager(new HistoryManager() {
            @Override
            public void add(Task task) {
            }

            @Override
            public List<Task> getHistory() {
                return Collections.singletonList(task);
            }

            @Override
            public void remove(int id) {
            }
        });

        List<Task> history = taskManager.getHistory();
        assertNotNull(history, "Не вернул историю");
        assertEquals(history.size(), 1, "Размер не совпадает");
        assertTrue(history.contains(task), "История не содержит задачу");
    }
}
