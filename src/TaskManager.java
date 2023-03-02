import java.util.ArrayList;
import java.util.HashMap;

import model.*;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private int seq = 0;

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void clearTasks(){
        tasks.clear();
    }

    public Task getTask(int taskId){
        return tasks.get(taskId);
    }

    public boolean updateTask(Task task, Integer taskId){
        if(!tasks.containsKey(taskId))
            return false;
        if(tasks.get(taskId).getTaskType() != task.getTaskType())
            return false;

        Task concreteTask = tasks.get(taskId);

        concreteTask.setName(task.getName());
        concreteTask.setDescription(task.getDescription());
        concreteTask.setStatus(task.getStatus());

        return true;
    }

    public Task removeTask(int taskId){
        Task result = tasks.get(taskId);
        TaskType taskType = result.getTaskType();

        switch(result.getTaskType()){
            case TASK:
                tasks.remove(taskId);
                break;
            case SUB_TASK:
                SubTask subTask = (SubTask)result;

                for(Task task : tasks.values())
                    if(task.equals(subTask))
                        subTask = (SubTask) task;

                subTask.getEpicTask().removeSubTask(subTask);
                tasks.remove(taskId);
                break;
            case EPIC_TASK:
                EpicTask epicTask = (EpicTask)result;
                removeSubTasksOfEpic(epicTask);

                tasks.remove(taskId);
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

    public boolean tryCreateTask(Task task){
        boolean result;

        switch(task.getTaskType()){
            case TASK:
                addTask(new Task(task));
                result = true;
                break;
            case EPIC_TASK:
                addTask(new EpicTask(task));
                result = true;
                break;
            case SUB_TASK:
                result = tryCreateSubTask((SubTask) task);
                break;
            default:
                result = false;
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

    private boolean tryCreateSubTask(SubTask subTask){
        boolean result = false;

        for(Task task : tasks.values())
            if(task.equals(subTask.getEpicTask())){
                EpicTask epicTask = (EpicTask) task;

                SubTask temp = new SubTask(new SubTask(subTask));
                temp.setEpicTask((EpicTask) task);

                epicTask.addSubTask(temp);
                subTask.setEpicTask(epicTask);
                addTask(temp);
                result = true;

                break;
            }

        return result;
    }

    private void addTask(Task task){
        tasks.put(++seq ,task);
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks.size() +
                ", seq=" + seq +
                '}';
    }
}
