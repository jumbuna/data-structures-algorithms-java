package com.jumbuna.algs;

public class MergeSort {

    public static <T extends Comparable<T>> void mergeSort (Object[] array, int i, int j) {
        if(i < j) {
            int mid = (i+j)/2;
            mergeSort(array, i, mid);
            mergeSort(array, mid+1, j);
            merge(array, i, j, mid);
        }
    }
    private static <T extends Comparable<T>> void merge(Object[] array, int i, int j, int mid) {
        int leftArraySize = mid+1-i;
        int rightArraySize = j - mid;
        Object[] leftArray = new Object[leftArraySize];
        Object[] rightArray = new Object[rightArraySize];
        System.arraycopy(array, i, leftArray, 0, leftArraySize);
        System.arraycopy(array, mid+1, rightArray, 0, rightArraySize);
        int leftIndex = 0;
        int rightIndex = 0;
        T lv;
        T rv;
        while(leftIndex < leftArraySize && rightIndex < rightArraySize) {
            lv = (T) leftArray[leftIndex];
            rv = (T) rightArray[rightIndex];
            if(lv.compareTo(rv) <= 0) {
                array[i++] = leftArray[leftIndex++];
            }else {
                array[i++] = rightArray[rightIndex++];
            }
        }
        while(leftIndex != leftArraySize) {
            array[i++] = leftArray[leftIndex++];
        }
        while(rightIndex != rightArraySize) {
            array[i++] = rightArray[rightIndex++];
        }
    }
}
