package com.concurrency.labs.lab13;

import com.concurrency.annotations.GuardedBy;
import com.concurrency.annotations.ThreadSafe;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lab 13: Lock Striping (Performance Optimization)
 * 
 * JCiP REFERENCE:
 * - Ch 11.4.3: Lock Striping
 * 
 * PROBLEM:
 * Implement a thread-safe fixed-size array where multiple threads can update 
 * different indices simultaneously without global lock contention.
 */
@ThreadSafe
public class StripedLockLab {
    private final int numBuckets;
    private final int[] data;
    private final Lock[] locks;

    public StripedLockLab(int size, int numBuckets) {
        this.numBuckets = numBuckets;
        this.data = new int[size];
        this.locks = new Lock[numBuckets];
        // TODO: Initialize the array of ReentrantLocks
    }

    /**
     * Increments the value at the given index.
     * 💡 THINK: How do we map 'index' to a specific lock in the 'locks' array?
     */
    public void increment(int index) {
        // TODO: Map the index to one of the locks (stripes)
        // TODO: Acquire the correct lock
        try {
            data[index]++;
        } finally {
            // TODO: Unlock the correct lock
        }
    }

    /**
     * Calculates the sum of all elements.
     * 💡 THINK: Why do we need to acquire ALL locks to calculate the sum?
     * ⚠️ WARNING: What is the risk of acquiring multiple locks, and how 
     * can we avoid deadlock?
     */
    public int sum() {
        // TODO: Lock all buckets in a consistent order to avoid deadlock
        try {
            int total = 0;
            for (int val : data) {
                total += val;
            }
            return total;
        } finally {
            // TODO: Unlock all buckets
        }
    }

    public static void main(String[] args) throws InterruptedException {
        StripedLockLab lab = new StripedLockLab(100, 10);
        lab.increment(5);
        System.out.println("Sum (Expected 1): " + lab.sum());
    }
}
