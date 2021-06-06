package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 传输格式，服务端需要一些信息才能唯一确定需要调用的接口的方法
 * @Author Cy
 * @Date 2021/5/20 22:06
 */

// 建造者模式：将复杂对象的构建过程封装到构建者里，使用构建者再去创建对象
// 内部类：更好的封装性,内部类成员可以直接访问外部类的私有数据，因为内部类被当成其外部类成员，但外部类不能访问内部类的实现细节，例如内部类的成员变量,匿名内部类适合用于创建那些仅需要一次使用的类
// 使用static来修饰一个内部类，则这个内部类就属于外部类本身，而不属于外部类的某个对象。称为静态内部类（也可称为类内部类），
// 这样的内部类是类级别的，static关键字的作用是把类的成员变成类相关，而不是实例相关
// 注意
// 1.非静态内部类中不允许定义静态成员
// 2.外部类的静态成员不可以直接使用非静态内部类
// 3.静态内部类，不能访问外部类的实例成员，只能访问外部类的类成员

//对外提供初始化RpcRequest类的唯一接口，通过这个方法，获得内部类的实例
//    public static RpcRequest.RpcRequestBuilder builder() {
//       return new RpcRequest.RpcRequestBuilder();
//    }

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 请求号
     */
    private String requestId;

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
