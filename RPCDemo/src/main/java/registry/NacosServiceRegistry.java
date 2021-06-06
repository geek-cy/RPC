package registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import enumeration.RpcError;
import exception.RpcException;
import loadBalancer.LoadBalancer;
import loadBalancer.RandomLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import utils.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 通过NamingFactory创建NamingService连接Nacos
 * @Author Cy
 * @Date 2021/5/28 23:29
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry{

    private static final String SERVER_ADDR = "192.168.31.92:8848";
    private static final NamingService namingService;
    private final LoadBalancer loadBalancer;

    public NacosServiceRegistry() {
        this.loadBalancer = new RandomLoadBalancer();
    }

    public NacosServiceRegistry(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos有错误发生",e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        NacosUtil.registerService(serviceName, inetSocketAddress);
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> allInstances = NacosUtil.getAllInstance(serviceName);

            Instance instance = allInstances.get(0);// 此处涉及负载均衡
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生",e);
        }
        return null;
    }
}
