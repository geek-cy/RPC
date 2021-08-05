package service;

/**
 * 服务器通用接口
 * @author Cy
 * @date 2021/6/4 22:17
 */
public interface RpcServer {

    void start();

    <T> void publishService(T service,String serviceName);

    void close();

    void scanService();
}
