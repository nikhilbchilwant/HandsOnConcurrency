package com.concurrency.labs.lab01;

/**
 * Lab 01: Race Condition Fix - Synchronized Counter
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 💡 MOTIVATION: In a concurrent system, multiple threads often share     │
 * │ state. For example, an ad-click counter or a bank account balance.      │
 * │ If two threads increment a counter simultaneously, one update might     │
 * │ be lost because "count++" is not an atomic operation.                   │
 * │                                                                         │
 * │ 🎯 PROBLEM: This class is used in a multi-threaded environment where    │
 * │ consistent results are required. You need to ensure that the increment  │
 * │ operation is "atomic" - meaning only one thread can modify the value    │
 * │ at a time.                                                              │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * TODO: Fix the race condition using the synchronized keyword.
 * 
 * 🔑 HINT: You can either:
 *   - Make the entire method synchronized, OR
 *   - Use a synchronized block on a specific lock object
 * 
 * 💡 THINK: What are the tradeoffs between method-level vs block-level synchronization?
 * 
 * ⚠️ AVOID: Don't synchronize on 'this' in public APIs - external code could
 * also synchronize on your object, causing unexpected blocking!
 */
public class SynchronizedCounter {
    
    private int count = 0;
    
    // 🔑 HINT: Consider using a private lock object for better encapsulation
    // private final Object lock = new Object();
    
    /**
     * TODO: Make this method thread-safe using synchronized.
     * 
     * 💡 THINK: After implementing, can you explain what "mutual exclusion" means?
     */
    public void increment() {
        // TODO: Add synchronization here
        count++;
    }
    
    /**
     * TODO: Should this method also be synchronized? Why or why not?
     * 
     * 💡 THINK: What happens if increment() is synchronized but getCount() is not?
     * 📝 NOTE: This relates to the concept of "visibility" in the Java Memory Model.
     */
    public int getCount() {
        // TODO: Consider if synchronization is needed here
        return count;
    }
}
