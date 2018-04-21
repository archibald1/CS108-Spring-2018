package com.freeuni.oop.seminar14.semaphore;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("WeakerAccess")
public class Semaphore {
    private final Queue<Condition> conditions = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private int available;

    /**
     * Construct semaphore with maximum available number of allowed thread
     *
     * @param limit Total number of acquires possible on the semaphore
     */
    public Semaphore(int limit) {
        this.available = limit;
    }

    /**
     * In case the maximum thread limit is not reached, the method lets
     * the current thread to pass, otherwise makes it wait until some
     * other thread leaves the guarded region.
     */
    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            if (available <= 0) {
                Condition unavailable = lock.newCondition();
                conditions.add(unavailable); // OR use offer(T elem)
                unavailable.await();
            }

            --available;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Invoke this method to notify semaphore about the ending of critical
     * region. Its invocation means that current thread passed the guarded
     * block and made it available for another thread in queue.
     */
    public void release() {
        lock.lock();
        try {
            ++available;
            if (conditions.size() > 0) {
                Objects.requireNonNull(conditions.poll()).signal();
                // signal(), so we can wake only *one* thread and not the rest
            }
        } finally {
            lock.unlock();
        }
    }

}
