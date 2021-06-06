/*
import annotation.ServiceScan;
import server.client.netty.NettyClient;
import client.RpcClient;
import client.RpcClientProxy;
import org.junit.Test;
import api.HelloObject;
import server.client.netty.NettyServer;
import api.HelloService;
import serializer.KryoSerializer;

*/
/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 21:14
 *//*

@ServiceScan
public class TestNetty {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer NettyServer = new NettyServer("127.0.0.1", 8000,new KryoSerializer());
        NettyServer.start();
    }


    @Test
    public void testClient() {
        RpcClient client = new NettyClient();
        client.setSerializer(new KryoSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String hello = helloService.hello(object);
        System.out.println(hello);
    }
}
*/
