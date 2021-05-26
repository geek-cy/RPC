import domain.Person;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 框架类
 * 1、将需要创建的对象全类名和需要执行的方法定义在配置文件中
 * 2、在程序中加载读取配置文件
 * 3、使用反射技术来加载类文件进内存
 * 4、创建对象
 * 5、执行方法
 * @Author Cy
 * @Date 2021/5/27 0:17
 */
public class ReflectTest {

    /**
     * 不能改变该类的任何代码来创建任意类的对象，可以执行任意方法
     */
    public static void main(String[] args) throws Exception {
        // 1、加载配置文件
        Properties pro = new Properties();
        // 2、转换为一个集合
        // 2.1、获取配置文件
        ClassLoader classLoader = ReflectTest.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream("pro.properties");
        pro.load(is);
        // 3、获取配置文件中定义的数据
        String className = pro.getProperty("className");
        String methodName = pro.getProperty("methodName");
        // 4、加载该类进内存
        Class<?> aClass = Class.forName(className);
        // 5、创建对象
        Object o = aClass.newInstance();
        // 6、获取方法对象
        Method method = aClass.getMethod(methodName,String.class);
        // 7、执行方法
        method.invoke(o,"饭");
    }
}
