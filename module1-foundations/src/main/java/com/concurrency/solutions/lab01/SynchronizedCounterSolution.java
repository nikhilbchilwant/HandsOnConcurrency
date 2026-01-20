package com.concurrency.solutions.lab01;

/**
 * SOLUTION: Lab 01 - Synchronized Counter
 */
public class SynchronizedCounterSolution {

    private int count = 0;

    // Using method-level synchronization for simplicity
    public synchronized void increment() {
        count++;
    }

    // Must also be synchronized for visibility!
    public synchronized int getCount() {
        return count;
    }
}
