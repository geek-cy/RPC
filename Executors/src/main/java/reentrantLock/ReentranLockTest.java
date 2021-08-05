package reentrantLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Cy
 * @date 2021/6/27 22:18
 */
public class ReentranLockTest {
    static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {

        // 创建一个新的条件变量
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        lock.lock();
        // 等待
        condition1.await();
        // 叫醒
        condition1.signal();
        condition1.signalAll();
    }
}
