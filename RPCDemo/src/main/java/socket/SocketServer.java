package socket;

import enumeration.RpcError;
import exception.RpcException;
import factory.ThreadPoolFactory;
import handler.RequestHandler;
import hook.ShutdownHook;
import registry.NacosServiceRegistry;
import registry.ServiceProviderImpl;
import registry.ServiceRegistry;
import serializer.CommonSerializer;
import server.RpcServer;
import lombok.extern.slf4j.Slf4j;
import registry.ServiceProvider;
import java.io.IOException;
import java.net.InetSocketAddress;
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

    private final ExecutorService threadPool;
    private final RequestHandler requestHandler = new RequestHandler();
    private final ServiceProvider serviceProvider;
    private final ServiceRegistry serviceRegistry;
    private final String host;
    private final int port;
    private CommonSerializer serializer;
    public SocketServer(String host,int port) {
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-NettyServer");
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        this.host = host;
        this.port = port;
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            log.info("服务器正在启动");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                log.info("消费者连接:{}:{}",socket.getInetAddress(),socket.getPort());
                // 开启一个线程，从ServiceRegistry获取提供服务的对象后，将RpcRequest和服务对象直接交给RequestHandler处理
                threadPool.execute(new RequestHandlerThread(socket,requestHandler, serviceRegistry,serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:",e);
        }
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if(serializer == null){
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.register(service);
        serviceRegistry.register(serviceClass.getCanonicalName(),new InetSocketAddress(host,port));
        start();
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}
