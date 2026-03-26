package com.concurrency.labs.lab12;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StampedLockLabTest {

    @Test
    public void testMoveAndDistance() {
        StampedLockLab lab = new StampedLockLab();
        assertEquals(0.0, lab.distanceFromOrigin());
        
        lab.move(3.0, 4.0);
        assertEquals(5.0, lab.distanceFromOrigin());
    }

    @Test
    public void testMoveIfAtOrigin() {
        StampedLockLab lab = new StampedLockLab();
        lab.moveIfAtOrigin(10.0, 10.0);
        assertEquals(Math.sqrt(200), lab.distanceFromOrigin());
        
        lab.move(-10.0, -10.0);
        assertEquals(0.0, lab.distanceFromOrigin());
        
        lab.moveIfAtOrigin(5.0, 0.0);
        assertEquals(5.0, lab.distanceFromOrigin());
    }
}
