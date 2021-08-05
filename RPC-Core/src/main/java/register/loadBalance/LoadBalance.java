package register.loadBalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡接口
 * @author Cy
 * @date 2021/6/9 20:12
 */
public interface LoadBalance {

    /**
     * 通过负载均衡算法选择实例
     */
    Instance select(List<Instance> instances);

}
