import client.RpcClientProxy;
import org.junit.Test;
import api.HelloObject;
import registry.DefaultServiceRegistry;
import registry.ServiceRegistry;
import api.HelloService;
import socket.client.SocketClient;
import socket.server.SocketServer;
//import socket.server.SocketServer;
/**
 * @Description
 * @Author Cy
 * @Date 2021/5/21 21:47
 */
public class TestSocket {

    @Test
    public void testServer(){
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);

    }

    @Test
    public void testClient(){
        SocketClient client = new SocketClient("127.0.0.1", 9000);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
