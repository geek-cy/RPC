import client.RpcClientProxy;
import org.junit.Test;
import api.HelloObject;
import registry.ServiceProvider;
import api.HelloService;
import registry.ServiceProviderImpl;
import serializer.KryoSerializer;
import socket.SocketClient;
import socket.SocketServer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/21 21:47
 */
public class TestSocket {

    @Test
    public void testServer(){
        HelloService helloService = new HelloServiceImpl();
        SocketServer socketServer = new SocketServer("127.0.0.1",8888);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.publishService(helloService,HelloService.class);
    }

    @Test
    public void testClient(){
        SocketClient client = new SocketClient();
        client.setSerializer(new KryoSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        // HelloService被代理接口
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
