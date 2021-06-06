package loadBalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @Author Cy
 * @Date 2021/6/3 16:09
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    private int index = 0;
    @Override
    public Instance select(List<Instance> instances) {
        if(index >= instances.size()){
            index %= instances.size();
        }
        return instances.get(index++);
    }
}
