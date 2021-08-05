package register.loadBalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * 随机算法
 * @author Cy
 * @date 2021/6/9 20:14
 */
public class RandomLoadBalance implements LoadBalance{

    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(new Random().nextInt(instances.size()));
    }
}
