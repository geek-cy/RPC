package request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 传输格式，服务端需要一些信息才能唯一确定需要调用的接口的方法
 * @Author Cy
 * @Date 2021/5/20 22:06
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    /**
     * 待调用接口名称
     */
    private String interfaceName;

    /**
     * 待调用方法名称
     */
    private String methodName;

    /**
     * 调用方法参数
     */
    private Object[] parameters;

    /**
     * 调用方法参数类型
     */
    private Class<?>[] paramTypes;// 这里用字符串也行
}
