import api.HelloObject;
import api.HelloService;
import client.RpcClient;
import client.RpcClientProxy;
import client.netty.NettyClient;

/**
 * @author Cy
 * @date 2021/6/6 15:43
 */
public class NettyClientTest {
    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService proxy = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(1, "gg");
        String hello = proxy.hello(object);
        System.out.println(hello);
    }
}
