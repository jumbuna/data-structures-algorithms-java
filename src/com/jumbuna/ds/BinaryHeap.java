package com.jumbuna.ds;

//min heap
public class BinaryHeap <T extends Comparable<T>> {
    Vector<T> array;
    private void swim(int i) {
        if(i > 0) {
            int parent = (i-1)/2;
            if(array.elementAt(parent).compareTo(array.elementAt(i)) > 0) {
                swap(parent, i);
                swim(parent);
            }
        }
    }

    private void sink(int i) {
        int leftChildIndex = i*2+1;
        int rightChildIndex = i*2+2;
        int swapChildIndex = i;
        if(leftChildIndex < array.size()) {
            swapChildIndex = array.elementAt(swapChildIndex).compareTo(array.elementAt(leftChildIndex)) > 0 ? leftChildIndex : swapChildIndex;
        }
        if(rightChildIndex < array.size()) {
            swapChildIndex = array.elementAt(swapChildIndex).compareTo(array.elementAt(rightChildIndex)) > 0 ? rightChildIndex : swapChildIndex;
        }
        if(swapChildIndex != i) {
            swap(swapChildIndex, i);
            sink(swapChildIndex);
        }
    }

    private void swap(int i, int j) {
        T temp = array.elementAt(i);
        array.update(i, array.elementAt(j));
        array.update(j, temp);
    }

    public void insert(T element) {
        if(array == null) {
            array = new Vector<>();
        }
        array.insert(element);
        swim(array.size()-1);
    }

    public T remove() {
        T element = array.elementAt(0);
        swap(0, array.size()-1);
        array.removeAt(array.size()-1);
        sink(0);
        return element;
    }

    public T peek() {
        return array.elementAt(0);
    }

    public int size() {
        return array.size();
    }

    public void clear() {
        array.clear();
    }

}
