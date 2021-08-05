package register;

import utils.NacosUtil;

import java.net.InetSocketAddress;

/**
 * @author Cy
 * @date 2021/6/9 19:26
 */
public class NacosServiceRegistry implements ServiceRegistry{

    @Override
    public void registry(String serviceName, InetSocketAddress inetSocketAddress) {
        NacosUtil.registerInstance(serviceName, inetSocketAddress);
    }
}
