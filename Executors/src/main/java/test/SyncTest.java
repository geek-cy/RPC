package test;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/26 21:31
 */
public class SyncTest {

    @Test
    public void test() {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            threadPool.execute(this::SyncSolution);
        }
    }

    public void SyncSolution() {
        synchronized (this) { // 给定对象的锁
            System.out.println("开始");
            System.out.println("测试");
            System.out.println("结束");
        }
    }

    public void solution() {
        synchronized (SyncTest.class) {
            System.out.println("开始");
            System.out.println("测试");
            System.out.println("结束");
        }
    }
}
