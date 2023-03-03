import manager.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        createTasks(taskManager);
        printTasks(taskManager);
        changeTasks(taskManager);
        printTasks(taskManager);
        deleteTasks(taskManager);
        printTasks(taskManager);

        taskManager.clearTasks();
        printTasks(taskManager);
    }

    private static void createTasks(TaskManager taskManager){
        Task taskA = new Task(null ,"TaskA", "NormalTask");
        Task taskB = new Task(null ,"TaskB", "NormalTask");
        taskB.setStatus(TaskStatus.DONE);

        EpicTask epicTaskA = new EpicTask(null ,"EpicTaskA", "EpicTask");
        EpicTask epicTaskB = new EpicTask(null ,"EpicTaskB", "EpicTask");

        SubTask subTaskA = new SubTask(null ,"SubTaskA", "SubTask", epicTaskA);
        SubTask subTaskB = new SubTask(null ,"SubTaskB", "SubTask", epicTaskA);

        taskManager.tryCreateTask(taskA);
        taskManager.tryCreateTask(taskB);
        taskManager.tryCreateTask(epicTaskA);
        taskManager.tryCreateTask(epicTaskB);

        for(Task task : taskManager.getTasks().values())
            if(task.getName().equals("EpicTaskA"))
                epicTaskA = (EpicTask) task;

        subTaskA.setEpicTask(epicTaskA);
        subTaskB.setEpicTask(epicTaskA);

        taskManager.tryCreateTask(subTaskA);
        taskManager.tryCreateTask(subTaskB);
    }

    private static void printTasks(TaskManager taskManager){
        for(Task task : taskManager.getTasks().values())
            System.out.println(task);

        for(int i :  taskManager.getTasks().keySet())
            System.out.println(i);

        System.out.println("\n" + taskManager + "\n");
    }

    private static void deleteTasks(TaskManager taskManager){
        taskManager.removeTask(7);
        taskManager.removeTask(3);
    }

    private static void changeTasks(TaskManager taskManager) {
        HashMap<Integer, Task> tasks = taskManager.getTasks();

        Task taskA = new Task(tasks.get(1).getId() ,tasks.get(1));
        EpicTask taskB = new EpicTask(tasks.get(3).getId(), (EpicTask) (tasks.get(3)));
        Task taskC = new SubTask(tasks.get(5).getId() ,(SubTask) tasks.get(5));

        taskA.setDescription("ChangedSubTask A");
        taskC.setStatus(TaskStatus.DONE);
        taskB.setName("EpicTaskA Changed");

        SubTask subTask = new SubTask(null ,"SubTaskC", "SubTask", (EpicTask) tasks.get(4));

        System.out.println(taskManager.updateTask(taskA));
        System.out.println(taskManager.updateTask(taskB));
        System.out.println(taskManager.updateTask(taskC));
        System.out.println(taskManager.tryCreateTask(subTask));
    }
}
