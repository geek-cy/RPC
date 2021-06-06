package provider;

/**
 * 本地保存和提供服务实例对象
 * @author Cy
 * @date 2021/6/5 21:54
 */
public interface ServiceProvider {

    <T> void addService(T Service,String serviceName);

    Object getService(String serviceName);
}
