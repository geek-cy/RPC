import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 13:55
 */
@Slf4j
public class CountDownLatchTest {
    private static final int threadCount = 50;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(5);
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            log.info("start");
            service.execute(() -> {
                try {
                    Thread.sleep(1000);
                    log.debug("threadnum:" + threadNum);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 一个请求完成
                    log.info("请求完成" + threadNum);
                    countDownLatch.countDown();
                }
            });
        }
        // 主线程阻塞住等countDownLatch计数器为0
        countDownLatch.await();
        service.shutdown();
        log.info("finish");
    }

    @Test
    public void test() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(5);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            service.execute(() -> {
                try {
                    // 多线程阻塞住
                    countDownLatch.await();
                    Thread.sleep(1000);
                    log.debug("threadnum:" + finalI);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        // 主线程这里计数器减为0
        countDownLatch.countDown();
        log.info("finish");
        Thread.sleep(2000);
    }
}