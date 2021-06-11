package api;

import annotation.ServiceScan;
import lombok.extern.slf4j.Slf4j;
import server.RpcServer;
import server.netty.NettyServer;


/**
 * @author Cy
 * @date 2021/6/5 10:41
 */
@Slf4j
@ServiceScan
public class NettyServiceTest {
    public static void main(String[] args) throws InterruptedException {
        RpcServer server = new NettyServer("192.168.31.92",8000,0);
//        HelloService helloService = new HelloServiceImpl();
//        server.publishService(helloService, "HelloService");
        server.start();
    }

}
