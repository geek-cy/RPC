package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消费者向提供者发送请求对象
 * @author Cy
 * @Date 2021/6/4 19:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest {

    /**
     * 请求号
     * UUID.randomUUID().toString()
     */
    private String requestId;

    /**
     * 待调用接口名称
     * method.getDeclaringClass().getName()
     */
    private String interfaceName;

    /**
     * 待调用方法名称
     */
    private String methodName;

    /**
     * 待调用方法形参
     */
    private Object[] parameters;

    /**
     * 待调用方法类型
     */
    private Class<?>[] paramTypes;

    /**
     * 判断心跳包
     */
    private Boolean heartBeat;

}
