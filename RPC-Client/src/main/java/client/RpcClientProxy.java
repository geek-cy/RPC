package client;

import client.netty.NettyClient;
import entity.RpcRequest;
import entity.RpcResponse;
import enums.RpcErrorMessageEnum;
import exception.RpcException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 代理实现类：与主要业务进行绑定
 * @author Cy
 * @date 2021/6/6 1:06
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private RpcClient rpcClient;

    // 第一种构造函数传参
    public RpcClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    // 第二种反射传参
    public RpcClientProxy(String name){
        try {
            this.rpcClient = (RpcClient) this.getClass().getClassLoader().loadClass(name).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        // 指定类加载器
        ClassLoader classLoader = clazz.getClassLoader();
        // 向jvm索要代理（监听）的对象
        Class<?>[] classes = {clazz};
        // 指定代理实现类invocation，即this
        return (T)Proxy.newProxyInstance(classLoader,classes,this);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 生成一个识别码,getDeclaringClass返回该方法的声明类
        // method是完整名字
        String interfaceName = method.getDeclaringClass().getName();
        if(interfaceName.contains(".")){
            interfaceName = interfaceName.substring(interfaceName.lastIndexOf(".")+1);
        }
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), interfaceName, method.getName(), args, method.getParameterTypes(), false);
        RpcResponse<Object> rpcResponse = null;
        if(rpcClient instanceof NettyClient){
            CompletableFuture<RpcResponse<Object>> future = (CompletableFuture<RpcResponse<Object>>) rpcClient.sendRequest(rpcRequest);
            rpcResponse = future.get();
        }
        this.check(rpcResponse,rpcRequest);
        log.info("动态代理返回响应");
        rpcClient.close();
        return null;
    }

    private void check(RpcResponse<Object> rpcResponse,RpcRequest rpcRequest){
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, "INTERFACE_NAME" + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, "INTERFACE_NAME" + ":" + rpcRequest.getInterfaceName());
        }
    }
}
