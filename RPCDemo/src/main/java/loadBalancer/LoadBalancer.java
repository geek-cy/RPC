package loadBalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡策略
 * @Author Cy
 * @Date 2021/6/3 16:07
 */
public interface LoadBalancer {
    Instance select(List<Instance> instances);
}
