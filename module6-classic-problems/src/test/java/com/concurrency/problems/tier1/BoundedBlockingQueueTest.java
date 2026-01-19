package com.concurrency.problems.tier1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BoundedBlockingQueue implementation.
 * 
 * 📝 NOTE: These tests will FAIL until you implement the skeleton correctly.
 * Use the failures to guide your implementation!
 * 
 * Run with: mvn test -Dtest=BoundedBlockingQueueTest
 */
class BoundedBlockingQueueTest {

    // ============================================
    // FUNCTIONAL TESTS (Single-threaded)
    // ============================================

    @Test
    void testPutAndTake_singleItem() throws InterruptedException {
        BoundedBlockingQueue<String> queue = new BoundedBlockingQueue<>(5);
        
        queue.put("hello");
        String result = queue.take();
        
        assertEquals("hello", result);
        assertTrue(queue.isEmpty());
    }

    @Test
    void testFifoOrder() throws InterruptedException {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(3);
        
        queue.put(1);
        queue.put(2);
        queue.put(3);
        
        assertEquals(1, queue.take());
        assertEquals(2, queue.take());
        assertEquals(3, queue.take());
    }

    @Test
    void testSizeTracking() throws InterruptedException {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(5);
        
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        
        queue.put(1);
        assertEquals(1, queue.size());
        assertFalse(queue.isEmpty());
        
        queue.put(2);
        assertEquals(2, queue.size());
        
        queue.take();
        assertEquals(1, queue.size());
    }

    // ============================================
    // BLOCKING TESTS
    // ============================================

    @Test
    @Timeout(5) // Fail if test takes more than 5 seconds
    void testPutBlocks_whenFull() throws InterruptedException {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(1);
        queue.put(1); // Fill the queue
        
        AtomicInteger putCount = new AtomicInteger(0);
        
        Thread producer = new Thread(() -> {
            try {
                queue.put(2); // Should block
                putCount.incrementAndGet();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        Thread.sleep(200); // Give time for thread to block
        
        // Producer should be WAITING (blocked on full queue)
        assertEquals(Thread.State.WAITING, producer.getState(),
            "Producer should be WAITING when queue is full");
        assertEquals(0, putCount.get(), "Put should not have completed yet");
        
        // Unblock by taking an item
        queue.take();
        producer.join(1000);
        
        assertEquals(1, putCount.get(), "Put should have completed after take");
    }

    @Test
    @Timeout(5)
    void testTakeBlocks_whenEmpty() throws InterruptedException {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(5);
        
        AtomicInteger takeResult = new AtomicInteger(-1);
        
        Thread consumer = new Thread(() -> {
            try {
                int value = queue.take(); // Should block
                takeResult.set(value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        consumer.start();
        Thread.sleep(200); // Give time for thread to block
        
        // Consumer should be WAITING (blocked on empty queue)
        assertEquals(Thread.State.WAITING, consumer.getState(),
            "Consumer should be WAITING when queue is empty");
        assertEquals(-1, takeResult.get(), "Take should not have completed yet");
        
        // Unblock by putting an item
        queue.put(42);
        consumer.join(1000);
        
        assertEquals(42, takeResult.get(), "Take should return the put value");
    }

    // ============================================
    // CONCURRENT STRESS TESTS
    // ============================================

    @RepeatedTest(3) // Run multiple times to catch race conditions
    @Timeout(10)
    void testConcurrentProducerConsumer() throws InterruptedException {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(10);
        int numProducers = 5;
        int numConsumers = 5;
        int itemsPerProducer = 100;
        
        ExecutorService executor = Executors.newFixedThreadPool(numProducers + numConsumers);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numProducers + numConsumers);
        AtomicInteger producedCount = new AtomicInteger(0);
        AtomicInteger consumedCount = new AtomicInteger(0);
        
        // Start producers
        for (int i = 0; i < numProducers; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < itemsPerProducer; j++) {
                        queue.put(j);
                        producedCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        // Start consumers
        for (int i = 0; i < numConsumers; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < itemsPerProducer; j++) {
                        queue.take();
                        consumedCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        // Start all threads at once
        startLatch.countDown();
        
        // Wait for completion
        assertTrue(endLatch.await(10, TimeUnit.SECONDS), "Threads should complete");
        executor.shutdown();
        
        int expected = numProducers * itemsPerProducer;
        assertEquals(expected, producedCount.get(), "All items should be produced");
        assertEquals(expected, consumedCount.get(), "All items should be consumed");
        assertTrue(queue.isEmpty(), "Queue should be empty at the end");
    }

    @Test
    void testCapacityRespected() throws InterruptedException {
        int capacity = 3;
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(capacity);
        
        queue.put(1);
        queue.put(2);
        queue.put(3);
        
        assertTrue(queue.isFull());
        assertEquals(capacity, queue.size());
    }
}
