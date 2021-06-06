package provider;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的服务注册表
 * @author Cy
 * @date 2021/6/5 21:56
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider{

    private static final Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    // set保存服务名
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();


    @Override
    public <T> void addService(T service, String serviceName) {
        if(registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        serviceMap.put(serviceName,service);
        log.info("向接口:{}注册服务:{}",service.getClass().getInterfaces(),serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        log.info("寻找服务:{}",serviceName);
        Object service = serviceMap.get(serviceName);
        if(service == null) throw new RuntimeException("服务不存在");
        return service;
    }
}
