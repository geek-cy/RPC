package service.api;

/**
 * @author Cy
 * @date 2021/6/21 15:25
 */
public class SPIServiceImpl implements SPIService {
    @Override
    public void say() {
        System.out.println("测试Api");
    }
}
