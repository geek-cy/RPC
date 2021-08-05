package test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 14:32
 */
@Slf4j
public class CyclicBarrierTest {
    private static final int threadCount = 50;
    // 需要同步的线程数量
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
        System.out.println("------当线程数达到之后，优先执行，异常就不执行------");
    });

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            Thread.sleep(1000);
            executorService.execute(() -> {
                log.debug("threadnum:" + threadNum + "is ready");
                try {
                    // 多线程在这里阻塞住，除非超过20s，否则达到CyclicBarrier中线程数才会执行
                    cyclicBarrier.await(20000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    e.printStackTrace();
                }
                log.debug("threadNum:" + threadNum + "is Finish");
            });
        }
        executorService.shutdown();
    }
}
