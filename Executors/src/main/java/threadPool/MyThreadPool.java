package 线程池;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.IntStream;

/**
 * 生产者消费者模式、阻塞队列、线程列表
 *
 * @author Cy
 * @date 2021/6/26 19:12
 */
public class MyThreadPool {
    BlockingQueue<Runnable> taskQueue;
    List<MyThread> threads;

    public MyThreadPool(BlockingQueue<Runnable> taskQueue, int threadSize) {
        this.taskQueue = taskQueue;
        this.threads = new ArrayList<>(threadSize);
        // 初始化线程，定义名称
        // 返回数字流
        IntStream.rangeClosed(1, threadSize).forEach((i) -> {
            MyThread myThread = new MyThread("yse-task-thread-" + i);
            myThread.start();
            threads.add(myThread);
        });
    }

    public void execute(Runnable task) throws InterruptedException{
        taskQueue.put(task);
    }

    // 自定义线程
    class MyThread extends Thread {

        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                Runnable task = null;
                try {
                    task = taskQueue.take();
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(new LinkedBlockingDeque<>(10), 10);
        IntStream.rangeClosed(1,20).forEach((i)->{
            try {
                myThreadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName());
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

