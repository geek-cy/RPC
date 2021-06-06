package client;

import entity.RpcRequest;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.UUID;

/**
 * 动态代理客户端(RpcClient)
 * @author Cy
 * @date 2021/6/6 1:06
 */
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {

    private final RpcClient rpcClient;

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        System.out.println(Arrays.toString(new Class<?>[]{clazz}));
        System.out.println(clazz);
        System.out.println(this);
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 生成一个识别码,getDeclaringClass返回该方法的声明类
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getSimpleName(), method.getName(), args, method.getParameterTypes(), false);
        return rpcClient.sendRequest(rpcRequest);
    }
}
