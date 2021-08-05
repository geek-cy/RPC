package utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import enums.RpcErrorMessageEnum;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Nacos Api
 * @Author Cy
 * @Date 2021/6/4 19:21
 */
@Slf4j
public class NacosUtil {

    private static final NamingService namingService;
    private static final Set<String> serviceNames = new HashSet<>();
    private static InetSocketAddress address;
    private static final String SERVER_ADDR = "192.168.31.92:8848";

    static {
        namingService = getNamingService();
    }

    /**
     * 从Nacos服务器获得名字服务用于服务发现和 DNS
     * 服务发现:对服务下的实例的地址和元数据进行探测
     */
    public static NamingService getNamingService(){
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生:",e);
            throw new RpcException(RpcErrorMessageEnum.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    /**
     * 根据服务名称和服务地址注册到Naocs
     */
    public static void registerInstance(String serviceName,InetSocketAddress address){
        try {
            namingService.registerInstance(serviceName,address.getHostName(),address.getPort());
            NacosUtil.address = address;
            serviceNames.add(serviceName);
        } catch (NacosException e) {
            log.error("注册Nacos服务出错");
        }
    }

    /**
     * 根据服务名称获取所有实例
     * 实例:instanceId、ip、port、weight、healthy、enabled、ephemeral、clusterName、serviceName
     */
    public static List<Instance> getAllInstance(String serviceName){
        try {
            return namingService.getAllInstances(serviceName);
        } catch (NacosException e) {
            log.error("获取Nacos所有实例出错");
            return null;
        }
    }

    public static void deregisterInstance(){
        if(!serviceNames.isEmpty() && address != null){
            String host = address.getHostName();
            int port = address.getPort();
            for (String serviceName : serviceNames) {
                try {
                    namingService.deregisterInstance(serviceName, host, port);
                } catch (NacosException e) {
                    log.error("注销服务失败", e);
                }
            }
        }
    }


}
