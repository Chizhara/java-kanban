package history;

import manager.history.HistoryManager;
import model.EpicTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class HistoryManagerTest<T extends HistoryManager> {
    T historyManager;
    List<Task> tasks;

    protected abstract void init();

    @BeforeEach
    void initBeforeEach() {
        init();

        tasks = new ArrayList<>();
        tasks.add(new Task(1, "TaskA", "TaskA", null, null));
        tasks.add(new EpicTask(3, "EpicTask", "EpicTask", null, null));
        tasks.add(new Task(2, "TaskB", "TaskB", null, null));
    }

    @Test
    void historyManagerEmptyWithNoTasks() {
        assertTrue(historyManager.getHistory().isEmpty(), "История не пустая");
    }

    @Test
    void addNewTasksOfAnyType() {
        Task task = tasks.get(0);
        historyManager.add(task);
        assertTrue(historyManager.getHistory().contains(task), "Задачи не найдено");
    }

    @Test
    void historyManagerRemoveTask() {
        Task task = tasks.get(0);
        historyManager.add(task);
        assertFalse(historyManager.getHistory().isEmpty(), "Задача не добавилась");
        historyManager.remove(task.getId());
        assertFalse(historyManager.getHistory().contains(task), "Задача не добавилась");
    }

    @Test
    void historyManagerRemoveFromBeginning() {
        for (Task task : tasks) {
            historyManager.add(task);
        }

        historyManager.remove(tasks.get(0).getId());
        Collections.reverse(tasks);
        tasks.remove(tasks.size() - 1);
        assertEquals(tasks, historyManager.getHistory(), "Неверное удаление первого элемента");
    }

    @Test
    void historyManagerRemoveFromMiddle() {
        for (Task task : tasks) {
            historyManager.add(task);
        }

        historyManager.remove(tasks.get(1).getId());
        Collections.reverse(tasks);
        tasks.remove(tasks.size() - 2);
        assertEquals(tasks, historyManager.getHistory(), "Неверное удаление первого элемента");
    }

    @Test
    void historyManagerRemoveFromEnd() {
        for (Task task : tasks) {
            historyManager.add(task);
        }

        historyManager.remove(tasks.get(tasks.size() - 1).getId());
        Collections.reverse(tasks);
        tasks.remove(0);
        assertEquals(tasks, historyManager.getHistory(), "Неверное удаление первого элемента");
    }
}
