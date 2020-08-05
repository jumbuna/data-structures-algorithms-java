package com.jumbuna.algs;

public class InsertionSort {
    public static  <T extends Comparable<T>> void insertionSort(Object[] array, int i, int j) {
        T val1;
        T val2;
        T val3;
        for(int s = i+1;s <= j; s++) {
            val2 = (T) array[s];
            val3 = (T) array[i];
            if(val3.compareTo(val2) > 0) {
                if (s - i >= 0) System.arraycopy(array, i, array, i + 1, s - i);
                array[i] = val2;
            }else {
                int c = s;
                val1 = (T) array[--c];
                while(val1.compareTo(val2) > 0 && c > 0) {
                    val1 = (T) array[--c];
                }
                if (s - c >= 0) System.arraycopy(array, c, array, c + 1, s - c);
                array[c+1] = val2;
            }
        }
    }
}
