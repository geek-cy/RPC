package server;

import annotation.Service;
import annotation.ServiceScan;
import enumeration.RpcError;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import registry.ServiceProvider;
import registry.ServiceRegistry;
import serializer.CommonSerializer;
import utils.ReflectUtil;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * 扫描服务
 *
 * @Author Cy
 * @Date 2021/6/3 16:52
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer {

    private String host;
    private int port;
    private ServiceRegistry serviceRegistry;
    private ServiceProvider serviceProvider;

    public void scanServices() {
        // main方法处于栈的最低端
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            // 当前类是否有此注解
            if (!startClass.isAnnotationPresent(ServiceScan.class)) {
                log.error("启动类缺少@ServiceScan注解");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            log.error("出现未知错误");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if ("".equals(basePackage)) {
            if (mainClassName.lastIndexOf(".") != -1) {
                basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
            }
        }
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(Service.class)) {
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    log.error("创建" + clazz + "时有错误发生");
                    continue;
                }
                if ("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        publishService(obj, i.getCanonicalName());
                    }
                }
            }
        }
    }

    public <T> void publishService(T service, String serviceName) {
        serviceProvider.register(service);
        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));
    }
}
