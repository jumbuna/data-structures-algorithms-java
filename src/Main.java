import com.jumbuna.ds.HashMap;
import com.jumbuna.ds.Queue;
import com.jumbuna.ds.Stack;
import com.jumbuna.ds.Vector;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.insert("jacob", 30);
        map.insert("jacobo", 90);
        map.insert("jacobu", 70);
        map.insert("jacobi", 6);
        map.insert("jacoba", 3);
        map.insert("jacoba", 55);

        map.remove("jacoba");

        Vector<String> vec = map.keys();
        for (int i = 0; i < vec.size(); i++) {
            System.out.println(map.get(vec.elementAt(i)));
        }
    }
}
