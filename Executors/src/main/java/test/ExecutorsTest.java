package test;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

//一个线程池中的线程异常了，那么线程池会怎么处理这个线程?
public class ExecutorsTest {

    public static void main(String[] args) {
        ThreadPoolTaskExecutor executorService = buildThreadPoolTaskExecutor();
        executorService.execute(() -> sayHi("execute3"));//execute堆栈异常输出
        Future<?> future = executorService.submit(() -> sayHi("submit"));//submit堆栈异常未输出
        /*try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }*/
        future.cancel(true);

        /*ExecutorService service = buildThreadPoolExecutor();
        service.execute(() -> sayHi("execute"));
        executorService.execute(() -> sayHi("execute"));
        executorService.execute(() -> sayHi("execute"));
        executorService.execute(() -> sayHi("execute"));
        executorService.execute(() -> sayHi("execute"));*/
    }

    private static void sayHi(String name) {
        String printStr = "【thread-name:" + Thread.currentThread().getName() + ",执行方式:" + name + "】";
        if ("execute".equals(name)) {
            System.out.println(printStr);
        } else {
            throw new RuntimeException(printStr + ",我异常啦!哈哈哈!");
        }
    }

    private static ThreadPoolTaskExecutor buildThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executorService = new ThreadPoolTaskExecutor();
        executorService.setThreadNamePrefix("(公众号-why技术)-");
        executorService.setCorePoolSize(5);
        executorService.setMaxPoolSize(10);
        executorService.setQueueCapacity(1000);
        executorService.setKeepAliveSeconds(30);
        executorService.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executorService.initialize();
        return executorService;
    }

    // 重写uncaughtException方法的两种形式
    private static Thread buildThread() {
        // 使用线程时
        Thread t = new Thread();
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("根据业务场景做你想做的");
            }
        });
        return t;
    }

    private static ExecutorService buildThreadPoolExecutor() {
        //线程池的时候:
        ExecutorService threadPool = Executors.newFixedThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(
                    (t1, e) -> System.out.println("根据业务场景，做你想做的:"));
            return t;
        });
        return threadPool;
    }
}