package socket;

import client.RpcClient;
import entity.RpcRequest;
import entity.RpcResponse;
import enumeration.ResponseCode;
import enumeration.RpcError;
import exception.RpcException;
import loadBalancer.RoundRobinLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import registry.NacosServiceRegistry;
import registry.ServiceProvider;
import registry.ServiceRegistry;
import serializer.CommonSerializer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Description 实现发送的逻辑
 * @Author Cy
 * @Date 2021/5/21 20:50
 */
@Slf4j
public class SocketClient implements RpcClient {

    private final ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;
    private ServiceProvider serviceProvider;
    public SocketClient() {
        this.serviceRegistry = new NacosServiceRegistry(new RoundRobinLoadBalancer());
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Object sendRequest(RpcRequest rpcRequest){
        if(serializer == null){
            log.error("未设置反序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
//        Object service = serviceProvider.getService(rpcRequest.getInterfaceName());
        InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
        try(Socket socket = new Socket()){
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream,rpcRequest,serializer);
            RpcResponse rpcResponse = (RpcResponse) ObjectReader.readObject(inputStream);
            if(rpcResponse == null) {
                log.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                log.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();
        } catch (IOException e) {
            log.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
