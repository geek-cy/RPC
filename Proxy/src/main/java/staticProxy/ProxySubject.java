package staticProxy;

/**
 * 代理类，需要为每一个对象都创建代理对象
 * @author Cy
 * @date 2021/6/29 16:47
 */
public class ProxySubject implements Subject{

    private RealSubject realSubject;

    // 方式一：通过传值传递实例化对象（装饰者模式）
    public ProxySubject(RealSubject realSubject){
        this.realSubject = realSubject;
    }
    // 方式二：采用类加载器形式加载实例对象，无需关心什么时候需要真实实例化对象
    public ProxySubject() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.realSubject = (RealSubject) this.getClass().getClassLoader().loadClass("staticProxy.RealSubject").newInstance();
    }

    @Override
    public void doSomething() {
        realSubject.doSomething();
    }

    public static void main(String[] args) {
        // 方式一
        new ProxySubject(new RealSubject()).doSomething();

        // 方式二
        try {
            new ProxySubject().doSomething();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
