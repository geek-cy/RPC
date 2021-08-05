/**
 * @author Cy
 * @date 2021/6/29 23:03
 */
public class ThreadLocalTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();
        threadLocal.set(1);
        Thread t = new Thread(()->{
            System.out.println(threadLocal.get());
        });
        t.start();
        t.join();
    }
}
