package registry;

/**
 * 本地注册表
 * @Author Cy
 * @Date 2021/5/24 22:03
 */
public interface ServiceProvider {

    <T> void register(T service);

    Object getService(String serviceName);
}
