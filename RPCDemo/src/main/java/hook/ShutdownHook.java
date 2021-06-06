package hook;

import factory.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;
import utils.NacosUtil;

import java.util.concurrent.ExecutorService;

/**
 * 使用单例模式创建钩子
 * @Author Cy
 * @Date 2021/6/3 15:07
 */
@Slf4j
public class ShutdownHook {

    private final ExecutorService threadPool = ThreadPoolFactory.createDefaultThreadPool("shutdown-hook");
    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook(){
        return shutdownHook;
    }

    public void addClearAllHook(){
        log.info("关闭后将自动注销所有服务");
        // JVM关闭后会执行所有方法
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            NacosUtil.clearRegistry();
            threadPool.shutdown();
        }));
    }
}
