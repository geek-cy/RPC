package 锁;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 *
 * @author Cy
 * @date 2021/6/27 20:15
 */
@Slf4j
public class SyncTest {
    static final Object obj = new Object();
    /**
     * 轻量级锁
     */
    public static void method1(){
        synchronized (obj){
            method2();
        }
    }
    public static void method2(){
        synchronized (obj){
            System.out.println("执行结束");
        }
    }

    public static void main(String[] args) {
        SyncTest syncTest = new SyncTest();
        log.debug(ClassLayout.parseInstance(syncTest).toPrintable());
        synchronized (syncTest){
            log.debug(ClassLayout.parseInstance(syncTest).toPrintable());
        }
        log.debug(ClassLayout.parseInstance(syncTest).toPrintable());
    }
}
