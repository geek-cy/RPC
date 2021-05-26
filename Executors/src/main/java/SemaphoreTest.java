

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Description 需要一次性拿一个许可的情况
 * @Author Cy
 * @Date 2021/5/24 13:41
 */
@Slf4j
public class SemaphoreTest {
    // 请求数量
    private static final int threadCount = 550;

    public static void main(String[] args) {
        // 创建一个具有固定线程数量的线程池对象
        ExecutorService service = Executors.newFixedThreadPool(300);
        // 一次只允许执行的线程数量
        final Semaphore semaphore = new Semaphore(100);

        for(int i = 0;i < threadCount;i++){
            final int threadNum = i;
            service.execute(new Runnable(){
                @Override
                public void run() {
                    try {
                        // 获取一个许可，阻塞直到有许可
                        semaphore.acquire(10);// 100/10,只允许10个线程运行
                        Thread.sleep(1000);
                        log.debug("threadNum:"+threadNum);
                        Thread.sleep(1000);
                        // 释放许可
                        semaphore.release(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        service.shutdown();
        log.info("finish");
    }
}
