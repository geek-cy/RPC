package server;

import serializer.CommonSerializer;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/24 23:47
 */
public interface RpcServer {

    void start();

    /**
     * 向Nacos注册服务
     */
    <T> void publishService(Object service,Class<T> serviceClass);

    void setSerializer(CommonSerializer serializer);
}
