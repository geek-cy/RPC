import netty.NettyClient;
import client.RpcClient;
import client.RpcClientProxy;
import org.junit.Test;
import api.HelloObject;
import registry.ServiceProvider;
import netty.NettyServer;
import api.HelloService;
import registry.ServiceProviderImpl;
import serializer.KryoSerializer;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 21:14
 */
public class TestNetty {

    @Test
    public void testServer() {
        HelloService helloService = new HelloServiceImpl();
        ServiceProvider registry = new ServiceProviderImpl();
        registry.register(helloService);
        NettyServer server = new NettyServer("127.0.0.1",8000);
        server.setSerializer(new KryoSerializer());
        server.publishService(helloService,HelloService.class);
    }

    @Test
    public void testClient(){
        RpcClient client = new NettyClient();
        client.setSerializer(new KryoSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12,"This is a message");
        String hello = helloService.hello(object);
        System.out.println(hello);
    }
}
