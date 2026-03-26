package com.concurrency.labs.lab06;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.concurrency.labs.lab06.RecordDelegationLab.PointTracker;
import com.concurrency.labs.lab06.RecordDelegationLab.Point;

public class RecordDelegationLabTest {

    @Test
    public void testInitialState() {
        PointTracker tracker = new PointTracker();
        assertEquals(new Point(0, 0), tracker.getPoint());
    }

    @Test
    public void testSetPoint() {
        PointTracker tracker = new PointTracker();
        tracker.setPoint(10, 20);
        assertEquals(new Point(10, 20), tracker.getPoint());
    }

    @Test
    public void testMove() {
        PointTracker tracker = new PointTracker();
        tracker.move(5, 5);
        assertEquals(new Point(5, 5), tracker.getPoint());
        
        tracker.move(-2, 10);
        assertEquals(new Point(3, 15), tracker.getPoint());
    }

    @Test
    public void testConcurrentMove() throws InterruptedException {
        PointTracker tracker = new PointTracker();
        int threads = 10;
        int increments = 1000;
        Thread[] ts = new Thread[threads];

        for (int i = 0; i < threads; i++) {
            ts[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    tracker.move(1, 1);
                }
            });
            ts[i].start();
        }

        for (Thread t : ts) {
            t.join();
        }

        assertEquals(new Point(threads * increments, threads * increments), tracker.getPoint());
    }
}
