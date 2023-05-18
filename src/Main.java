import kv.KVServer;
import manager.HttpTaskManager;
import manager.Managers;
import model.Task;
import server.HttpTaskServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            KVServer server = new KVServer();
            server.start();
            HttpTaskManager taskManager = new HttpTaskManager("http://localhost:8078/", Managers.getGson(), Managers.getDefaultHistory());
            Task task = new Task(null ,"Task", "Task", null, null);
            taskManager.addTask(task);
            taskManager.getAllTasks();
            HttpTaskManager taskManager2 = new HttpTaskManager("http://localhost:8078/", Managers.getGson(), Managers.getDefaultHistory());
            taskManager2.load();
            System.out.println(taskManager2.getTasks());
        } catch(IOException ex) {
            System.out.println("Ошибка запуска KVS");
        }


        try {
            HttpTaskServer httpTaskServer = new HttpTaskServer("fileText.txt");
        } catch (Exception ex) {
            System.out.println("ERRRRRRRROR");
        }

        /*
        System.out.println("Поехали!");
        FileBackedTasksManager inMemoryTaskManager = new FileBackedTasksManager(Managers.getDefaultHistory(), "filereader.txt");
        createTasks(inMemoryTaskManager);
        printTasks(inMemoryTaskManager);

        callTasks(inMemoryTaskManager);
        printTasks(inMemoryTaskManager);

        File file = new File("filereader.txt");
        System.out.println("Считывание из файла");
        FileBackedTasksManager fileBackedTasksManagerSecond = FileBackedTasksManager.loadFromFile(file);
        printTasks(fileBackedTasksManagerSecond); */
    }

    /* static void createTasks(TaskManager inMemoryTaskManager) {
        Task taskA = new Task(null ,"TaskA", "NormalTask");
        Task taskB = new Task(null ,"TaskB", "NormalTask");

        EpicTask epicTaskA = new EpicTask(null ,"EpicTaskA", "EpicTask");
        EpicTask epicTaskB = new EpicTask(null ,"EpicTaskB", "EpicTask");

        SubTask subTaskA = new SubTask(null ,"SubTaskA", "SubTask", epicTaskA.getId());
        SubTask subTaskB = new SubTask(null ,"SubTaskB", "SubTask", epicTaskA.getId());
        SubTask subTaskC = new SubTask(null ,"SubTaskC", "SubTask", epicTaskA.getId());

        inMemoryTaskManager.addTask(taskA);
        inMemoryTaskManager.addTask(taskB);
        inMemoryTaskManager.addEpicTask(epicTaskA);
        inMemoryTaskManager.addEpicTask(epicTaskB);

        for(EpicTask task : inMemoryTaskManager.getEpicTasks())
            if(task.getName().equals("EpicTaskA"))
                epicTaskA = task;

        subTaskA.setEpicTaskId(epicTaskA.getId());
        subTaskB.setEpicTaskId(epicTaskA.getId());
        subTaskC.setEpicTaskId(epicTaskA.getId());

        inMemoryTaskManager.addSubTask(subTaskA);
        inMemoryTaskManager.addSubTask(subTaskB);
        inMemoryTaskManager.addSubTask(subTaskC);
    }

    private static void printTasks(TaskManager inMemoryTaskManager) {
        for(Task task : inMemoryTaskManager.getTasks()) {
            System.out.println(task);
        }
        for(Task task : inMemoryTaskManager.getEpicTasks()) {
            System.out.println(task);
        }
        for(Task task : inMemoryTaskManager.getSubTasks()) {
            System.out.println(task);
        }

        System.out.println("\n" + inMemoryTaskManager + "\n");

        System.out.println("\tHistory");
        for(Task taskb : inMemoryTaskManager.getHistory())
            System.out.println(taskb);
    }

    private static void deleteTasks(TaskManager inMemoryTaskManager) {
        inMemoryTaskManager.removeSubTask(7);
        inMemoryTaskManager.removeEpicTask(3);
    }

    private static void callTasks(TaskManager inMemoryTaskManager) {
        Random rand = new Random();

        for(int i = 0; i < 20; i++){
            int randId = rand.nextInt(7) + 1;

            Task task = inMemoryTaskManager.getTask(randId);

            if(task == null)
                task = inMemoryTaskManager.getSubTask(randId);
            if(task == null)
                task = inMemoryTaskManager.getEpicTask(randId);
            System.out.println(task);

            System.out.println("\tHistory");
            for(Task taskH : inMemoryTaskManager.getHistory())
                System.out.println(taskH);

            System.out.println("\n");
        }
    }*/
}
