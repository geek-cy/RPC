package utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import enumeration.RpcError;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author Cy
 * @Date 2021/6/3 14:10
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

    public static NamingService getNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    /**
     * 注册实例
     */
    public static void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            log.error("注册服务{}到{}失败",serviceName,inetSocketAddress,e);
        }
        address = inetSocketAddress;
    }

    /**
     * 获取所有实例
     */
    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    /**
     * 注销实例
     */
    public static void clearRegistry() {
        if (!serviceNames.isEmpty() && address != null){
            String host = address.getHostName();
            int port = address.getPort();
            Iterator<String> iterator = serviceNames.iterator();
            while(iterator.hasNext()){
                String serviceName = iterator.next();
                try{
                    namingService.deregisterInstance(serviceName,host,port);
                } catch (NacosException e) {
                    log.error("注销服务{}失败",serviceName,e);
                }
            }
        }
    }
}
