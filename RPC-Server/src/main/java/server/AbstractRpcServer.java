package server;

import lombok.extern.slf4j.Slf4j;
import provider.ServiceProvider;

/**
 * @author Cy
 * @date 2021/6/4 22:19
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer {

    protected String host;
    protected int port;
    protected ServiceProvider serviceProvider;

    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addService(service,serviceName);
    }
}
