import reflect.Pro;

/**
 * @author Cy
 * @date 2021/6/10 19:28
 */
@Pro(className = "reflect.Demo",methodName = "show")
public class ProTest {
    public static void main(String[] args) {
        // 不改变类的任何代码创建对象，执行任意方法

        // 获取该类的字节码文件
        Class<ProTest> proTestClass = ProTest.class;
        // 获取注解对象(其实就是在内存中生成了一个该注解接口的子类实现对象)
        Pro pro = proTestClass.getAnnotation(Pro.class);
        // 调用注解对象中定义的抽象方法，获取返回值
        String className = pro.className();
        String methodName = pro.methodName();
        System.out.println(className);
        System.out.println(methodName);
    }
}
