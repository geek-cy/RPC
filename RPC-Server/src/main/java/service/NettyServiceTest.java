package service;

import annotation.ServiceScan;
import lombok.extern.slf4j.Slf4j;
import service.api.HelloServiceImpl;
import service.netty.NettyServer;

/**
 * @author Cy
 * @date 2021/6/5 10:41
 */
@Slf4j
@ServiceScan
public class NettyServiceTest {
    public static void main(String[] args)  {
        RpcServer server = new NettyServer("192.168.31.92",8000,0);
        HelloServiceImpl helloService = new HelloServiceImpl();
        server.publishService(helloService,"HelloService");
        server.start();
    }
}
