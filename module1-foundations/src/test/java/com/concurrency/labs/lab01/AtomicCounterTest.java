package com.concurrency.labs.lab01;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class AtomicCounterTest {
    @Test
    public void testAtomicIncrement() throws InterruptedException {
        AtomicCounter counter = new AtomicCounter();
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
        assertEquals(numThreads * incrementsPerThread, counter.getCount(), "Counter should not lose updates");
    }
}
