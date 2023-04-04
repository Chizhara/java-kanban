package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    HashMap<Integer, Node<Task>> nodes = new HashMap<>();

    private final CustomLinkedList<Task> tasksHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        if(nodes.containsKey(task.getId())) {
            remove(task.getId());
        }
        nodes.put(task.getId() ,tasksHistory.linkLast(task));
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory.getTasks(10);
    }

    @Override
    public void remove(int taskId){
        tasksHistory.removeNode(nodes.get(taskId));
    }

    private static class CustomLinkedList<T> {
        private int size = 0;

        private Node<T> head;
        private Node<T> tail;

        public int getSize() {
            return size;
        }

        public Node<T> linkLast(T element) {
            final Node<T> newNode = new Node<>(tail, element, null);

            if (head == null) {
                head = newNode;
            } else {
                tail.setNext(newNode);
            }
            tail = newNode;
            size++;

            return tail;
        }

        public List<T> getTasks(int elementsCount) {
            List<T> resultCollection = new ArrayList<>();
            Node<T> node = tail;
            for(int i = 0; i < elementsCount && i < size; i++) {
                resultCollection.add((T) node.getData());
                node = node.getPrev();
            }

            return resultCollection;
        }

        public void removeNode(Node<T> removedNode) {
            if(removedNode.getPrev() != null)
                removedNode.getPrev().setNext(removedNode.getNext());
            else
                head = removedNode.getNext();

            if(removedNode.getNext() != null)
                removedNode.getNext().setPrev(removedNode.getPrev());
            else
                tail = removedNode.getPrev();
            size--;
        }
    }
}

