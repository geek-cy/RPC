import api.HelloService;
import api.HelloServiceImpl;
import server.RpcServer;
import server.netty.NettyServer;

/**
 * @author Cy
 * @date 2021/6/5 10:41
 */
public class NettyServiceTest {
    public static void main(String[] args) {
        RpcServer server = new NettyServer("192.168.31.92",8000,0);
        HelloService helloService = new HelloServiceImpl();
        server.publishService(helloService,"HelloService");
        server.start();
    }

}
