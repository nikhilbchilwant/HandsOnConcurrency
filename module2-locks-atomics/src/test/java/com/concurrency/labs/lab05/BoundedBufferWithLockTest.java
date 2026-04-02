package com.concurrency.labs.lab05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class BoundedBufferWithLockTest {
    @Test
    public void testBufferWithLock() throws InterruptedException {
        BoundedBufferWithLock<Integer> buffer = new BoundedBufferWithLock<>(5);
        CountDownLatch latch = new CountDownLatch(1);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    buffer.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    assertEquals(i, buffer.take());
                }
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        latch.await();
    }
}
