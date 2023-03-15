package model;

import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TaskType taskType;

    public Task(Integer id, String name, String description){
        if(id == null)
            id = 0;

        this.id = id.intValue();
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        taskType = TaskType.TASK;
    }

    public Task(Integer id, Task task){
        this(id, task.getName(), task.getDescription());

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
    public String toString(){
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status.toString() + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, taskType);
    }
}
