package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList tasksHistory = new CustomLinkedList();

    @Override
    public void add(Task task) {
        tasksHistory.removeNode(task.getId());
        tasksHistory.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory.getTasks(10);
    }

    @Override
    public void remove(int taskId){
        tasksHistory.removeNode(taskId);
    }
}

class CustomLinkedList {
    HashMap<Integer, Node> nodeMap = new HashMap<>();

    private int size = 0;

    Integer head;
    Integer tail;

    public int getSize() {
        return size;
    }

    public void linkLast(Task task) {
        Node node;
        if(head == null) {
            node = new Node(null, task, null);
            head = task.getId();
            tail = task.getId();
        } else
            node = new Node(nodeMap.get(tail), task, null);

        nodeMap.put(task.getId(), node);
        nodeMap.get(tail).next = node;
        tail = node.data.getId();
        size++;
    }

    public List<Task> getTasks(int elementsCount) {
        List<Task> resultCollection = new ArrayList<>();
        Node buf = nodeMap.get(tail);

        for(int i = 0; i < elementsCount && i < size; i++) {
            resultCollection.add(buf.data);
            buf = buf.prev;
        }

        return resultCollection;
    }

    public void removeNode(int taskId) {
        if(!nodeMap.containsKey(taskId))
            return;

        Node concreteNode = nodeMap.get(taskId);

        if(concreteNode.prev != null)
            concreteNode.prev.next = concreteNode.next;
        else
            head = concreteNode.next.data.getId();

        if(concreteNode.next != null)
            concreteNode.next.prev = concreteNode.prev;
        else
            tail = concreteNode.prev.data.getId();

        nodeMap.remove(taskId);
        size--;
    }
}

