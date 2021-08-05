import client.api.ByeService;
import api.HelloObject;
import client.api.HelloService;
import client.RpcClient;
import client.RpcClientProxy;
import client.netty.NettyClient;

/**
 * @author Cy
 * @date 2021/6/6 15:43
 */
public class NettyClientTest {

    public static void main(String[] args) throws InterruptedException {
        RpcClient client = new NettyClient();
        // 构造函数传参,类似装饰者模式
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService proxy = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(1, "gg");
        String hello = proxy.hello(object);
        System.out.println(hello);

        // 反射传参，采用类加载器形式加载实例对象，不需要关系什么时候需要真实的实例化对象
        /*RpcClientProxy rpcClientProxy2 = new RpcClientProxy(NettyClient.class.getName());
        ByeService byeService = rpcClientProxy2.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));*/
        /*Thread.sleep(5000);
        client.close();*/
    }
}
