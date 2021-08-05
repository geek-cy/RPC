package register;

import java.net.InetSocketAddress;

/**
 * @author Cy
 * @date 2021/6/9 20:09
 */
public interface ServiceDiscovery {

    /**
     * 服务发现
     */
    InetSocketAddress lookupService(String serviceName);
}
