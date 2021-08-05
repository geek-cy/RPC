package atomic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.atomic.*;

/**
 * @author Cy
 * @date 2021/6/28 21:57
 */
@Slf4j
public class AtomicTest {

    /**
     * 原子整数类型
     */
    @Test
    public void testAtomicInteger(){
        AtomicInteger i = new AtomicInteger(1);
        i.incrementAndGet();//++i 2
        i.getAndIncrement();//i++ 2
        i.getAndAdd(5);// 3 + 5 = 8
        i.addAndGet(2);// 8
        i.updateAndGet(x -> x*10);// 100
        System.out.println(i.get());
    }

    /**
     * 原子引用类型
     */
    @Test
    public void testAtomicReference() throws InterruptedException {
        int a = 0;
        AtomicReference<Integer> ref = new AtomicReference<>(a);
        new Thread(()->{
            log.info("change a = 2 {}",ref.compareAndSet(0,2));
        }).start();

        new Thread(()->{
            log.info("change a = 3 {}",ref.compareAndSet(0,3));
        }).start();
        Thread.sleep(1000);
        System.out.println(ref.get());
    }
    /**
     * 通过Stamp决定修改是否成功
     */
    @Test
    public void testAtomicStampedReference() throws InterruptedException {
        int a = 0;
        AtomicStampedReference<Integer> ref = new AtomicStampedReference<>(a,0);
        new Thread(()->{
            int stamp = ref.getStamp();
            log.info("change a = 2 {}",ref.compareAndSet(ref.getReference(),2,stamp,stamp+1));
        }).start();

        new Thread(()->{
            int stamp = ref.getStamp();
            log.info("change a = 1 {}",ref.compareAndSet(ref.getReference(),1,stamp,stamp+1));
        }).start();

        new Thread(()->{
            int stamp = 1;
            log.info("change a = 1 {}",ref.compareAndSet(ref.getReference(),1,stamp,stamp+1));
        }).start();

        Thread.sleep(1000);
        System.out.println(ref.getReference());
    }

    /**
     * 通过Mark标记决定修改是否成功
     */
    @Test
    public void testAtomicMarkableReference() throws InterruptedException {
        int a = 0;
        AtomicMarkableReference<Integer> ref = new AtomicMarkableReference<>(a,true);

        new Thread(()->{
            int stamp = ref.getReference();
            log.info("change a = 4 {}",ref.compareAndSet(ref.getReference(),4,true,false));
        }).start();

        new Thread(()->{
            int stamp = ref.getReference();
            log.info("change a = 3 {}",ref.compareAndSet(ref.getReference(),3,true,false));

        }).start();
        Thread.sleep(1000);
        System.out.println(ref.getReference());
    }



}
