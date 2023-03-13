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

    public Task tryCreateSubTask(SubTask subTask) {
        if(epicTasks.containsKey(subTask.getEpicTaskId())){
            EpicTask epicTask = epicTasks.get(subTask.getEpicTaskId());
            SubTask concreteSubTask = generateSubTask(subTask, epicTask);
            return addTask(concreteSubTask);
        }

        return null;
    }

    public Task addTask(Task task) {
        switch(task.getTaskType()) {
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

    public void removeSubTask(SubTask subTask) {
        subTask = subTasks.get(subTask.getId());

        epicTasks.get(subTask.getEpicTaskId()).removeSubTask(subTask);
        subTasks.remove(subTask.getId());
    }

    public void removeEpicTask(EpicTask epicTask) {
        removeSubTasksOfEpic(epicTask);
        epicTasks.remove(epicTask.getId());
    }

    public void removeSubTasksOfEpic(EpicTask epicTask) {
        for(SubTask subTask : epicTask.getSubTasks())
            subTasks.remove(subTask.getId());
    }

    public void clear() {
        epicTasks.clear();
        subTasks.clear();
    }

    private SubTask generateSubTask(SubTask subTask, EpicTask epicTask) {
        SubTask concreteSubTask = new SubTask(seq.getNextSeq(), subTask);
        concreteSubTask.setEpicTaskId(epicTask.getId());

        epicTask.addSubTask(concreteSubTask);
        concreteSubTask.setEpicTaskId(epicTask.getId());

        return concreteSubTask;
    }
}
