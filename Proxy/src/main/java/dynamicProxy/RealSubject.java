package dynamicProxy;

/**
 * 代理对象即监听对象
 * @author Cy
 * @date 2021/6/29 17:08
 */
public class RealSubject implements BaseService{

    @Override
    public void mainService() {
        System.out.println("mainService");
    }

    @Override
    public String stringService() {
        return "StringService";
    }
}
