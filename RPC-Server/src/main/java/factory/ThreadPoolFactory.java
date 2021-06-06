package factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 创建线程池的工厂
 * @author Cy
 * @date 2021/6/4 21:56
 */
@Slf4j
public class ThreadPoolFactory {
    private static final int CORE_POOL_SIZE = 9;
    private static final int MAXI_MUM_POOL_SIZE = 100;
    private static final Long KEEP_ALIVE_TIME = 60L;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    /**
     * 创建一个普通线程
     */
    public static ExecutorService createDefaultThreadPool(){
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        return new ThreadPoolExecutor(CORE_POOL_SIZE,MAXI_MUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,workQueue,threadFactory);
    }
}
