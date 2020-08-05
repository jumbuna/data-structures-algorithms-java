package com.jumbuna.ds;

import com.jumbuna.algs.QuickSort;

import java.util.Arrays;
import java.util.Iterator;

public class Vector<T> {
    private int currentIndex;
    private int capacity;
    private int threshold;
    final private double loadFactor = .67;
    T[] array = null;

    public Vector(int capacity) {
        this.capacity = capacity;
        threshold = (int) loadFactor * capacity;
        currentIndex = 0;
    }

    public Vector() {
        this(16);
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * loadFactor);
        T[] newArray = (T[]) new Object[capacity];
        System.arraycopy(array, 0, newArray, 0, currentIndex);
        array = null;
        array = newArray;
    }

    public void insert(T element) {
        if(array == null) {
            array = (T[]) new Object[capacity];
        }
        array[currentIndex++] = element;
        if(currentIndex >= threshold) {
            resize();
        }
    }

    public T elementAt(int i) {
        if(i >= currentIndex | i < 0) {
            throw new IndexOutOfBoundsException();
        }
        return array[i];
    }

    public void remove(T element) {
        int i = indexOf(element);
        if(i != -1) {
            --currentIndex;
            for(int j = i; j < currentIndex; j++) {
                array[j] = array[j+1];
            }
        }
    }

    public void removeAt(int i) {
        if(i < currentIndex) {
            --currentIndex;
            for(int j = i; j < currentIndex; j++) {
                array[j] = array[j+1];
            }
        }
    }

    public int indexOf(T element) {
        for (int i = 0; i < currentIndex; i++) {
            if(array[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    public int size() {
        return currentIndex;
    }

    public int capacity() {
        return capacity;
    }

    public void clear() {
        currentIndex = 0;
        capacity = 16;
        threshold = (int) (capacity * loadFactor);
        array = (T[]) new Object[capacity];
    }

    public void update(int i, T element) {
        if(i < currentIndex) {
            array[i] = element;
        }
    }
}
