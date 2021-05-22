package server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import request.RpcRequest;
import response.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/21 21:15
 */

@Slf4j
public class RpcServer {

    // newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
    // newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
    // newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
    // newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
    // DefaultThreadFactory和PrivilegedThreadFactory
    // DefaultThreadFactory就是创建一个普通的线程，非守护线程，优先级为5。
    // PrivilegedThreadFactory增加了两个特性：ClassLoader和AccessControlContext，从而使运行在此类线程中的任务具有与当前线程相同的访问控制和类加载器。
    private final ExecutorService threadPool;

    public RpcServer() {
        int corePoolSize = 5;
        int maximumPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,workingQueue,threadFactory);
    }

    public void register(Object service,int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            log.info("服务器正在启动");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                log.info("客户端连接:{}",socket.getInetAddress());
                // 开启一个线程执行任务
                threadPool.execute(new WorkerThread(socket,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
@Data
@AllArgsConstructor
class WorkerThread implements Runnable{

    private Socket socket;
    private Object service;

    @Override
    public void run() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest)objectInputStream.readObject();
            // 通过class.getMethod方法，传入方法名和方法参数类型获得Method对象
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            // 传入对象实例和参数调用且获得返回值
            Object returnObject = method.invoke(service, rpcRequest.getParameters());
            objectOutputStream.writeObject(RpcResponse.success(returnObject));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
