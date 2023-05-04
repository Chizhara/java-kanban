package model;

import java.time.Instant;
import java.util.Objects;

public class SubTask extends Task {
    protected Integer epicTaskId;

    public SubTask(Integer id, String name, String description, Integer epicTaskId, Instant startTime, Integer duration) {
        super(id ,name, description, startTime, duration);
        this.epicTaskId = epicTaskId;
        taskType = TaskType.SUB_TASK;
    }

    public SubTask(Integer id ,SubTask task) {
        super(id, task);
        this.epicTaskId = task.epicTaskId;
        taskType = TaskType.SUB_TASK;
    }

    public void setEpicTaskId(Integer epicTaskId) {
        this.epicTaskId = epicTaskId;
    }

    public Integer getEpicTaskId() {
        return epicTaskId;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(null == obj) return false;
        if(this.getClass() != obj.getClass()) return false;
        SubTask otherSubTask = (SubTask) obj;

        return Objects.equals(id, otherSubTask.id) && Objects.equals(name, otherSubTask.name) &&
               Objects.equals(description, otherSubTask.description) && Objects.equals(status, otherSubTask.status) &&
               Objects.equals(epicTaskId, otherSubTask.epicTaskId);
    }

    @Override
    public String toString(){
        String result = super.toString().replace("Task{","SubTask{");
        result = result.replace("}", ", EpicTask='" + epicTaskId + '\'');
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicTaskId);
    }
}
