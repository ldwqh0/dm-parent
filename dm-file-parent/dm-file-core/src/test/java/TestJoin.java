
import org.apache.commons.lang3.StringUtils;

public class TestJoin {
    public static void main(String[] args) {
        join("1", "2");
//        Arrays.asList(null);
    }

    static void join(String... path) {
        System.out.println(StringUtils.join(path, ","));
    }
}
