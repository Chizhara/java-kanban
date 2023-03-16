package manager;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class EpicTaskManager {

    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private final ManagerSeq seq;

    public HashMap<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public EpicTaskManager(ManagerSeq seq) {
        this.seq = seq;
    }

    public SubTask addSubTask(SubTask subTask) {
        if(epicTasks.containsKey(subTask.getEpicTaskId())){
            subTask.setId(seq.getNextSeq());

            EpicTask epicTask = epicTasks.get(subTask.getEpicTaskId());
            epicTask.addSubTask(subTask);

            subTasks.put(subTask.getId(), subTask);
            return subTask;
        }
        return null;
    }

    public EpicTask addEpicTask(EpicTask epicTask) {
        epicTask.setId(seq.getNextSeq());
        epicTasks.put(epicTask.getId() , epicTask);
        return epicTask;
    }

    public boolean updateEpicTask(EpicTask epicTaskDonor){
        EpicTask taskUpdated = epicTasks.get(epicTaskDonor.getId());

        if(taskUpdated == null)
            return false;

        taskUpdated.updateTaskValues(epicTaskDonor);
        updateSubTasksListOfEpic(taskUpdated, epicTaskDonor.getSubTasks());

        return true;
    }

    public boolean updateSubTask(SubTask subTaskDonor) {
        SubTask taskUpdated = subTasks.get(subTaskDonor.getId());

        if(taskUpdated == null)
            return false;

        taskUpdated.updateTaskValues(subTaskDonor);
        updateSubTaskEpic(taskUpdated, epicTasks.get(subTaskDonor.getEpicTaskId()));

        return true;
    }

    public SubTask removeSubTask(Integer subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);

        if(subTask == null)
            return  null;

        epicTasks.get(subTask.getEpicTaskId()).removeSubTask(subTask);
        return subTasks.remove(subTask.getId());
    }

    public EpicTask removeEpicTask(Integer epicTaskId) {
        removeSubTasksOfEpic(epicTasks.get(epicTaskId));
        return epicTasks.remove(epicTaskId);
    }

    public void removeSubTasksOfEpic(EpicTask epicTask) {
        for(SubTask subTask : epicTask.getSubTasks())
            subTasks.remove(subTask.getId());
    }

    public void clearEpicTasks() {
        subTasks.clear();
        epicTasks.clear();
    }

    public void clearSubTasks() {
        subTasks.clear();

        for(EpicTask epicTask : epicTasks.values())
            epicTask.getSubTasks().clear();
    }

    private void updateSubTasksListOfEpic(EpicTask epicTaskUpdated, ArrayList<SubTask> subTasksDonor) {
        if(!epicTaskUpdated.getSubTasks().equals(subTasksDonor))
            return;

        for(SubTask subTask : subTasksDonor)
            if(subTasks.containsKey(subTask.getId()))
                updateSubTaskEpic(subTask, epicTaskUpdated);
    }

    private void updateSubTaskEpic(SubTask subTask, EpicTask epicTaskUpdated) {
        epicTasks.get(subTask.getEpicTaskId()).removeSubTask(subTask);
        subTask.setEpicTaskId(epicTaskUpdated.getId());
        epicTaskUpdated.addSubTask(subTask);
    }
}
