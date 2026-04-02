package com.concurrency.labs.lab07;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class LockFreeStackTest {
    @Test
    public void testLockFreeStack() throws InterruptedException {
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        int numThreads = 10;
        int itemsPerThread = 100;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                for (int j = 0; j < itemsPerThread; j++) {
                    stack.push(j);
                }
                latch.countDown();
            }).start();
        }

        latch.await();

        int count = 0;
        while (stack.pop() != null) {
            count++;
        }
        assertEquals(numThreads * itemsPerThread, count);
    }
}
