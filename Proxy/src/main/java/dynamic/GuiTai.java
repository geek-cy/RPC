package dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/25 20:56
 */
public class GuiTai implements InvocationHandler {

    private final Object brand;


    public GuiTai(Object brand){
        this.brand = brand;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getClass().getName());
        System.out.println(method.getDeclaringClass().getName());
        System.out.println(method.getName());
        System.out.println("销售开始 柜台是:"+this.getClass().getSimpleName());
        method.invoke(brand,args);
        System.out.println("销售结束");
        return null;
    }
}
