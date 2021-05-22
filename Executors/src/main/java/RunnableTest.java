import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/22 22:29
 */
public class RunnableTest implements Runnable{

    private String command;

    public RunnableTest(String s){
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"start.time"+new Date());
        processCommand();
        System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
    }

    private void processCommand(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.command;
    }
}

class RunnableDemo {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    public static void main(String[] args) {
        // 通过ThreadPoolExecutor构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());
        for(int i = 0;i < 10;i++){
            // 创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Runnable worker = new RunnableTest(""+i);
            // 执行Runnable
            executor.execute(worker);
        }
        // 终止线程池
        executor.shutdown();
        System.out.println("1"+executor.isShutdown());
        while(true){
            if (executor.isTerminated()) break;
        }
        System.out.println("2"+executor.isTerminated());
        System.out.println("Finished");
    }
}
