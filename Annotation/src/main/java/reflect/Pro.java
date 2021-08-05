package reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述需要执行的类名，方法名
 * @author Cy
 * @date 2021/6/10 19:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pro {

    String className();
    String methodName();
}
/**
 * public class ProImpl implements Pro{
 *     public String className(){
 *         return "reflect.Demo";
 *     }
 *     public String methodName(){
 *         return "show";
 *     }
 * }
 */
