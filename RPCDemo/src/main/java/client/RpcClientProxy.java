package client;

import lombok.AllArgsConstructor;
import entity.RpcRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 *  jdk动态代理
 * @Author Cy
 * @Date 2021/5/20 22:38
 */
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {

    private final RpcClient client;

    /**
     * 通过Proxy 类的 newProxyInstance() 创建的代理对象在调用方法的时候
     * 实际会调用到实现InvocationHandler 接口的类的 invoke()方法
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {

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
        /*RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();*/
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(),method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());
        return client.sendRequest(rpcRequest);
    }
}
