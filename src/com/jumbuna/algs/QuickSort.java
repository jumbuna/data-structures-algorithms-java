package com.jumbuna.algs;

public class QuickSort {
    public static <T extends Comparable<T>> void quickSort(T[] array, int start, int stop) {
        if(start < stop) {
            int pivot = stop;
            for(int i = stop-1; i >= 0; i--) {
                if(array[i].compareTo(array[pivot]) > 0) {
                    T temp = array[pivot];
                    array[pivot] = array[i];
                    array[i] = array[pivot-1];
                    array[pivot-1] = temp;
                }
            }
            quickSort(array, start, pivot-1);
            quickSort(array, pivot+1, stop);
        }
    }
}
