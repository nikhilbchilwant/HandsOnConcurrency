package com.concurrency.labs.lab01;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class UnsafeCounterTest {
    @Test
    public void testUnsafeIncrement() throws InterruptedException {
        UnsafeCounter counter = new UnsafeCounter();
        int numThreads = 100;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        assertTrue(counter.getCount() <= numThreads * incrementsPerThread, "Counter should be less than or equal to max due to race conditions");
    }
}
