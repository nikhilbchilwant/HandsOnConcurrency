package com.concurrency.solutions.lab13;

import com.concurrency.annotations.GuardedBy;
import com.concurrency.annotations.ThreadSafe;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SOLUTION: Lock Striping
 * 
 * Demonstrates how to divide a data structure into stripes to reduce lock contention.
 */
@ThreadSafe
public class StripedLockSolution {
    private final int numBuckets;
    private final int[] data;
    private final Lock[] locks;

    public StripedLockSolution(int size, int numBuckets) {
        this.numBuckets = numBuckets;
        this.data = new int[size];
        this.locks = new Lock[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    public void increment(int index) {
        Lock lock = locks[index % numBuckets];
        lock.lock();
        try {
            data[index]++;
        } finally {
            lock.unlock();
        }
    }

    public int get(int index) {
        Lock lock = locks[index % numBuckets];
        lock.lock();
        try {
            return data[index];
        } finally {
            lock.unlock();
        }
    }

    public int sum() {
        for (Lock lock : locks) {
            lock.lock();
        }
        try {
            int total = 0;
            for (int val : data) {
                total += val;
            }
            return total;
        } finally {
            for (Lock lock : locks) {
                lock.unlock();
            }
        }
    }
}
