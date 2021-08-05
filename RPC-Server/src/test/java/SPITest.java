import service.api.SPIService;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 约定在 Classpath 下的 META-INF/services/ 目录里创建一个以服务接口命名的文件，然后文件里面记录的是此 jar 包提供的具体实现类的全限定名。
 * @author Cy
 * @date 2021/6/21 15:19
 */
public class SPITest {
    /**
     * 1、ServiceLoader通过LazyIterator的hasNextService方法从约定好的目录找到接口名对应的文件，
     * 2、加载文件解析文件内容得到实现类的全限定类名,循环加载实现类和创建实例
     * 缺点：在查找扩展实现类的时候遍历SPI配置文件将实现类全部实例化，无法按需加载实现类
     */
    public static void main(String[] args) {
        ServiceLoader<SPIService> serviceLoader = ServiceLoader.load(SPIService.class);
        Iterator<SPIService> iterator = serviceLoader.iterator();
        while(iterator.hasNext()){
            SPIService server = iterator.next();
            server.say();
        }
    }
}
