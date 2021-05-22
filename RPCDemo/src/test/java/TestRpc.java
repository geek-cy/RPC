import client.RpcClientProxy;
import org.junit.Test;
import pojo.HelloObject;
import server.RpcServer;
import service.HelloService;
import service.impl.HelloServiceImpl;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/21 21:47
 */
public class TestRpc {

    @Test
    public void testServer(){
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService,9000);
    }

    @Test
    public void testClient(){
        // 动态代理 屏蔽远程方法调用细节
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1",9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
