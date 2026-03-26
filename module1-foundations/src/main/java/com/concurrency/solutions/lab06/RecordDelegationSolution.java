package com.concurrency.solutions.lab06;

import com.concurrency.annotations.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;

/**
 * SOLUTION: Record Delegation
 * 
 * Demonstrates using Java 17 Records for immutability and thread-safe delegation.
 */
public class RecordDelegationSolution {

    public record Point(int x, int y) {}

    @ThreadSafe
    public static class PointTracker {
        private final AtomicReference<Point> point = new AtomicReference<>(new Point(0, 0));

        public Point getPoint() {
            return point.get();
        }

        public void setPoint(int x, int y) {
            point.set(new Point(x, y));
        }
        
        public void move(int dx, int dy) {
            point.updateAndGet(p -> new Point(p.x() + dx, p.y() + dy));
        }
    }
}
