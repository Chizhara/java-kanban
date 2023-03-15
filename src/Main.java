import manager.Managers;
import manager.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager inMemoryTaskManager = Managers.getDefault();
        createTasks(inMemoryTaskManager);
        printTasks(inMemoryTaskManager);
        changeTasks(inMemoryTaskManager);
        printTasks(inMemoryTaskManager);
        deleteTasks(inMemoryTaskManager);
        printTasks(inMemoryTaskManager);

        inMemoryTaskManager.clearTasks();
        inMemoryTaskManager.clearEpicTasks();

        printTasks(inMemoryTaskManager);

        for(Task task : inMemoryTaskManager.getHistory())
            System.out.println(task);

    }

    private static void createTasks(TaskManager inMemoryTaskManager){
        Task taskA = new Task(null ,"TaskA", "NormalTask");
        Task taskB = new Task(null ,"TaskB", "NormalTask");
        taskB.setStatus(TaskStatus.DONE);

        EpicTask epicTaskA = new EpicTask(null ,"EpicTaskA", "EpicTask");
        EpicTask epicTaskB = new EpicTask(null ,"EpicTaskB", "EpicTask");

        SubTask subTaskA = new SubTask(null ,"SubTaskA", "SubTask", epicTaskA.getId());
        SubTask subTaskB = new SubTask(null ,"SubTaskB", "SubTask", epicTaskA.getId());

        inMemoryTaskManager.addTask(taskA);
        inMemoryTaskManager.addTask(taskB);
        inMemoryTaskManager.addEpicTask(epicTaskA);
        inMemoryTaskManager.addEpicTask(epicTaskB);

        for(Task task : inMemoryTaskManager.getTasks())
            if(task.getName().equals("EpicTaskA"))
                epicTaskA = (EpicTask) task;

        subTaskA.setEpicTaskId(epicTaskA.getId());
        subTaskB.setEpicTaskId(epicTaskA.getId());

        inMemoryTaskManager.addSubTask(subTaskA);
        inMemoryTaskManager.addSubTask(subTaskB);
    }

    private static void printTasks(TaskManager inMemoryTaskManager){
        for(Task task : inMemoryTaskManager.getTasks()){
            System.out.println(task);
        }
        for(Task task : inMemoryTaskManager.getEpicTasks()){
            System.out.println(task);
        }
        for(Task task : inMemoryTaskManager.getSubTasks()){
            System.out.println(task);
        }

        System.out.println("\n" + inMemoryTaskManager + "\n");
    }

    private static void deleteTasks(TaskManager inMemoryTaskManager){
        inMemoryTaskManager.removeSubTask(7);
        inMemoryTaskManager.removeEpicTask(3);
    }

    private static void changeTasks(TaskManager inMemoryTaskManager) {
        ArrayList<Task> tasks = inMemoryTaskManager.getTasks();
        tasks.addAll(inMemoryTaskManager.getEpicTasks());
        tasks.addAll(inMemoryTaskManager.getSubTasks());

        Task taskA = new Task(tasks.get(0).getId(), tasks.get(0));
        EpicTask taskB = new EpicTask(tasks.get(2).getId(), (EpicTask) (tasks.get(2)));
        Task taskC = new SubTask(tasks.get(4).getId(), (SubTask) tasks.get(4));

        taskA.setDescription("ChangedSubTask A");
        taskC.setStatus(TaskStatus.DONE);
        taskB.setName("EpicTaskA Changed");

        SubTask subTask = new SubTask(null ,"SubTaskC", "SubTask", tasks.get(3).getId());

        System.out.println(inMemoryTaskManager.updateTask(taskA));
        System.out.println(inMemoryTaskManager.updateTask(taskB));
        System.out.println(inMemoryTaskManager.updateTask(taskC));
        System.out.println(inMemoryTaskManager.addSubTask(subTask));
    }
}
