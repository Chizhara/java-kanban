import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.HashMap;

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

        inMemoryTaskManager.tryCreateTask(taskA);
        inMemoryTaskManager.tryCreateTask(taskB);
        inMemoryTaskManager.tryCreateTask(epicTaskA);
        inMemoryTaskManager.tryCreateTask(epicTaskB);

        for(Task task : inMemoryTaskManager.getTasks().values())
            if(task.getName().equals("EpicTaskA"))
                epicTaskA = (EpicTask) task;

        subTaskA.setEpicTaskId(epicTaskA.getId());
        subTaskB.setEpicTaskId(epicTaskA.getId());

        inMemoryTaskManager.tryCreateTask(subTaskA);
        inMemoryTaskManager.tryCreateTask(subTaskB);
    }

    private static void printTasks(TaskManager inMemoryTaskManager){
        for(Task task : inMemoryTaskManager.getTasks().values())
            System.out.println(task);

        for(int i :  inMemoryTaskManager.getTasks().keySet())
            System.out.println(i);

        System.out.println("\n" + inMemoryTaskManager + "\n");
    }

    private static void deleteTasks(TaskManager inMemoryTaskManager){
        inMemoryTaskManager.removeTask(7);
        inMemoryTaskManager.removeTask(3);
    }

    private static void changeTasks(TaskManager inMemoryTaskManager) {
        HashMap<Integer, Task> tasks = inMemoryTaskManager.getTasks();

        Task taskA = new Task(tasks.get(1).getId() ,tasks.get(1));
        EpicTask taskB = new EpicTask(tasks.get(3).getId(), (EpicTask) (tasks.get(3)));
        Task taskC = new SubTask(tasks.get(5).getId() ,(SubTask) tasks.get(5));

        taskA.setDescription("ChangedSubTask A");
        taskC.setStatus(TaskStatus.DONE);
        taskB.setName("EpicTaskA Changed");

        SubTask subTask = new SubTask(null ,"SubTaskC", "SubTask", tasks.get(4).getId());

        System.out.println(inMemoryTaskManager.updateTask(taskA));
        System.out.println(inMemoryTaskManager.updateTask(taskB));
        System.out.println(inMemoryTaskManager.updateTask(taskC));
        System.out.println(inMemoryTaskManager.tryCreateTask(subTask));
    }
}
