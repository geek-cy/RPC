package registry;

/**
 * @Description 服务注册表，保存本地服务信息
 * @Author Cy
 * @Date 2021/5/24 22:03
 */
public interface ServiceRegistry {
    <T> void register(T service);
    Object getService(String serviceName);
}
