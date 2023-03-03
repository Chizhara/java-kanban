package model;

import java.util.Objects;

public class SubTask extends Task{
    protected EpicTask epicTask;

    public SubTask(Integer id, String name, String description, EpicTask epicTask) {
        super(id ,name, description);
        this.epicTask = epicTask;
        taskType = TaskType.SUB_TASK;
    }

    public SubTask(Integer id ,SubTask task) {
        super(id, task);
        this.epicTask = task.epicTask;
        taskType = TaskType.SUB_TASK;
    }

    public void setEpicTask(EpicTask epicTask) {
        this.epicTask = epicTask;
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(null == obj) return false;
        if(this.getClass() != obj.getClass()) return false;
        SubTask otherSubTask = (SubTask) obj;

        return Objects.equals(id, otherSubTask.id) && Objects.equals(name, otherSubTask.name) &&
               Objects.equals(description, otherSubTask.description) && Objects.equals(status, otherSubTask.status) &&
               Objects.equals(epicTask, otherSubTask.epicTask);
    }

    @Override
    public String toString(){
        String result = super.toString().replace("Task{","SubTask{");
        result = result.replace("}", ", EpicTask='" + epicTask + '\'');
        return result;
    }
}
