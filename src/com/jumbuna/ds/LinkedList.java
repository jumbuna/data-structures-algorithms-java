package com.jumbuna.ds;

public class LinkedList<T> {
    private class Node {
        Node(T element, Node n) {
            data = element;
            next = n;
        }
        T data;
        Node next;
    }
    private Node head = null;
    private Node tail = null;
    private int nodeCount = 0;

    public void insertFront(T element) {
        if(element != null) {
            if(nodeCount == 0) {
                head = tail = new Node(element, null);
            }else {
                head = new Node(element, head);
            }
            ++nodeCount;
        }
    }

    public void insertBack(T element) {
        if(element != null) {
            if(nodeCount == 0) {
                head = tail = new Node(element, null);
            }else {
                tail.next = new Node(element, null);
                tail = tail.next;
            }
            ++nodeCount;
        }
    }

    public T removeFront() {
        if(nodeCount == 0) {
            throw new IndexOutOfBoundsException("list is empty");
        }
        T temp = head.data;
        head = head.next;
        --nodeCount;
        return temp;
    }

    public T removeBack() {
        if(nodeCount == 0) {
            throw new IndexOutOfBoundsException("list is empty");
        }
        Node t = head;
        while(!t.next.equals(tail)) {
            t = t.next;
        }
        T temp = tail.data;
        t.next = null;
        tail = t;
        --nodeCount;
        return temp;
    }

    public T valueAt(int i) {
        if(i >= nodeCount) {
            throw new IndexOutOfBoundsException("max index is "+i);
        }
        Node temp = head;
        for(int j = 0; j != i; j++) {
            temp = temp.next;
        }
        return temp.data;
    }

    public int size() {
        return nodeCount;
    }

    public boolean empty() {
        return nodeCount == 0;
    }
}
