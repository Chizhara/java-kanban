package model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TaskType taskType;
    protected int duration;
    protected Instant startTime;

    public Task(Integer id, String name, String description, Instant startTime, Integer duration) {
        if(id == null)
            id = 0;
        if(duration == null)
            duration = 0;
        if(startTime == null || startTime.isBefore(Instant.now()))
            startTime = Instant.now();

        this.id = id;
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
        taskType = TaskType.TASK;
    }

    public Task(Integer id, Task task){
        this(id, task.getName(), task.getDescription(), task.getStartTime(), task.getDuration());

        this.status = task.getStatus();
        taskType = TaskType.TASK;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        if(id == null)
            return 0;
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        if(startTime == null)
            return null;
        return startTime.plus(Duration.ofMinutes(duration));
    }

    public void updateTaskValues(Task taskDonor) {
        this.setName(taskDonor.getName());
        this.setDescription(taskDonor.getDescription());
        this.setStatus(taskDonor.getStatus());
        this.setDuration(taskDonor.getDuration());
        this.setStartTime(taskDonor.getStartTime());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(null == obj) return false;
        if(this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;

        return  Objects.equals(id, otherTask.id) && Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) && Objects.equals(status, otherTask.status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", taskType=" + taskType +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, taskType);
    }
}
