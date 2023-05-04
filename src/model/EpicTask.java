package model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class EpicTask extends Task {

    protected ArrayList<SubTask> subTasks;

    public EpicTask(Integer id,String name, String description, Instant startTime, Integer duration) {
        super(id , name, description, startTime, duration);
        taskType = TaskType.EPIC_TASK;
        subTasks = new ArrayList<>();
    }

    public EpicTask(Integer id, EpicTask task) {
        super(id ,task);
        taskType = TaskType.EPIC_TASK;
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
    }

    @Override
    public TaskStatus getStatus() {
        status = null;

        if(subTasks.isEmpty()) {
            status = TaskStatus.NEW;
            return TaskStatus.NEW;
        } else
            for(SubTask subTask : subTasks)
                if(subTask.getStatus() == TaskStatus.DONE)
                    status = TaskStatus.DONE;
                else if(subTask.getStatus() == TaskStatus.IN_PROGRESS || subTask.getStatus() != TaskStatus.DONE && status == TaskStatus.DONE) {
                    status = TaskStatus.IN_PROGRESS;
                    break;
                }
                else if(subTask.getStatus() == TaskStatus.NEW && status != TaskStatus.DONE)
                    status = TaskStatus.NEW;

        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(null == obj) return false;
        if(this.getClass() != obj.getClass()) return false;
        EpicTask otherEpicTask = (EpicTask) obj;

        return  Objects.equals(id, otherEpicTask.id) &&
                Objects.equals(name, otherEpicTask.name) &&
                Objects.equals(description, otherEpicTask.description) &&
                Objects.equals(getStatus(), otherEpicTask.getStatus()) &&
                Objects.equals(subTasks, otherEpicTask.subTasks);
    }

    @Override
    public Instant getStartTime() {
        if(subTasks.isEmpty())
            return null;

        startTime = Instant.MAX;

        for(SubTask subTask : subTasks) {
            if(startTime.isAfter(subTask.getStartTime())) {
                startTime = subTask.getStartTime();
            }
        }

        return startTime;
    }

    @Override
    public Integer getDuration() {
        if(subTasks.isEmpty()) {
            return null;
        }

        duration = 0;

        for(SubTask subTask : subTasks) {
            duration += subTask.getDuration();
        }

        return duration;
    }

    @Override
    public Instant getEndTime() {
        if(subTasks.isEmpty()) {
            return null;
        }

        return subTasks.stream().max(Comparator.comparing(SubTask::getEndTime)).get().getEndTime();
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + getStatus().toString() + '\'' +
                ", subTask.length='" + subTasks.size() + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }
}
