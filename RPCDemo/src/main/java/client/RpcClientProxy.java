package client;

import lombok.AllArgsConstructor;
import lombok.Data;
import request.RpcRequest;
import response.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description jdk动态代理
 * @Author Cy
 * @Date 2021/5/20 22:38
 */
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {
    private final String host;
    private final int port;

    /**
     * 通过Proxy 类的 newProxyInstance() 创建的代理对象在调用方法的时候
     * 实际会调用到实现InvocationHandler 接口的类的 invoke()方法
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {

        // newProxyInstance() ，这个方法主要用来生成一个代理对象。
        // 这个方法一共有 3 个参数：
        // loader :类加载器，用于加载代理对象。
        // interfaces : 被代理类实现的一些接口；
        // h : 实现了 InvocationHandler 接口的对象；
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * @param proxy  动态生成的代理类
     * @param method 与代理类对象调用的方法相对应
     * @param args   当前method方法的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 生存一个RpcRequest对象发送出去
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new RpcClient();
        return ((RpcResponse)rpcClient.sendRequest(rpcRequest,host,port)).getData();
    }
}
