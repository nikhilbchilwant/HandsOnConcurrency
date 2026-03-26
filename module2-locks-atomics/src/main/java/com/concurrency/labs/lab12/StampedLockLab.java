package com.concurrency.labs.lab12;

import com.concurrency.annotations.GuardedBy;
import com.concurrency.annotations.ThreadSafe;
import java.util.concurrent.locks.StampedLock;

/**
 * Lab 12: Modern Locks
 * 
 * Demonstrates the use of StampedLock for Optimistic Reading.
 */
@ThreadSafe
public class StampedLockLab {

    @GuardedBy("sl")
    private double x, y;
    
    private final StampedLock sl = new StampedLock();

    /**
     * Moves the point using an exclusive write lock.
     */
    public void move(double deltaX, double deltaY) {
        // TODO: Acquire a write lock
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            // TODO: Unlock the write lock
        }
    }

    /**
     * Calculates distance from origin using Optimistic Reading.
     * 💡 THINK: Why is optimistic reading faster than a traditional read lock?
     */
    public double distanceFromOrigin() {
        // TODO: Try an optimistic read
        long stamp = 0L;
        
        // TODO: Read the fields into local variables
        double currentX = x;
        double currentY = y;
        
        // TODO: Validate if a write occurred since we got the stamp
        // if (!sl.validate(stamp)) {
        //     // Validation failed! Fall back to a traditional read lock.
        // }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    /**
     * Conditional update: If the point is at origin, move it to a new location.
     * 💡 THINK: How is tryConvertToWriteLock more efficient than unlockRead/lockWrite?
     */
    public void moveIfAtOrigin(double newX, double newY) {
        // TODO: Acquire a read lock
        long stamp = 0L;
        try {
            while (x == 0.0 && y == 0.0) {
                // TODO: Try to upgrade to a write lock
                // If success: Update x, y and break
                // If fail: unlockRead, acquire writeLock
            }
        } finally {
            // TODO: Unlock
        }
    }

    public static void main(String[] args) {
        StampedLockLab lab = new StampedLockLab();
        System.out.println("Initial Distance: " + lab.distanceFromOrigin());
    }
}
