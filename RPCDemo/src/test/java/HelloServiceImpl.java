import annotation.Service;
import lombok.extern.slf4j.Slf4j;
import api.HelloObject;
import api.HelloService;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/20 21:58
 */
@Slf4j
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloObject object) {
        log.info("接收到:{}",object.getMessage());
        return "这是调用的返回值,id="+object.getId();
    }
}
