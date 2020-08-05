import com.jumbuna.algs.HeapSort;
import com.jumbuna.algs.InsertionSort;
import com.jumbuna.algs.MergeSort;
import com.jumbuna.ds.HashMap;
import com.jumbuna.ds.Queue;
import com.jumbuna.ds.Stack;
import com.jumbuna.ds.Vector;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Integer[] array = {5, 8, 2, 2, 5, 7, 0, 9, 15, 3, 22, 12, 13, 82, 61, 23, 5, 8, 2, 2, 5, 7, 0, 9, 15, 3, 22, 12, 13, 82, 61, 23, 5, 8, 2, 2, 5, 7, 0, 9, 15, 3, 22, 12, 13, 82, 61, 23, 5, 8, 2, 2, 5, 7, 0, 9, 15, 3, 22, 12, 13, 82, 61, 23, 5, 8, 2, 2, 5, 7, 0, 9, 15, 3, 22, 12, 13, 82, 61, 23, 5, 8, 2, 2, 5, 7, 0, 9, 15, 3, 22, 12, 13, 82, 61, 23, 5, 8, 2, 2, 5, 7, 0, 9, 15, 3, 22, 12, 13, 82, 61, 23, 5, 8, 2, 2, 5, 7, 0, 9, 15, 3, 22, 12, 13, 82, 61, 23};
        long t1 = System.currentTimeMillis();
        HeapSort.heapSort(array);
        System.out.println(Arrays.toString(array));
        System.out.println(array.length);
    }
}
