package manager;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class EpicTaskManager {

    public HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();

    ManagerSeq seq;

    public HashMap<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public EpicTaskManager(ManagerSeq seq) {
        this.seq = seq;
    }

    public Task tryCreateSubTask(SubTask subTask){
        for(EpicTask epicTask : epicTasks.values())
            if(epicTask.equals(subTask.getEpicTask())) {
                SubTask concreteSubTask = generateSubTask(subTask, epicTask);
                return addTask(concreteSubTask);
            }

        return null;
    }

    public void removeSubTask(SubTask subTask){
        for(Task task : subTasks.values())
            if(task.equals(subTask))
                subTask = (SubTask) task;

        subTask.getEpicTask().removeSubTask(subTask);
        subTasks.remove(subTask.getId());
    }

    public void removeEpicTask(EpicTask epicTask){
        removeSubTasksOfEpic(epicTask);
        epicTasks.remove(epicTask.getId());
    }

    public void removeSubTasksOfEpic(EpicTask epicTask){
        ArrayList<SubTask> epicSubTasks = epicTask.getSubTasks();

        ArrayList<Integer> temp = new ArrayList<>();

        for(Integer subTaskNum : subTasks.keySet())
            if(epicSubTasks.contains(subTasks.get(subTaskNum)))
                temp.add(subTaskNum);

        for(Integer subTaskNum : temp)
            subTasks.remove(subTaskNum);
    }

    public Task addTask(Task task){
        switch(task.getTaskType()){
            case EPIC_TASK:
                EpicTask epicTaskToAdd = new EpicTask(seq.getNextSeq(), (EpicTask) task);
                epicTasks.put(epicTaskToAdd.getId() , epicTaskToAdd);
                return epicTaskToAdd;
            case SUB_TASK:
                subTasks.put(task.getId(), (SubTask) task);
                break;
            default:
                return null;
        }
        return task;
    }

    public void clear() {
        epicTasks.clear();
        subTasks.clear();
    }

    private SubTask generateSubTask(SubTask subTask, EpicTask epicTask){
        SubTask concreteSubTask = new SubTask(seq.getNextSeq(), subTask);
        concreteSubTask.setEpicTask(epicTask);

        epicTask.addSubTask(concreteSubTask);
        concreteSubTask.setEpicTask(epicTask);

        return concreteSubTask;
    }
}
