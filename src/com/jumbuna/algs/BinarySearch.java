package com.jumbuna.algs;

public class BinarySearch {
    //iterative
    public static  <T extends Comparable<T>> int binarySearchR(T[] array, T value, int start, int end) {
        if(start < end) {
            int midian = (start +end)/2;
            if(array[midian].equals(value)) {
                return midian;
            }
            if(array[midian].compareTo(value) > 0) {
                return binarySearchR(array, value, start, midian-1);
            }else {
                return binarySearchR(array, value, midian+1, end);
            }
        }
        return array[start].equals(value) ? start : -1;
    }
    //recursive
    public static  <T extends Comparable<T>> int binarySearchI(T[] array, T value, int start, int stop) {
        int midian;
        while(start < stop) {
            midian = (start+stop)/2;
            if(array[midian].equals(value)) {
                return midian;
            }
            if(array[midian].compareTo(value) > 0) {
                stop = midian-1;
            }else {
                start = midian+1;
            }
        }
        return array[start].equals(value) ? start : -1;
    }
}
