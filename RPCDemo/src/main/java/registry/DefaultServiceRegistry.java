package registry;

import enumeration.RpcError;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 22:06
 */
@Slf4j
public class DefaultServiceRegistry implements ServiceRegistry{

    /**
     * 将服务名与提供服务的对象对应关系保存在ConcurrentHashMap中
     * 使用一个Set保存当前有哪些对象已经注册，在注册服务时，默认采用这个对象实现的接口的完整类名作为服务名
     */
    private final static Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    private final static Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();// 用于获取该类的规范名称
        if(registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> i : interfaces){
            serviceMap.put(i.getCanonicalName(),service);
        }
        log.info("向接口:{}注册服务:{}",interfaces,serviceName);
    }

    @Override
    public synchronized Object getService(String serviceName){
        Object o = serviceMap.get(serviceName);
        if(o == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return o;
    }

}
