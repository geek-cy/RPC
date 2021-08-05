package register;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 * @author Cy
 * @date 2021/6/9 19:29
 */
public interface ServiceRegistry {

    /**
     * 服务注册
     */
    void registry(String serviceName, InetSocketAddress inetSocketAddress);
}
