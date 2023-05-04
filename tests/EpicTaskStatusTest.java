import model.EpicTask;
import model.SubTask;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EpicTaskStatusTest {
    static private List<SubTask> subTasks;
    static private EpicTask epicTask;

    @BeforeAll
    static void initSubTasks() {
        epicTask = new EpicTask(1 ,"EpicTaskA", "EpicTask", null, null);
        subTasks = new ArrayList<>();

        subTasks.add(new SubTask(2 ,"SubTaskA", "SubTask", epicTask.getId(),
                Instant.MAX.minusSeconds(100000), 2));
        subTasks.add(new SubTask(3 ,"SubTaskB", "SubTask", epicTask.getId(),
                Instant.MAX.minusSeconds(300000), 3));
        subTasks.add(new SubTask(4 ,"SubTaskC", "SubTask", epicTask.getId(),
                Instant.MAX.minusSeconds(200000), 1));

        for(SubTask subTask : subTasks) {
            epicTask.addSubTask(subTask);
        }
    }

    @Test
    public void shouldBeNewWhenSubTasksEmpty() {
        EpicTask epicTaskOwn = new EpicTask(5 ,"EpicTaskA", "EpicTask", null, null);
        assertEquals(TaskStatus.NEW, epicTaskOwn.getStatus());
    }

    @Test
    public void shouldBeNewWhenSubTasksNew() {
        for(SubTask subTask : subTasks) {
            subTask.setStatus(TaskStatus.NEW);
        }

        assertEquals(TaskStatus.NEW, epicTask.getStatus());
    }

    @Test
    public void shouldBeNewWhenSubTasksDone() {
        for(SubTask subTask : subTasks) {
            subTask.setStatus(TaskStatus.DONE);
        }

        assertEquals(TaskStatus.DONE, epicTask.getStatus());
    }

    @Test
    public void shouldBeNewWhenSubTasksNewAndDone() {
        for(SubTask subTask : subTasks) {
            subTask.setStatus(TaskStatus.NEW);
        }
        subTasks.get(0).setStatus(TaskStatus.DONE);

        assertEquals(TaskStatus.IN_PROGRESS, epicTask.getStatus());
    }

    @Test
    public void shouldBeNewWhenSubTasksInProgress() {
        for(SubTask subTask : subTasks) {
            subTask.setStatus(TaskStatus.IN_PROGRESS);
        }

        assertEquals(TaskStatus.IN_PROGRESS, epicTask.getStatus());
    }

    @Test
    public void shouldReturnCurrentStartTime() {
        Instant startTime = epicTask.getStartTime();
        assertNotNull(startTime, "Возвращает null значение");
        assertEquals(startTime, subTasks.get(1).getStartTime(), "Возвращает неверное значение");
    }

    @Test
    public void shouldReturnCurrentDurationTime() {
        Integer duration = epicTask.getDuration();
        Integer subTasksDuration = 0;

        for(SubTask subTask : subTasks) {
            subTasksDuration += subTask.getDuration();
        }
        assertNotNull(duration, "Возвращает null значение");
        assertEquals(duration,  subTasksDuration , "Возвращает неверное значение");
    }

    @Test
    public void shouldReturnCurrentEndTime() {
        Instant endTime = epicTask.getEndTime();
        Instant lastEndTime = subTasks.stream().max(Comparator.comparing(SubTask::getEndTime)).get().getEndTime();
        assertNotNull(endTime, "Возвращает null значение");
        assertEquals(endTime,  lastEndTime , "Возвращает неверное значение");
    }
}
