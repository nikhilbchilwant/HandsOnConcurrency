package com.concurrency.labs.lab06;

import com.concurrency.annotations.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Lab 06: Record Delegation
 * 
 * Demonstrates using Java 17 Records for immutability and thread-safe delegation.
 * Records are inherently immutable (shallow), making them excellent for representing 
 * consistent state snapshots.
 * 
 * JCiP REFERENCE:
 * - Ch 3.4: Immutability
 * - Ch 4.3: Delegation
 */
public class RecordDelegationLab {

    /**
     * An immutable representation of a 2D point.
     * Records automatically provide final fields and safe publication.
     */
    public record Point(int x, int y) {}

    /**
     * A thread-safe tracker that delegates its state to an immutable Point record.
     * 
     * 💡 THINK: Why is it safer to replace an entire Record rather than updating 
     * individual fields (int x, int y)?
     */
    @ThreadSafe
    public static class PointTracker {
        // TODO: Initialize an AtomicReference with a new Point(0, 0)
        private final AtomicReference<Point> point = null;

        public Point getPoint() {
            // TODO: Return the current point snapshot
            return null;
        }

        public void setPoint(int x, int y) {
            // TODO: Update the state by replacing the entire record instance atomically.
        }
        
        /**
         * Move the point by a delta.
         */
        public void move(int dx, int dy) {
            // TODO: Use updateAndGet to move the point based on its current value.
            // 💡 THINK: How can we use the current record's values p.x() and p.y()
            // to create the new moved point?
        }
    }

    public static void main(String[] args) {
        PointTracker tracker = new PointTracker();
        System.out.println("Initial Point: " + tracker.getPoint());
        
        // After implementing, this should work correctly:
        // tracker.move(5, 10);
        // System.out.println("After move: " + tracker.getPoint());
    }
}
