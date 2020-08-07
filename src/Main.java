import com.jumbuna.algs.*;
import com.jumbuna.ds.*;


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
