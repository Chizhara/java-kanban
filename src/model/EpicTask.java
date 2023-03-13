package model;

import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {

    protected ArrayList<SubTask> subTasks;

    public EpicTask(Integer id,String name, String description) {
        super(id , name, description);
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

    public void addSubTask(SubTask subTask){
        subTasks.add(subTask);
    }

    public void removeSubTask(SubTask subTask){
        subTasks.remove(subTask);
    }

    @Override
    public TaskStatus getStatus() {
        TaskStatus result = null;
        status = null;

        if(subTasks.isEmpty()) {
            result = TaskStatus.NEW;
            status = TaskStatus.NEW;
        } else {
            for(SubTask subTask : subTasks)
                if(subTask.getStatus() == TaskStatus.DONE){
                    result = TaskStatus.DONE;
                    status = TaskStatus.DONE;
                } else if(subTask.getStatus() == TaskStatus.IN_PROGRESS || subTask.getStatus() != TaskStatus.DONE && status == TaskStatus.DONE){
                    result = TaskStatus.IN_PROGRESS;
                    status = TaskStatus.IN_PROGRESS;
                    break;
                } else if(subTask.getStatus() == TaskStatus.NEW && status != TaskStatus.DONE){
                    result = TaskStatus.NEW;
                    status = TaskStatus.NEW;
                }
        }

        return result;
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
    public String toString(){
        return "EpicTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + getStatus().toString() + '\'' +
                ", subTask.length='" + subTasks.size() + '\'' +
                '}';
    }
}
