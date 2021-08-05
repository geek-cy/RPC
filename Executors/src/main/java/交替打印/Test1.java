package 交替打印;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交替打印奇偶数
 * synchronized、ReentrantLock、LockSupport、CAS、TransferQueue 这几种实现方式必须掌握
 *
 * @author Cy
 * @date 2021/6/11 23:20
 */
@Slf4j
public class Test1 {

    private final int NUM = 100;
    private int count = 0;

    /**
     * 因为线程是随机抢锁的，有可能连续十次都是偶数线程好运抢到了锁，
     * 只是因为不满足条件，没有对 count 进行 +1，白白浪费一次占用资源的机会
     */
    @Test
    public void sync() {
        Thread t1 = new Thread(() -> {
            while (count < NUM) {
                    if ((count & 1) == 1) {

                }
            }
        });
        Thread t2 = new Thread(() -> {
            while (count < NUM) {
                synchronized (this) {
                    if ((count & 1) != 1) {
                        System.out.println("偶数:" + count++);
                    } else System.out.println("偶数拿到锁");
                }
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 无法保证t1先执行
     */
    @Test
    public void waitAndNotify() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (this) {
                while (count < NUM) {

                        System.out.printf("奇数:%d", count++);
                        System.out.println();
                    try {
                        this.notify();
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.notify();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (this) {
                while (count < NUM) {

                        System.out.printf("偶数:%d", count++);
                        System.out.println();

                    try {
                        this.notify();
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.notify();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("执行结束");
    }

    private static CountDownLatch latch = new CountDownLatch(1);

    /**
     * 通过CountDownLatch保证谁先执行
     */
    @Test
    public void countDownLatch() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                while (count < NUM) {
                    if ((count & 1) == 1) {
                        log.info("奇数:{}", count++);
                    }
                    try {
                        this.notify();
                        this.wait();// 让出锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.notify();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (this) {
                while (count < NUM) {
                    if ((count & 1) == 0) {
                        log.info("偶数:{}", count++);
                        latch.countDown();
                    }
                    try {
                        this.notify();
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.notify();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    Thread t1 = new Thread();
    Thread t2 = new Thread();

    @Test
    public void lockSupport() throws InterruptedException {
        t1 = new Thread(() -> {
            while (count <= NUM) {

                LockSupport.park();// 1
                    System.out.println("奇数:" + count++);
                LockSupport.unpark(t2);
            }
        });
        t2 = new Thread(() -> {
            while (count <= NUM) {
                if ((count & 1) == 0) {
                    System.out.println("偶数:" + count++);
                }
                LockSupport.unpark(t1);// 2
                LockSupport.park();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void reentrantLock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
                while (count <= NUM) {
                    if ((count & 1) == 1) log.info("奇数:{}", count++);
                    condition2.signal();
                    condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition2.signal();
        });
        Thread t2 = new Thread(() -> {
            try {
                lock.lock();
                while (count <= NUM) {
                    if ((count & 1) == 0) log.info("偶数:{}", count++);
                    ;
                    condition.signal();
                    condition2.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition.signal();
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void transferQueue() throws InterruptedException {
        TransferQueue<Integer> queue = new LinkedTransferQueue<>();// 容量为0；一旦传输就会阻塞
        t1 = new Thread(() -> {
            while (count <= NUM) {
                try {
                    if ((count & 1) == 1) {
                        System.out.println("偶数:" + queue.take()); // 取出偶数
                        queue.transfer(count++);// 将奇数放进去
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t2 = new Thread(() -> {
            while (count <= NUM) {
                try {
                    if ((count & 1) == 0) {
                        queue.transfer(count++);// 将偶数放进去
                        System.out.println("奇数:" + queue.take());// 取出奇数
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}
