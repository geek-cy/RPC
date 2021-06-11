package api;

import annotation.Service;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cy
 * @date 2021/6/6 0:47
 */
@Slf4j
@Service
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(HelloObject object) {
        log.debug("成功接受到{}",object.getMessage());
        return "OK";
    }
}
