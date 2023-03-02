import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
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
        Task taskA = new Task("TaskA", "NormalTask");
        Task taskB = new Task("TaskB", "NormalTask");
        taskB.setStatus(TaskStatus.DONE);

        EpicTask epicTaskA = new EpicTask("EpicTaskA", "EpicTask");
        EpicTask epicTaskB = new EpicTask("EpicTaskB", "EpicTask");

        SubTask subTaskA = new SubTask("SubTaskA", "SubTask", epicTaskA);
        SubTask subTaskB = new SubTask("SubTaskB", "SubTask", epicTaskA);

        System.out.println(taskManager.tryCreateTask(taskA));
        System.out.println(taskManager.tryCreateTask(taskB));
        System.out.println(taskManager.tryCreateTask(epicTaskA));
        System.out.println(taskManager.tryCreateTask(epicTaskB));
        System.out.println(taskManager.tryCreateTask(subTaskA));

        for(Task task : taskManager.getTasks().values())
            if(task.getName().equals("EpicTaskA"))
                epicTaskA = (EpicTask) task;

        subTaskB.setEpicTask(epicTaskA);

        System.out.println(taskManager.tryCreateTask(subTaskB));
    }

    private static void printTasks(TaskManager taskManager){
        for(Task task : taskManager.getTasks().values())
            System.out.println(task);

        System.out.println("\n" + taskManager + "\n");
    }

    private static void deleteTasks(TaskManager taskManager){
        taskManager.removeTask(7);
        taskManager.removeTask(3);
    }

    private static void changeTasks(TaskManager taskManager) {
        HashMap<Integer, Task> tasks = taskManager.getTasks();

        Task taskA = new Task(tasks.get(1));
        EpicTask taskB = new EpicTask((EpicTask) (tasks.get(3)));
        Task taskC = new SubTask((SubTask) tasks.get(5));

        taskA.setDescription("ChangedSubTask A");
        taskC.setStatus(TaskStatus.DONE);
        taskB.setName("EpicTaskA Changed");

        SubTask subTask = new SubTask("SubTaskC", "SubTask", (EpicTask) tasks.get(4));

        System.out.println(taskManager.updateTask(taskA, 1));
        System.out.println(taskManager.updateTask(taskB, 3));
        System.out.println(taskManager.updateTask(taskC, 5));
        System.out.println(taskManager.tryCreateTask(subTask));
        System.out.println(taskA);
    }
}
