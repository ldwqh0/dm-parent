import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CalMd5 {

    @Test
    public void calMd5() throws IOException {
        File file = new File("D:\\Users\\LiDong\\Downloads\\ideaIU-2020.2.2.exe");
        try (FileInputStream fi = new FileInputStream(file)) {
            long s2 = System.currentTimeMillis();
            System.out.println(DigestUtils.md5DigestAsHex(fi));
            System.out.println("耗时" + (System.currentTimeMillis() - s2));
        }
        try (FileInputStream fi = new FileInputStream(file)) {
            long s1 = System.currentTimeMillis();
            System.out.println(org.apache.commons.codec.digest.DigestUtils.md5Hex(fi));
            System.out.println("耗时" + (System.currentTimeMillis() - s1));
        }
    }

    @Test
    public void calSha256() throws IOException {
        File file = new File("D:\\Users\\LiDong\\Downloads\\pdi-ce-9.0.0.0-423(kettle).zip");
        try (FileInputStream fi = new FileInputStream(file)) {
            long s1 = System.currentTimeMillis();
            System.out.println(DigestUtils.md5DigestAsHex(fi));
            System.out.println("耗时" + (System.currentTimeMillis() - s1));
        }
        try (FileInputStream fi = new FileInputStream(file)) {
            long s2 = System.currentTimeMillis();
            System.out.println(org.apache.commons.codec.digest.DigestUtils.sha256Hex(fi));
            System.out.println("耗时" + (System.currentTimeMillis() - s2));
        }
    }

    @Test
    public void testFor() {
        long start = System.currentTimeMillis();
        long sum = 0;
        for (long i = 0; i < 1000000000L; i++) {
            sum += i;
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
