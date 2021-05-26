import netty.NettyClient;
import client.RpcClient;
import client.RpcClientProxy;
import org.junit.Test;
import api.HelloObject;
import registry.DefaultServiceRegistry;
import registry.ServiceRegistry;
import netty.NettyServer;
import api.HelloService;

import java.util.Scanner;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 21:14
 */
public class TestNetty {

    @Test
    public void testServer() {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9000);
    }

    @Test
    public void testClient(){
        RpcClient client = new NettyClient("127.0.0.1",9000);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        Scanner s = new Scanner(System.in);
        HelloObject object = new HelloObject(1,s.toString());
        String hello = helloService.hello(object);
        System.out.println(hello);
    }
}
