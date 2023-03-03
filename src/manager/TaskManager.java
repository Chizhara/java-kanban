package manager;

import java.util.ArrayList;
import java.util.HashMap;

import model.*;

public class TaskManager {
    private HashMap<Integer ,Task> tasks = new HashMap<>();

    ManagerSeq seq = new ManagerSeq();

    EpicTaskManager epicTaskManager = new EpicTaskManager(seq);

    public HashMap<Integer ,Task> getTasks() {
        HashMap<Integer ,Task> result = new HashMap<>();

        result.putAll(tasks);
        result.putAll(epicTaskManager.getEpicTasks());
        result.putAll(epicTaskManager.getSubTasks());

        return result;
    }

    public void clearTasks(){
        tasks.clear();
        epicTaskManager.clear();
    }

    public Task getTask(int taskId){
        return getTasks().get(taskId);
    }

    public boolean updateTask(Task task){
        HashMap<Integer, Task> tasks = getTasks();
        if(!tasks.containsKey(task.getId()))
            return false;
        if(tasks.get(task.getId()).getTaskType() != task.getTaskType())
            return false;

        Task concreteTask = tasks.get(task.getId());

        concreteTask.setName(task.getName());
        concreteTask.setDescription(task.getDescription());
        concreteTask.setStatus(task.getStatus());

        return true;
    }

    public Task removeTask(int taskId){
        Task result = getTask(taskId);

        switch(result.getTaskType()){
            case TASK:
                tasks.remove(taskId);
                break;
            case SUB_TASK:
                epicTaskManager.removeSubTask((SubTask) result);
                break;
            case EPIC_TASK:
                epicTaskManager.removeEpicTask((EpicTask) result);
                break;
            default:
                result = null;
        }

        return result;
    }

    public int getTaskId(Task task){
        for(int taskId : tasks.keySet()) {
            System.out.println(tasks.get(taskId).toString());
            if (task.equals(tasks.get(taskId)))
                return taskId;
        }
        return 0;
    }

    public Task tryCreateTask(Task task){
        if (task == null)
            return null;

        Task result;

        switch(task.getTaskType()){
            case TASK:
                result = addTask(task);
                break;
            case EPIC_TASK:
                result = epicTaskManager.addTask(task);
                break;
            case SUB_TASK:
                result = epicTaskManager.tryCreateSubTask((SubTask) task);
                break;
            default:
                result = null;
        }

        return result;
    }

    private void removeSubTasksOfEpic(EpicTask epicTask){
        ArrayList<SubTask> subTasks = epicTask.getSubTasks();

        ArrayList<Integer> temp = new ArrayList<>();

        for(Integer taskNum : tasks.keySet())
            if(subTasks.contains(tasks.get(taskNum)))
                temp.add(taskNum);

        for(Integer subTaskNum : temp)
            tasks.remove(subTaskNum);
    }

    private Task addTask(Task task){
        Task taskToAdd = new Task(seq.getNextSeq() ,task);
        tasks.put(taskToAdd.getId(), taskToAdd);
        return taskToAdd;
    }

    @Override
    public String toString() {
        return "manager.TaskManager{" +
                "tasks.size()=" + getTasks().size() +
                '}';
    }
}
