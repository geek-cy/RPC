package annotation;

import java.lang.annotation.*;

/**
 * 定义服务名
 * @author Cy
 * @date 2021/6/10 18:42
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    String value() default "";
}
