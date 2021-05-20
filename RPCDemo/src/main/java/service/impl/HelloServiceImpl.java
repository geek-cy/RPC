package service.impl;

import lombok.extern.slf4j.Slf4j;
import pojo.HelloObject;
import service.HelloService;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/20 21:58
 */
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloObject object) {
        log.info("接收到:{}",object.getMessage());
        return "这是调用的返回值,id="+object.getId();
    }
}
