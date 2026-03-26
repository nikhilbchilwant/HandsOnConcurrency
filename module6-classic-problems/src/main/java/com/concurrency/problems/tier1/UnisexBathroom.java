package com.concurrency.problems.tier1;

import com.concurrency.annotations.GuardedBy;
import com.concurrency.annotations.ThreadSafe;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * PROBLEM: The Unisex Bathroom (Group Exclusion)
 * 
 * INTERVIEW RELEVANCE: 
 * - Companies: [Rubrik, Dropbox]
 * - Frequency: HIGH (Classic Rubrik question)
 * 
 * REQUIREMENTS:
 * 1. Only one gender can be in the bathroom at a time.
 * 2. Multiple people of the same gender can use it simultaneously (up to N capacity).
 * 3. Ensure no starvation (if women are waiting, men shouldn't keep entering).
 */
@ThreadSafe
public class UnisexBathroom {
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

    public UnisexBathroom(int capacity) {
        this.capacity = capacity;
    }

    public void enterMan() throws InterruptedException {
        // TODO: Implement logic for a man to enter safely
        // 💡 THINK: What conditions must be met? (womenCount? capacity? waitingWomen?)
    }

    public void exitMan() {
        // TODO: Implement logic for a man to exit and signal others
        // 💡 THINK: When should we signal menCondition vs womenCondition?
    }

    public void enterWoman() throws InterruptedException {
        // TODO: Implement logic for a woman to enter safely
    }

    public void exitWoman() {
        // TODO: Implement logic for a woman to exit and signal others
    }
}
