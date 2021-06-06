package handler;

import entity.RpcRequest;
import provider.ServiceProvider;
import provider.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 进行过程调用处理器
 * @author Cy
 * @date 2021/6/5 21:46
 */
public class RequestHandler {

    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handler(RpcRequest rpcRequest){
        Object service = RequestHandler.serviceProvider.getService(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest,service);
    }
    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service){
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            /*System.out.println(service); //api.HelloServiceImpl
            System.out.println(method);//public java.lang.String api.HelloServiceImpl.hello(api.HelloObject)
            System.out.println(rpcRequest.getMethodName());// hello
            System.out.println(Arrays.toString(rpcRequest.getParamTypes()));// [class api.HelloObject]
            System.out.println(Arrays.toString(rpcRequest.getParameters()));// [HelloObject(id=1, message=gg)]*/
            result = method.invoke(service,rpcRequest.getParameters());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("方法未找到");
        }
        return result;
    }

}
