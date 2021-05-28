package dynamic;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/27 22:23
 */
public class ProxyHandler implements ProxyInterface{
    @Override
    public void use() {
        System.out.println("实现动态代理");
    }
}
