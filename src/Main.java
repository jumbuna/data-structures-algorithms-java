import com.jumbuna.ds.Queue;
import com.jumbuna.ds.Stack;
import com.jumbuna.ds.Vector;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>();
        vector.insert(10);
        vector.insert(20);
        vector.insert(30);
        vector.insert(40);
        vector.insert(50);

        for(int i = 0; i < vector.size(); i++) {
            System.out.println(vector.elementAt(i));
        }
    }
}
