package 并发编程之美;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Cy
 * @date 2021/6/12 16:55
 */
@Slf4j
public class Test2 {

    @Test
    public void t1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (this) {
                try {
                    log.info("等待");
                    this.wait();
                    log.info("唤醒");
                    this.notify();
                    log.info("线程结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        log.info("线程开始");
        synchronized (this) {
            log.info("拿到锁");
            this.notify();
            this.wait();
            t1.interrupt();
        }
    }

    @Test
    public void t2() throws InterruptedException {
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                log.info("获取中断标志并重置:{}", Thread.interrupted());// 先true后false,因为标志重置了
                log.info("t2开始");
            }
        });
        t2.start();
        t2.interrupt();
        log.info("获取中断标志:{}", t2.isInterrupted());// true
        log.info("获取当前线程的中断标志并重置:{}", Thread.interrupted());// false，因为主线程没标志
        log.info("获取中断标志:{}", t2.isInterrupted());// false 因为标志重置了
        t2.join();
        log.info("主线程结束");
    }

    // 创建资源
    private static final Object a = new Object();
    private static final Object b = new Object();

    @Test
    public void t3() throws InterruptedException {
    Thread t3 = new Thread(() -> {
        List<Integer> list = new ArrayList<>();

        synchronized (a) {
            log.info("get a");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("waiting get b");
            synchronized (b) {
                log.info("get b");
            }
        }
    });

    Thread t4 = new Thread(() -> {
        synchronized (a) {
            log.info("get a");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("waiting get b");
            synchronized (b) {
                log.info("get b");
            }
        }
    });

        t3.start();
        t4.start();
        t3.join();
        t4.join();
    }

}
