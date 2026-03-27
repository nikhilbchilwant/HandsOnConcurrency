package com.concurrency.labs.lab01;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lab 01: Race Condition Fix - Atomic Counter
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 💡 MOTIVATION: In high-concurrency systems, synchronization using       │
 * │ locks (synchronized) can lead to performance bottlenecks. Threads       │
 * │ spend time waiting for locks, which is called "blocking."               │
 * │                                                                         │
 * │ 🎯 PROBLEM: You need to make this counter thread-safe WITHOUT using     │
 * │ explicit locks. The goal is to achieve "lock-free" concurrency that     │
 * │ allows multiple threads to proceed without being blocked.               │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * TODO: Fix the race condition using AtomicInteger.
 * 
 * 📝 NOTE: Atomic classes use Compare-And-Swap (CAS) operations at the hardware
 * level, which are lock-free and often faster than synchronized blocks.
 * 
 * 💡 THINK: When would you choose AtomicInteger over synchronized?
 *   - AtomicInteger: Single variable, high contention, simple operations
 *   - synchronized: Multiple variables, complex logic, need for atomicity across operations
 * 
 * ⚠️ AVOID: Don't use AtomicInteger when you need to update multiple related
 * fields atomically - that requires synchronized or Lock.
 */
public class AtomicCounter {
    
    // TODO: Replace int with AtomicInteger
    // 🔑 HINT: AtomicInteger has methods like incrementAndGet(), getAndIncrement()
    private int count = 0;
    
    /**
     * TODO: Use AtomicInteger's atomic increment method.
     * 
     * 📝 NOTE: incrementAndGet() is equivalent to ++count
     *          getAndIncrement() is equivalent to count++
     */
    public void increment() {
        // TODO: Use atomic operation instead of count++
        count++;
    }
    
    public int getCount() {
        // TODO: Return the atomic value
        return count;
    }
}
