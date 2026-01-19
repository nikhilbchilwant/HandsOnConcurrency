package com.concurrency.problems.tier3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Tests for SimpleThreadPool implementation.
 * 
 * Run with: mvn test -Dtest=SimpleThreadPoolTest
 */
class SimpleThreadPoolTest {

    @Test
    @Timeout(5)
    void testExecuteAndRun() throws InterruptedException {
        SimpleThreadPool pool = new SimpleThreadPool(2);
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(3);
        
        for (int i = 0; i < 3; i++) {
            pool.execute(() -> {
                counter.incrementAndGet();
                latch.countDown();
            });
        }
        
        assertTrue(latch.await(3, TimeUnit.SECONDS), "Tasks should complete");
        assertEquals(3, counter.get(), "All tasks should execute");
        
        pool.shutdown();
    }

    @Test
    @Timeout(5)
    void testGracefulShutdown() throws InterruptedException {
        SimpleThreadPool pool = new SimpleThreadPool(2);
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch allSubmitted = new CountDownLatch(1);
        
        // Submit tasks that take a bit of time
        for (int i = 0; i < 5; i++) {
            pool.execute(() -> {
                try {
                    allSubmitted.await();
                    Thread.sleep(50);
                    counter.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        allSubmitted.countDown();
        pool.shutdown();
        pool.awaitTermination();
        
        // Tasks should have completed
        assertTrue(counter.get() > 0, "Some tasks should complete before shutdown");
    }

    @Test
    @Timeout(10)
    void testConcurrentExecute() throws InterruptedException {
        SimpleThreadPool pool = new SimpleThreadPool(4);
        int numTasks = 100;
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(numTasks);
        
        ExecutorService submitter = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < numTasks; i++) {
            submitter.submit(() -> {
                pool.execute(() -> {
                    counter.incrementAndGet();
                    latch.countDown();
                });
            });
        }
        
        assertTrue(latch.await(5, TimeUnit.SECONDS), "All tasks should complete");
        assertEquals(numTasks, counter.get(), "All tasks should execute");
        
        submitter.shutdown();
        pool.shutdown();
    }

    @Test
    void testPoolSize() {
        SimpleThreadPool pool = new SimpleThreadPool(5);
        assertEquals(5, pool.getPoolSize());
        pool.shutdown();
    }

    @Test
    @Timeout(5)
    void testTaskExceptionDoesNotKillWorker() throws InterruptedException {
        SimpleThreadPool pool = new SimpleThreadPool(1);
        AtomicInteger successCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(2);
        
        // First task throws exception
        pool.execute(() -> {
            latch.countDown();
            throw new RuntimeException("Intentional failure");
        });
        
        // Give time for first task to run
        Thread.sleep(100);
        
        // Second task should still execute
        pool.execute(() -> {
            successCount.incrementAndGet();
            latch.countDown();
        });
        
        assertTrue(latch.await(3, TimeUnit.SECONDS), "Both tasks should run");
        assertEquals(1, successCount.get(), "Second task should succeed despite first failing");
        
        pool.shutdown();
    }
}
