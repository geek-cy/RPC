package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识服务的扫描包的范围
 * @Author Cy
 * @Date 2021/6/3 16:46
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceScan {
    /**
     * 定义扫描范围的根包
     */
    public String value() default "";
}
