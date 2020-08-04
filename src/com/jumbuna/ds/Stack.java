package com.jumbuna.ds;

public class Stack<T> {
    private LinkedList<T> list = new LinkedList<>();

    public void push(T element) {
        list.insertFront(element);
    }

    public T pop() {
        return list.removeFront();
    }

    public T peek() {
        return list.valueAt(0);
    }

    public int size() {
        return list.size();
    }

    public boolean empty() {
        return list.empty();
    }
}
