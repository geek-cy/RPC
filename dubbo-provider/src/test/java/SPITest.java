import com.dubbo.spi.Cluster;
import com.dubbo.spi.Test;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * @author Cy
 * @date 2021/6/21 16:00
 */
public class SPITest {

    public static void main(String[] args) {
        ExtensionLoader<Cluster> extensionLoader = ExtensionLoader.getExtensionLoader(Cluster.class);
        URL url = new URL("http", "localhost", 8080);
        url = url.addParameter("person", "person1");
        Cluster cluster = extensionLoader.getExtension("clusterImpl");
        cluster.name(url);

        ExtensionLoader<Test> testExtensionLoader = ExtensionLoader.getExtensionLoader(Test.class);
//        Test test = testExtensionLoader.getExtension("true");
        Test test = testExtensionLoader.getAdaptiveExtension();// 代理对象 // @Adaptive表示手动实现代理类
        URL url1 = url.addParameter("p", "person1");
        System.out.println(test.test(url1));

    }
}

