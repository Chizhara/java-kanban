package model;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TaskType taskType;

    public Task(String name, String description){
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        taskType = TaskType.TASK;
    }

    public Task(Task task){
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = task.getStatus();
        taskType = TaskType.TASK;
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

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(null == obj) return false;
        if(this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;

        return Objects.equals(name, otherTask.name) && Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status);
    }

    @Override
    public String toString(){
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status.toString() + '\'' +
                '}';
    }
}
