package register.loadBalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮转算法(废弃，多个客户端需要共享资源才能选择哪个服务端)
 * @author Cy
 * @date 2021/6/9 20:15
 */
public class RoundRobinLoadBalance implements LoadBalance {

    private final AtomicInteger index = new AtomicInteger();

    /**
     * 获取实例
     */
    public static RoundRobinLoadBalance getInstance() {
        return new RoundRobinLoadBalance();
    }

    @Override
    public Instance select(List<Instance> instances) {
        if(instances.size() == 0) throw new RuntimeException("当前没有实例对象");
        return instances.get(index.getAndIncrement() % instances.size());
    }


}
