package factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 创建线程池的工厂
 * @Author Cy
 * @Date 2021/6/3 15:09
 */
public class ThreadPoolFactory {

    private static final int CORE_POOL_SIZE = 9;
    private static final int MAXI_MUM_POOL_SIZE = 100;
    private static final Long KEEP_ALIVE_TIME = 60L;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    /**
     * 创建非守护线程
     */
    public static ExecutorService createDefaultThreadPool(String threadNamePrefix){
        return createDefaultThreadPool(threadNamePrefix,false);
    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix,Boolean daemon){
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE,MAXI_MUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,workQueue,threadFactory);
    }

    // DefaultThreadFactory和PrivilegedThreadFactory
    // DefaultThreadFactory就是创建一个普通的线程，非守护线程，优先级为5。
    // PrivilegedThreadFactory增加了两个特性：ClassLoader和AccessControlContext，从而使运行在此类线程中的任务具有与当前线程相同的访问控制和类加载器。
    public static ThreadFactory createThreadFactory(String threadNamePrefix,Boolean daemon){
        if(threadNamePrefix != null){
            if(daemon != null){
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(daemon).build();
            } else return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
        }
        return Executors.defaultThreadFactory();
    }
}
