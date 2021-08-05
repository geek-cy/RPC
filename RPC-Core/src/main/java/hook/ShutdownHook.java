package hook;

import utils.NacosUtil;

/**
 * 钩子函数，当服务关闭时自动注销所有服务
 * nacos 也是有保活机制的，一小段时间联系不上服务器就会自动注销
 * @author Cy
 * @date 2021/6/9 20:22
 */
public class ShutdownHook {

    /**
     * jvm中增加一个关闭的钩子，当jvm关闭的时候，会执行系统中已经设置的所有通过方法addShutdownHook添加的钩子，当系统执行完这些钩子后，jvm才会关闭
     */
    public static void shutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(NacosUtil::deregisterInstance));
    }
}
