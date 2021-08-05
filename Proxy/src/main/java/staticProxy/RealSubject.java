package staticProxy;

/**
 * 真实对象
 * @author Cy
 * @date 2021/6/29 16:46
 */
public class RealSubject implements Subject{
    @Override
    public void doSomething() {
        System.out.println("doSomething()");
    }
}
