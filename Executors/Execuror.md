### Executor框架
在Java5后启动线程，能够降低资源消耗、提高响应速度、提高线程的可管理性、有助于避免this逃逸问题（其他线程调用尚未构造完全的对象）

#### 三大结构
##### 任务(Runnable/Callable)
执行任务需要实现Runnable接口或Callable接口，这两接口都可以被ThreadPoolExecutor或ScheduledThreadPoolExecutor执行

##### 任务的执行(Executor)
包括任务执行的核心接口Executor、ExecutorService接口、ThreadPoolExecutor和ScheduledThreadPoolExecutor两个关键类
Executor中只有一个方法: void execute(Runnable command)
![任务执行相关接口](https://cdn.jsdelivr.net/gh/geek-cy/img/并发/640.webp)

##### 异步计算的结果(Future)
Future接口以及其实现类FutureTask类都可以代表异步计算结果
当Runnable接口或Callable接口的实现类提交给ThreadPoolExecutor或ScheduledThreadPoolExecutor执行

#### Executor使用流程
![](https://cdn.jsdelivr.net/gh/geek-cy/img/并发/流程.png)
将创建完成实现了Runnable/Callable接口的对象直接交给ExecutorService执行:execute(Runnable command)

或者将Runnable/Callable对象交给ExecutorService执行submit(Runnable task)/submit(Callable <T> task)

若执行submit方法，会返回一个实现Future接口的对象,submit()会返回一个FutureTask对象，FutureTask也实现了Runnable,因此可以创建FutureTask直接交给ExecutorService执行

最后主线程执行FutureTask.get()方法来等待任务执行，也可以cancel(boolean mayInterruptIfRunning)取消任务执行
如果参数为true并且任务正在运行，那么这个任务将被取消。如果参数为false并且任务正在运行，那么这个任务将不会被取消。

#### ThreadPoolExecutor类
![](https://cdn.jsdelivr.net/gh/geek-cy/img/并发/7760784b1f5d6425202fcc50caa98adb.png)
##### 参数
1、int corePoolSize:定义最小可同时运行线程数量
2、int maximumPoolSize:当队列中存放的任务达到队列容量时，当前可同时运行的最大线程数
3、BlockingQueue<Runnable> workQueue:当新任务来时会先判断当前运行线程数是否达到corePoolSize，若达到就会存放在队列中
4、long KeepAliveTime:当线程池中线程数量大于corePoolSize时，此时没有新的任务提交，核心线程外的线程不会立即销毁而是等待时间超过了keepAliveTime才会被回收销毁
5、TimeUnit unit:KeepAliveTime的时间单位
6、ThreadFactory threadFactory:创建新线程用
7、RejectedExecutionHandler handler:拒接策略,当前运行线程数达到maximumPoolSize且workQueue满了

##### 拒接策略
ThreadPoolTaskExecutor定义了一些策略
1、(默认)AbortPolicy:抛出RejectedExecutionException拒绝新任务处理
2、CallerRunsPolicy:调用当前线程池所在的线程运行,会阻塞主线程,对于可伸缩的应用程序建议使用
3、DiscardPolicy:不处理新任务，直接丢弃,不会抛异常
4、DiscardOldestPolicy:丢弃最早未处理的任务请求

##### 推荐使用 ThreadPoolExecutor 构造函数创建线程池
![](https://cdn.jsdelivr.net/gh/geek-cy/img/并发/7760784b1f5d6425202fcc50caa98adb.png)

Executors返回线程对象弊端有两点：
- FixedThreadPool和SingleThreadExecutor:允许请求的队列长度为Integer.MAX_VALUE，可能堆积大量请求导致OOM
- CachedThreadPool和ScheduledThreadPool:允许创建线程数量为Integer.MAX_VALUE,可能会创建大量线程导致OOM

#### 自带的线程池
##### 1、FixedThreadPool
```
// FixedThreadPool源码
public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L, TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>(),
                                  threadFactory);
}
```
FixedThreadPoll为可重用固定线程数的线程池，corePoolSize和maximumPoolSize被设置为 nThreads,由我们使用时候传递
线程池中线程执行完手头任务后会在循环中反复从LinkedBlockingQueue中获取任务来执行
使用了LinkedBlockingQueue（无界队列），此时maximumPoolSize将无效（因为任务队列不可能满），keepAliveTime也无效，也不会拒绝任务，从而导致OOM

##### 2、SingleThreadExecutor
```
// SingleThreadExecutor源码
public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>(),
                                threadFactory));
}
```
当线程池有一个运行的线程后，将任务加入LinkedBlockingQueue，线程执行完当前任务后会在循环中反复从LinkedBlockingQueue中获取任务来执行
同样也使用了无界队列LinkedBlockingQueue，与FixedThreadPoll原因相同

##### 3、CachedThreadPool
```
public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                  60L, TimeUnit.SECONDS,
                                  new SynchronousQueue<Runnable>(),
                                  threadFactory);
}
```
可以看到corePoolSize置空，而maximumPoolSize被设置为Integer.MAX.VALUE,因此也是无界的。若主线程提交速度高于maximumPool中线程处理任务速度，则会耗尽cpu和内存资源
![](https://cdn.jsdelivr.net/gh/geek-cy/img/并发/SynchronousQueue.png)
其中1为SynchronousQueue.offer(Runnable task)，2为execute(),3为SynchronousQueue.poll(keepAliveTime,TimeUnit.NANOSECONDS)
若当前有闲线程在执行3，主线程执行1与空闲线程配对成功，主线程把任务交给空闲线程执行2方法。
若初始maximumPool为空或maximumPool无空闲线程时，将没有线程执行3,此时1将失败，CachedThreadPool会创建新线程执行任务，2方法执行完成

##### 4、ScheduledThreadPoolExecutor
用来定期执行任务，使用较少，推荐使用Quartz
#### Executor运行原理
```
 // 存放线程池的运行状态 (runState) 和线程池内有效线程的数量 (workerCount)
 private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    private final BlockingQueue<Runnable> workQueue;

    public void execute(Runnable command) {
        // 如果任务为null，则抛出异常。
        if (command == null)
            throw new NullPointerException();
        // ctl 中保存的线程池当前的一些状态信息
        int c = ctl.get();

        //  下面会涉及到 3 步 操作
        // 1.首先判断当前线程池中之行的任务数量是否小于 corePoolSize
        // 如果小于的话，通过addWorker(command, true)新建一个线程，并将任务(command)添加到该线程中；然后，启动该线程从而执行任务。
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        // 2.如果当前之行的任务数量大于等于 corePoolSize 的时候就会走到这里
        // 通过 isRunning 方法判断线程池状态，线程池处于 RUNNING 状态才会被并且队列可以加入任务，该任务才会被加入进去
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            // 再次获取线程池状态，如果线程池状态不是 RUNNING 状态就需要从任务队列中移除任务，并尝试判断线程是否全部执行完毕。同时执行拒绝策略。
            if (!isRunning(recheck) && remove(command))
                reject(command);
                // 如果当前线程池为空就新创建一个线程并执行。
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
        // 3. 通过addWorker(command, false)新建一个线程，并将任务(command)添加到该线程中；然后，启动该线程从而执行任务。
        // 如果addWorker(command, false)执行失败，则通过reject()执行相应的拒绝策略的内容。
        else if (!addWorker(command, false))
            reject(command);
    }
```
![线程池实现原理](https://cdn.jsdelivr.net/gh/geek-cy/img/并发/线程池实现原理.png)

#### 几个常见的对比
##### Runnable vs Callable
Runnable自Java1.0以来一直存在，Callable在Java1.5中引入，目的就是处理Runnable不支持的用例
Runnable接口不会返回结果或抛出异常，而Callable接口可以

##### execute() vs submit()
execute()方法用于提交不需要返回值的任务，所以无法判断任务是否被线程池执行成功与否；
submit()方法用于提交需要返回值的任务。线程池会返回一个 Future 类型的对象，通过这个 Future 对象可以判断任务是否执行成功，并且可以通过 Future 的 get()方法来获取返回值，get()方法会阻塞当前线程直到任务完成，而使用 get（long timeout，TimeUnit unit）方法则会阻塞当前线程一段时间后立即返回，这时候有可能任务没有执行完。

##### shutdown() vs shutdownNow()
shutdown():关闭线程池，线程池状态为SHUTDOWN不再接受新任务但队列里的任务得执行完毕
shutdownNow():关闭线程池，线程池状态为STOP,线程池终止当前正在运行的任务，并停止处理排队的任务并返回正在等待执行的List

##### isTerminated() vs isShutdown()
isTerminated当调用shutdown()后提交任务完成后返回true，否则返回false
isShutDown当调用shutdown()后返回为true

