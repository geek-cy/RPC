package dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 次要业务，与主要业务绑定执行
 * @author Cy
 * @date 2021/6/29 18:20
 */
public class Invocation implements InvocationHandler {

    private final BaseService baseService;

    // 方式一：采用传参
    public Invocation(BaseService baseService){
        this.baseService = baseService;
    }

    // 监听对象
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = method.invoke(baseService, args);
        System.out.println("增强业务");
        // 返回值决定调方法时的返回值
        return invoke;
    }
}
