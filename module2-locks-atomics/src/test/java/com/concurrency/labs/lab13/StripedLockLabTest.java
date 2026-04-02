package com.concurrency.labs.lab13;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class StripedLockLabTest {
    @Test
    public void testStripedLock() throws InterruptedException {
        StripedLockLab lab = new StripedLockLab(100, 10);
        int numThreads = 10;
        int incrementsPerThread = 100;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    lab.increment((threadIndex * 10 + j) % 100);
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        assertEquals(numThreads * incrementsPerThread, lab.sum());
    }
}
