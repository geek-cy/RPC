package 并发编程之美;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class Test1 {
    public static void main(String[] args) {
        int i = 0;
        // 任务与代码未分离
        T t = new T("Thread-2");
        t.start();
        // 任务与代码分离
        R r = new R(++i);
        new Thread(r).start();
        new Thread(r).start();

        try {
            // FutureTask
            FutureTask<String> futureTask = new FutureTask<>(new C());
            // 启动线程
            new Thread(futureTask).start();
            log.info(futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 优点：获取当前线程直接使用this
 *      方便传参，可以在子类添加成员变量传递
 * 缺点：无法继承其他类,任务与代码未分离，无法拿到返回结果
 */
@Slf4j
class T extends Thread{

    public T(String name) {
        super(name);
    }

    @Override
    public void run() {
        log.info("线程名字:{}",this.getName());
    }
}

/**
 * 缺点：无法拿到返回结果
 */
@Slf4j
class R implements Runnable{

    private int i;

    public R(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        log.info("线程名字:{}",Thread.currentThread().getName()+i++);
    }
}

@Slf4j
class C implements Callable<String>{

    @Override
    public String call() throws Exception {
        return "hello";
    }
}
