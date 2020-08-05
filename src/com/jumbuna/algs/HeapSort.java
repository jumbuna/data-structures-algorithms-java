package com.jumbuna.algs;

public class HeapSort {
    public static <T extends Comparable<T>> void heapSort(Object[] array) {
        int parent = (array.length - 2)/2;
        int size = array.length;
        for(; parent >= 0; parent--) {
            heapify(array, parent, size);
        }
        swap(array, 0, --size);
        while(size > 1) {
            heapify(array, 0, size);
            swap(array, 0, --size);
        }
    }
    private static <T extends Comparable<T>> void heapify(Object[] array, int i, int size) {
        int max = i;
        int leftIndex = i*2 +1;
        int rightIndex = i*2 +2;
        T maxValue = (T) array[max];
        T candidate;
        if(leftIndex < size) {
            candidate = (T) array[leftIndex];
            max = candidate.compareTo(maxValue) > 0 ? leftIndex : max;
            maxValue =  (T) array[max];
        }
        if(rightIndex < size) {
            candidate = (T) array[rightIndex];
            max = candidate.compareTo(maxValue) > 0 ? rightIndex : max;
            maxValue = (T) array[max];
        }
        if(max != i) {
            swap(array, max, i);
            heapify(array, max, size);
        }
    }
    private static void swap(Object[] array, int i, int j) {
        Object temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}