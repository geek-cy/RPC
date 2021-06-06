package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识这个类提供一个服务
 * @Author Cy
 * @Date 2021/6/3 16:43
 */
@Target(ElementType.TYPE)// 该注解只能声明在一个类前
@Retention(RetentionPolicy.RUNTIME)// 注解保留在程序运行期间。
public @interface Service {
    /**
     * 定义服务的名称
     */
    public String name() default "";
}
