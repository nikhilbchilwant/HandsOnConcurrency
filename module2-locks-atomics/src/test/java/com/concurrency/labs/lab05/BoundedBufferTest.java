package com.concurrency.labs.lab05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Lab 05: BoundedBufferWithLock
 */
class BoundedBufferTest {

    @Test
    @Timeout(5)
    void testProducerConsumer() throws InterruptedException {
        BoundedBufferWithLock<Integer> buffer = new BoundedBufferWithLock<>(5);
        int itemCount = 100;
        CountDownLatch producerDone = new CountDownLatch(1);
        CountDownLatch consumerDone = new CountDownLatch(1);
        AtomicInteger producedSum = new AtomicInteger(0);
        AtomicInteger consumedSum = new AtomicInteger(0);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < itemCount; i++) {
                    buffer.put(i);
                    producedSum.addAndGet(i);
                }
                producerDone.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < itemCount; i++) {
                    int val = buffer.take();
                    consumedSum.addAndGet(val);
                }
                consumerDone.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        assertTrue(producerDone.await(3, TimeUnit.SECONDS), "Producer should finish");
        assertTrue(consumerDone.await(3, TimeUnit.SECONDS), "Consumer should finish");
        assertEquals(producedSum.get(), consumedSum.get(), "Produced sum should match consumed sum");
    }

    @Test
    @Timeout(2)
    void testBlockingWhenFull() throws InterruptedException {
        BoundedBufferWithLock<Integer> buffer = new BoundedBufferWithLock<>(1);
        buffer.put(1); // Fill the buffer

        Thread producer = new Thread(() -> {
            try {
                buffer.put(2); // Should block
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();

        // Wait a bit to ensure producer is blocked
        Thread.sleep(100);
        assertEquals(Thread.State.WAITING, producer.getState(), "Producer should be WAITING");

        buffer.take(); // Make space
        producer.join(1000); // Should finish now
        assertEquals(Thread.State.TERMINATED, producer.getState(), "Producer should finish after space available");
    }

    @Test
    @Timeout(2)
    void testBlockingWhenEmpty() throws InterruptedException {
        BoundedBufferWithLock<Integer> buffer = new BoundedBufferWithLock<>(1);

        Thread consumer = new Thread(() -> {
            try {
                buffer.take(); // Should block
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();

        Thread.sleep(100);
        assertEquals(Thread.State.WAITING, consumer.getState(), "Consumer should be WAITING");

        buffer.put(1); // Provide item
        consumer.join(1000); // Should finish now
        assertEquals(Thread.State.TERMINATED, consumer.getState(), "Consumer should finish after item available");
    }
}
