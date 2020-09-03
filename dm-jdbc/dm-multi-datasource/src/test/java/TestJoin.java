import java.util.HashMap;

public class TestJoin {
    public static void main(String[] args) {
        HashMap a = new HashMap();
        a.put("a", "a");
        HashMap b = new HashMap();
        b.put("a", "b");
        System.out.println(a.equals(b));
    }
}
