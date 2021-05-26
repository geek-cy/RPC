package socket.server;

import socket.handler.RequestHandler;
import socket.handler.RequestHandlerThread;
import lombok.extern.slf4j.Slf4j;
import registry.ServiceRegistry;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/21 21:15
 */

@Slf4j
public class SocketServer implements RpcServer {
    // DefaultThreadFactory和PrivilegedThreadFactory
    // DefaultThreadFactory就是创建一个普通的线程，非守护线程，优先级为5。
    // PrivilegedThreadFactory增加了两个特性：ClassLoader和AccessControlContext，从而使运行在此类线程中的任务具有与当前线程相同的访问控制和类加载器。
    private final ExecutorService threadPool;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;

    public SocketServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,workingQueue,threadFactory);
    }

    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            log.info("服务器正在启动");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                log.info("消费者连接:{}:{}",socket.getInetAddress(),socket.getPort());
                // 开启一个线程，从ServiceRegistry获取提供服务的对象后，将RpcRequest和服务对象直接交给RequestHandler处理
                threadPool.execute(new RequestHandlerThread(socket,requestHandler,serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:",e);
        }
    }
}
