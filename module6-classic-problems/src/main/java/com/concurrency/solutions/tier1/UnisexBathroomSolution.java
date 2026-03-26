package com.concurrency.solutions.tier1;

import com.concurrency.annotations.GuardedBy;
import com.concurrency.annotations.ThreadSafe;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SOLUTION: Unisex Bathroom
 */
@ThreadSafe
public class UnisexBathroomSolution {
    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition menCondition = lock.newCondition();
    private final Condition womenCondition = lock.newCondition();

    @GuardedBy("lock")
    private int menCount = 0;
    @GuardedBy("lock")
    private int womenCount = 0;
    @GuardedBy("lock")
    private int waitingMen = 0;
    @GuardedBy("lock")
    private int waitingWomen = 0;

    public UnisexBathroomSolution(int capacity) {
        this.capacity = capacity;
    }

    public void enterMan() throws InterruptedException {
        lock.lock();
        try {
            waitingMen++;
            while (womenCount > 0 || menCount >= capacity || (waitingWomen > 0 && menCount == 0)) {
                menCondition.await();
            }
            waitingMen--;
            menCount++;
        } finally {
            lock.unlock();
        }
    }

    public void exitMan() {
        lock.lock();
        try {
            menCount--;
            if (menCount == 0) {
                if (waitingWomen > 0) womenCondition.signalAll();
                else menCondition.signalAll();
            } else if (waitingWomen == 0) {
                menCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void enterWoman() throws InterruptedException {
        lock.lock();
        try {
            waitingWomen++;
            while (menCount > 0 || womenCount >= capacity || (waitingMen > 0 && womenCount == 0)) {
                womenCondition.await();
            }
            waitingWomen--;
            womenCount++;
        } finally {
            lock.unlock();
        }
    }

    public void exitWoman() {
        lock.lock();
        try {
            womenCount--;
            if (womenCount == 0) {
                if (waitingMen > 0) menCondition.signalAll();
                else womenCondition.signalAll();
            } else if (waitingMen == 0) {
                womenCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
