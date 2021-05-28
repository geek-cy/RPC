package socket;

import enumeration.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import entity.RpcRequest;
import entity.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description 通过反射进行方法调用
 * @Author Cy
 * @Date 2021/5/24 23:16
 */
@Slf4j
public class RequestHandler {
    public Object handler(RpcRequest rpcRequest,Object service){
        Object result = null;
        try{
            result = invokeTargetMethod(rpcRequest,service);
            log.info("服务:{},成功调用方法:{}",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("调用或发送时有错误发生",e);
        } return result;
    }

    public Object invokeTargetMethod(RpcRequest rpcRequest,Object service) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND);
        }
        return method.invoke(service,rpcRequest.getParameters());
    }
}
