package registry;

import java.net.InetSocketAddress;

/**
 * 远程注册表
 * @Author Cy
 * @Date 2021/5/28 23:28
 */
public interface ServiceRegistry {
    /**
     * 将服务的名称和地址注册进服务注册中心
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 根据服务名称从注册中心获取一个服务提供者地址
     */
    InetSocketAddress lookupService(String serviceName);
}
