package xor;

/**
 * @author Cy
 * @date 2021/7/8 13:18
 */
public class xorTest {

    public static void main(String[] args) {
        int i = 1 ^ 4 ^ 1 ^ 6;
        String s3 = Integer.toBinaryString(i);
        System.out.println(s3);
    }
}
