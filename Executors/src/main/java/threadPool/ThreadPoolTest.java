package threadPool;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Cy
 * @date 2021/6/19 18:16
 */
public class ThreadPoolTest {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    public static void main(String[] args) throws InterruptedException {
        // 通过ThreadPoolExecutor构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setCorePoolSize(8);
        executor.prestartAllCoreThreads();
        executor.allowCoreThreadTimeOut(true);
        Thread.sleep(800);
        System.out.println("当前运行的线程数:"+executor.getPoolSize());
        for(int i = 0;i < 10;i++){
            // 创建工作线程
            Runnable worker = new MyRunnable();
            executor.execute(worker);
        }
        executor.shutdown();
        while(executor.isTerminated()){
            System.out.println("所有线程均完成任务");
        }
    }
    @Slf4j
    private static class MyRunnable implements Runnable{
        @SneakyThrows
        @Override
        public void run() {
            Thread.sleep(1000);
            System.out.println();
            log.info("开始执行任务");
        }
    }
}
