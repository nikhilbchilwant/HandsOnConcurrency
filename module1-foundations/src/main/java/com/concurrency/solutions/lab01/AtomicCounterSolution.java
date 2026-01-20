package com.concurrency.solutions.lab01;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * SOLUTION: Lab 01 - Atomic Counter
 */
public class AtomicCounterSolution {

    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
