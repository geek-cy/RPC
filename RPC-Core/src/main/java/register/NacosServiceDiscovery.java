package register;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import register.loadBalance.LoadBalance;
import register.loadBalance.RandomLoadBalance;
import utils.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Cy
 * @date 2021/6/9 20:11
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery{

    private final LoadBalance loadBalance;

    public NacosServiceDiscovery() {
        loadBalance = new RandomLoadBalance();
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        List<Instance> instances = NacosUtil.getAllInstance(serviceName);
//        Instance instance = loadBalance.select(instances);
        Instance instance = instances.get(0);
        log.info("调用的当前实例为{}",instance.toString());
        return new InetSocketAddress(instance.getIp(),instance.getPort());
    }
}
