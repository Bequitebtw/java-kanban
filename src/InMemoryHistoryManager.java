import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private int nodeLinkedListSize;
    private Node head;
    private Node tail;
    private HashMap<Integer, Node> nodeHashMap = new HashMap<>();

    InMemoryHistoryManager() {
        head = null;
        tail = null;
        nodeLinkedListSize = 0;
    }

    //добавление в конце списка
    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            Node curNode = tail;
            curNode.next = newNode;
            newNode.prev = curNode;
            tail = newNode;
        }
        nodeLinkedListSize++;
    }

    //удаление ноды
    private void removeNode(Node node) {
        Node curNode = head;
        if (head == null && tail == null) { //проверка на null всего списка
            return;
        }
        if (node.data == tail.data) { // проверка что удаляемое знанчение находится в конце списка
            removeLast();
            return;
        }
        if (curNode.data == node.data) { // проверка что удаляемое значение находится в начале списка
            removeFirst();
            return;
        } else {
            while (curNode.next != null) {
                if (curNode.data == node.data) {
                    curNode.prev.next = curNode.next;
                    curNode.next.prev = curNode.prev;
                    return;
                }
                curNode = curNode.next;
            }
        }
        nodeLinkedListSize--;
    }

    //вспомогательные методы для удаления
    private void removeFirst() {
        Node curNode = head;
        curNode.next.prev = null;
        head = curNode.next;
    }

    private void removeLast() {
        Node prevNode = tail.prev;
        prevNode.next = null;
        tail = prevNode;
    }

    //История запросов без дубляжей
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node curNode = head;

        if (curNode != null) {
            tasks.add(curNode.data);
            while (curNode.next != null) {
                curNode = curNode.next;
                tasks.add(curNode.data);
            }
        }

        return tasks;
    }

    @Override
    public void remove(int id) {
        if (nodeHashMap.containsKey(id)) {
            removeNode(nodeHashMap.get(id));
        }
    }

    @Override
    public void add(Task task) {
        if (nodeHashMap.containsKey(task.getId())) {
            remove(task.getId());
            linkLast(task);
            return;
        }
        nodeHashMap.put(task.getId(), new Node(task));
        linkLast(task);
    }

    private class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

}

