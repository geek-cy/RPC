package dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Cy
 * @date 2021/6/29 18:24
 */
public class BeanFactory {
    public static BaseService newInstance(Class classFile){
        // 创建真实对象
        BaseService baseService = new RealSubject();
        // 创建代理类
        InvocationHandler invocation = new Invocation(baseService);
        // 向jvm索要代理（监听）的对象
        Class<?>[] classArray = {BaseService.class};
        BaseService b = (BaseService)Proxy.newProxyInstance(classFile.getClassLoader(), classArray, invocation);
        return b;
    }

    public static void main(String[] args) {
        BaseService baseService = newInstance(RealSubject.class);
        baseService.mainService();
        String s = baseService.stringService();
        System.out.println(s);
    }
}
