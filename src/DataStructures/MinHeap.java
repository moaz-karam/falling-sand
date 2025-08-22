package DataStructures;

import java.util.ArrayList;

public class MinHeap<T> {
    private ArrayList<Node> items;
    private int size;

    class Node {
        T item;
        double priority;

        private Node(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }
        private double getPriority() {
            return priority;
        }
        private T getItem() {
            return item;
        }
    }

    public MinHeap() {
        items = new ArrayList<>();
        size = 0;
    }

    private int getParent(int i) {
        return (int) (0.5 * (i - 1));
    }

    private int comparePriority(int i, int n) {
        if (items.get(i).getPriority() > items.get(n).getPriority()) {
            return 1;
        }
        else if (items.get(i).getPriority() < items.get(n).getPriority()) {
            return -1;
        }
        return 0;
    }

    private void swap(int i, int n) {
        Node temp = items.get(i);
        items.set(i, items.get(n));
        items.set(n, temp);
    }

    private int getSmallestChild(int p) {
        int rightChild =  2 * p + 2;
        int leftChild = 2 * p + 1;
        if (rightChild < size && leftChild < size) {
            if (items.get(leftChild).getPriority() > items.get(rightChild).getPriority()) {
                return rightChild;
            }
            return leftChild;
        }
        else if (leftChild < size) {
            return leftChild;
        }
        return 0;
    }

    public void add(T item, double p) {
        Node node = new Node(item, p);
        items.add(size, node);

        int nodeIndex = size;
        int parent = getParent(nodeIndex);
        while(comparePriority(parent, nodeIndex) == 1) {
            swap(parent, nodeIndex);
            nodeIndex = parent;
            parent = getParent(nodeIndex);
        }
        size += 1;
    }

    public T removeSmallest() {
        if (size <= 0) {
            throw new IllegalArgumentException("You can not remove a from a heap of size 0");
        }
        T smallest = items.get(0).getItem();
        items.set(0, null);
        items.set(0, items.get(size - 1));
        int addedRoot = 0;
        int smallestChild = getSmallestChild(addedRoot);

        while (smallestChild != 0 && comparePriority(addedRoot, smallestChild) == 1) {

            swap(addedRoot, smallestChild);
            addedRoot = smallestChild;
            smallestChild = getSmallestChild(addedRoot);
        }
        size -= 1;
        return smallest;
    }

    public T getSmallest() {
        if (size > 0) {
            return items.get(0).getItem();
        }
        return null;
    }

    public double getSmallestPriority() {
        return items.get(0).getPriority();
    }

    public int getSize() {
        return size;
    }
}
