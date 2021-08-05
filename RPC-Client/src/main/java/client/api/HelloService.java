package client.api;

import api.HelloObject;

/**
 * 客户端调用的接口
 * @Author Cy
 * @Date 2021/6/4 19:19
 */
public interface HelloService {
    String hello(HelloObject object);
}
