import client.RpcClientProxy;
import org.junit.Test;
import api.HelloObject;
import registry.ServiceProvider;
import api.HelloService;
import registry.ServiceProviderImpl;
import socket.SocketClient;
import socket.SocketServer;
//import socket.SocketServer;
/**
 * @Description
 * @Author Cy
 * @Date 2021/5/21 21:47
 */
public class TestSocket {

    @Test
    public void testServer(){
        HelloService helloService = new HelloServiceImpl();
        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.register(helloService);
        SocketServer socketServer = new SocketServer(serviceProvider);
        socketServer.start(9000);

    }

    @Test
    public void testClient(){
        SocketClient client = new SocketClient("127.0.0.1", 9000);
        RpcClientProxy proxy = new RpcClientProxy(client);
        // HelloService被代理接口
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
