package taskmanager;

import manager.HistoryManager;
import manager.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public abstract class TaskManagerTest <T extends TaskManager> {
    T taskManager;

    protected abstract void init();

    @BeforeEach
    void before() {
        init();
    }

    @Test
    void addNewTask() {
        final Task task = new Task(null ,"Task", "Task", null, null);
        final Task returnedTask;
        assertNotNull(returnedTask = taskManager.addTask(task), "При добавлении ничего не возвращает");
        assertEquals(returnedTask, task, "Задачи при добавлении не совпадают");
        assertEquals(taskManager.getTask(returnedTask.getId()), returnedTask, "Задача не найден");
    }

    @Test
    void addNewEpicTask() {
        final EpicTask epicTask = new EpicTask(null ,"EpicTask", "EpicTask", null,
                2);
        final EpicTask returnedEpicTask;
        assertNotNull(returnedEpicTask = taskManager.addEpicTask(epicTask),
                "При добавлении ничего не возвращает");
        assertEquals(returnedEpicTask, epicTask, "Эпики при добавлении не совпадают");
        assertEquals(taskManager.getEpicTask(returnedEpicTask.getId()), returnedEpicTask, "Эпик не найден");
    }

    @Test
    void addNewSubTask() {
        final int epicId = taskManager.addEpicTask(new EpicTask(null, "Epic", "Epic", null,
                null)).getId();
        final SubTask subTask = new SubTask(null ,"SubTask", "SubTask", epicId, null,
                null);
        final SubTask returnedSubTask;
        assertNotNull(returnedSubTask = taskManager.addSubTask(subTask), "При добавлении ничего не возвращает");
        assertEquals(returnedSubTask, subTask, "Подзадачи при добавлении не совпадают");
        assertEquals(taskManager.getSubTask(returnedSubTask.getId()), returnedSubTask, "Подзадача не найден");
    }

    @Test
    void removeTask() {
        final Task task = taskManager.addTask(new Task(null ,"Task", "Task", null,
                null));
        assertNotNull(taskManager.getTask(task.getId()), "Задача не добавилась");
        assertEquals(taskManager.removeTask(task.getId()), task, "Вернуло неверную задачу");
        assertNull(taskManager.getTask(task.getId()), "Задача не удалилась");
    }

    @Test
    void removeEpicTask() {
        final EpicTask epicTask = taskManager.addEpicTask(new EpicTask(null ,"EpicTask", "EpicTask",
                null, null));
        assertNotNull(taskManager.getEpicTask(epicTask.getId()), "Задача не добавилась");
        assertEquals(taskManager.removeEpicTask(epicTask.getId()), epicTask, "Вернуло неверную задачу");
        assertNull(taskManager.getEpicTask(epicTask.getId()), "Задача не удалилась");
    }

    @Test
    void removeSubTask() {
        final int epicId = taskManager.addEpicTask(new EpicTask(1, "Epic", "Epic", null,
                null)).getId();
        final SubTask subTask = taskManager.addSubTask( new SubTask(null ,"SubTask", "SubTask",
                epicId, null, null));
        assertNotNull(taskManager.getSubTask(subTask.getId()), "Подзадача не добавилась");
        assertEquals(taskManager.removeSubTask(subTask.getId()), subTask, "Вернуло неверную подзадачу");
        assertNull(taskManager.getSubTask(subTask.getId()), "Подзадача не удалилась");
    }

    @Test
    void shouldReturnTasks() {
        final Task taskA = taskManager.addTask(new Task(null ,"TaskA", "TaskA", null,
                null));
        final Task taskB = taskManager.addTask(new Task(null ,"TaskB", "TaskB", null,
                null));

        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Не возвращает список задач");
        assertTrue(tasks.contains(taskA), "Нет задачи A");
        assertTrue(tasks.contains(taskB), "Нет задачи B");
        assertEquals(tasks.size(), 2, "Есть лишняя задача");
    }

    @Test
    void shouldReturnEpicTask() {
        final EpicTask taskA = taskManager.addEpicTask(new EpicTask(null ,"EpicTaskA", "EpicTaskA",
                null, null));
        final EpicTask taskB = taskManager.addEpicTask(new EpicTask(null ,"EpicTaskB", "EpicTaskB",
                null, null));

        List<EpicTask> epicTasks = taskManager.getEpicTasks();
        assertNotNull(epicTasks, "Не возвращает список эпиков");
        assertTrue(epicTasks.contains(taskA), "Нет эпика A");
        assertTrue(epicTasks.contains(taskB), "Нет эпика B");
        assertEquals(epicTasks.size(), 2, "Есть лишний эпик");
    }

    @Test
    void shouldReturnSubTasksOfEpic() {
        final int epicIdA = taskManager.addEpicTask(new EpicTask(null, "EpicA", "EpicA",
                null, null)).getId();
        final int epicIdB = taskManager.addEpicTask(new EpicTask(null, "EpicB", "EpicB",
                null, null)).getId();
        final SubTask subTaskA = taskManager.addSubTask(new SubTask(null ,"SubTaskA", "SubTaskA",
                epicIdA, null, null));
        final SubTask subTaskB = taskManager.addSubTask(new SubTask(null ,"SubTaskB", "SubTaskB",
                epicIdA, null, null));
        final SubTask subTaskC = taskManager.addSubTask(new SubTask(null ,"SubTaskC", "SubTaskC",
                epicIdB, null, null));

        List<SubTask> subTasksOfEpicA = taskManager.getSubTasksOfEpic(epicIdA);
        assertFalse(subTasksOfEpicA.isEmpty(), "Возвращает пустой список");
        assertTrue(subTasksOfEpicA.contains(subTaskA), "Не найдена подзадача A");
        assertTrue(subTasksOfEpicA.contains(subTaskB), "Не найдена подзадача B");
        assertFalse(subTasksOfEpicA.contains(subTaskC), "Найдена подзадача C");
    }

    @Test
    void shouldNotAddSubTaskWithoutEpic() {
        final SubTask taskA = new SubTask(null ,"SubTaskA", "SubTaskA", null, null,
                null);
        final SubTask taskB = new SubTask(null ,"SubTaskB", "SubTaskB", null, null,
                null);

        final SubTask taskAReturned = taskManager.addSubTask(taskA);
        final SubTask taskBReturned = taskManager.addSubTask(taskB);

        assertNull(taskAReturned, "Подзадача без идентификатора добавилась");
        assertNull(taskBReturned, "Подзадача без эпика добавилась");

        List<SubTask> subTasks = taskManager.getSubTasks();
        assertFalse(subTasks.contains(taskA), "Подзадача без идентификатора добавилась");
        assertFalse(subTasks.contains(taskB), "Подзадача без идентификатора добавилась");
    }

    @Test
    void returnNotEmptyTasksListAfterClearing() {
        final Task task = new Task(null ,"Task", "Task", null, null);
        taskManager.addTask(task);
        assertFalse(taskManager.getTasks().isEmpty(), "Список задач пустой");
        taskManager.clearTasks();
        assertTrue(taskManager.getTasks().isEmpty(), "Список задач не пустой");
    }

    @Test
    void returnNotEmptyEpicTasksListAfterClearing() {
        final EpicTask epicTask = new EpicTask(null ,"EpicTask", "EpicTask", null,
                null);
        taskManager.addEpicTask(epicTask);
        assertFalse(taskManager.getEpicTasks().isEmpty(), "Список эпиков пустой");
        taskManager.clearEpicTasks();
        assertTrue(taskManager.getEpicTasks().isEmpty(), "Список эпиков не пустой");
    }

    @Test
    void removeSubTasksAfterClearingEpics() {
        final int epicIdA = taskManager.addEpicTask(new EpicTask(null, "EpicA", "EpicA",
                null, null)).getId();
        taskManager.addSubTask(new SubTask(null ,"SubTaskA", "SubTaskA", epicIdA, null,
                null));
        taskManager.addSubTask(new SubTask(null ,"SubTaskB", "SubTaskB", epicIdA, null,
                null));

        assertFalse(taskManager.getSubTasks().isEmpty(), "Подзадачи не добавились");
        taskManager.clearEpicTasks();
        assertTrue(taskManager.getSubTasks().isEmpty(), "Подзадачи не удалились");
    }

    @Test
    void removeCurrentlySubTasksOfEpicAfterRemovingEpic() {
        final int epicIdA = taskManager.addEpicTask(new EpicTask(null, "EpicA", "EpicA",
                null, null)).getId();
        final int epicIdB = taskManager.addEpicTask(new EpicTask(null, "EpicB", "EpicB",
                null, null)).getId();
        final SubTask subTaskA = taskManager.addSubTask(new SubTask(null ,"SubTaskA", "SubTaskA",
                epicIdA, null, null));
        final SubTask subTaskB = taskManager.addSubTask(new SubTask(null ,"SubTaskB", "SubTaskB",
                epicIdA, null, null));
        final SubTask subTaskC = taskManager.addSubTask(new SubTask(null ,"SubTaskC", "SubTaskC",
                epicIdB, null, null));

        assertFalse(taskManager.getSubTasks().isEmpty(),"Подзадачи не добавились");
        taskManager.removeEpicTask(epicIdA);
        List<SubTask> subTasks = taskManager.getSubTasks();
        assertFalse(subTasks.contains(subTaskA),"Подзадача A не удалилась");
        assertFalse(subTasks.contains(subTaskB),"Подзадача B не удалилась");
        assertTrue(subTasks.contains(subTaskC), "Удалились лишние подзадачи");
    }

    @Test
    void returnCurrentPrioritizedTasks() {
        Set<Task> tasks;

        final Task taskA = taskManager.addTask(new Task(null ,"TaskA", "TaskA", Instant.MAX.minusSeconds(1000),
                null));
        final Task taskB = taskManager.addTask(new Task(null ,"TaskB", "TaskB", Instant.MAX.minusSeconds(3000),
                null));
        final Task taskC = taskManager.addTask(new Task(null ,"TaskC", "TaskC", Instant.MAX.minusSeconds(2000),
                null));

        taskManager.addTask(taskA);
        taskManager.addTask(taskB);
        taskManager.addTask(taskC);

        tasks = taskManager.getPrioritizedTasks();

        System.out.println(taskManager.getTasks());

        assertNotNull(tasks, "Возвращает пустой список");
        assertEquals(Set.of(taskB, taskC, taskA), tasks, "Возвращает неверный список");
    }


    protected static class EmptyHistoryManager implements HistoryManager {
        @Override
        public void add(Task task) {

        }

        @Override
        public List<Task> getHistory() {
            return List.of(new Task(null ,"Task", "Task", null, null));
        }

        @Override
        public void remove(int id) {

        }
    }
}
