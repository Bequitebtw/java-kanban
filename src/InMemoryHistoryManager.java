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
    private Node linkLast(Node newNode) {
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
        return newNode;
    }

    //удаление ноды
    private void removeNode(Node node) {
        /*  проверка на запрос одного и того же таска подряд, когда он единственный в LinkedList, так как у него нет
         *  указателей prev и next, мы присваиваем head и tail значения null для удаления из списка
         */
        if (nodeLinkedListSize == 1) {
            tail = null;
            head = null;
            nodeLinkedListSize--;
            return;
        }
        if (head == null) { // проверка на null всего списка
            return;
        }
        if (node.data == tail.data) { // проверка что удаляемое знанчение находится в конце списка
            removeLast();
            return;
        }
        if (node.data == head.data) { // проверка что удаляемое значение находится в начале списка
            removeFirst();
            return;
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
        nodeLinkedListSize--;
    }

    //вспомогательные методы для удаления
    private void removeFirst() {
        Node curNode = head;
        curNode.next.prev = null;
        head = curNode.next;
        nodeLinkedListSize--;
    }

    private void removeLast() {
        Node prevNode = tail.prev;
        prevNode.next = null;
        tail = prevNode;
        nodeLinkedListSize--;
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
        if(nodeHashMap.containsKey(id)){
            removeNode(nodeHashMap.get(id));
            nodeHashMap.remove(id);
        }
    }

    @Override
    public void add(Task task) {
        Node newNode = new Node(task);
        if (nodeHashMap.containsKey(task.getId())) {
            removeNode(nodeHashMap.get(task.getId())); // удаление из linkedList
            nodeHashMap.put(task.getId(), linkLast(newNode)); // замена связей в hashMap для быстрого удаления из LinkedList
            return;
        }
        nodeHashMap.put(task.getId(), linkLast(newNode));
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

