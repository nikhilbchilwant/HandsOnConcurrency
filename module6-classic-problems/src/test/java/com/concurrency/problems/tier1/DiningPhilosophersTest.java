package com.concurrency.problems.tier1;

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
 * Tests for DiningPhilosophers implementation.
 * 
 * Run with: mvn test -Dtest=DiningPhilosophersTest
 */
class DiningPhilosophersTest {

    @Test
    @Timeout(10)
    void testNoDeadlock() throws InterruptedException {
        int numPhilosophers = 5;
        int mealsPerPhilosopher = 10;
        DiningPhilosophers dp = new DiningPhilosophers(numPhilosophers);
        
        AtomicInteger totalMeals = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(numPhilosophers);
        
        for (int i = 0; i < numPhilosophers; i++) {
            final int id = i;
            new Thread(() -> {
                for (int j = 0; j < mealsPerPhilosopher; j++) {
                    dp.eat(id);
                    totalMeals.incrementAndGet();
                }
                done.countDown();
            }, "Philosopher-" + i).start();
        }
        
        // If there's a deadlock, this will timeout
        assertTrue(done.await(8, TimeUnit.SECONDS), 
            "All philosophers should complete (no deadlock)");
        
        assertEquals(numPhilosophers * mealsPerPhilosopher, totalMeals.get(),
            "All meals should be eaten");
    }

    @Test
    @Timeout(10)
    void testConcurrentEating() throws InterruptedException {
        int numPhilosophers = 5;
        DiningPhilosophers dp = new DiningPhilosophers(numPhilosophers);
        
        ExecutorService executor = Executors.newFixedThreadPool(numPhilosophers);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numPhilosophers);
        AtomicInteger eatCount = new AtomicInteger(0);
        
        for (int i = 0; i < numPhilosophers; i++) {
            final int id = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < 5; j++) {
                        dp.eat(id);
                        eatCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        startLatch.countDown();
        assertTrue(endLatch.await(8, TimeUnit.SECONDS));
        executor.shutdown();
        
        assertEquals(25, eatCount.get(), "All philosophers should eat 5 times each");
    }

    @Test
    void testSinglePhilosopherCanEat() {
        DiningPhilosophers dp = new DiningPhilosophers(5);
        
        // Should not throw or deadlock
        dp.eat(0);
        dp.eat(2);
        dp.eat(4);
    }

    @Test
    @Timeout(5)
    void testHighContention() throws InterruptedException {
        // Only 2 philosophers but lots of meals - high contention for forks
        DiningPhilosophers dp = new DiningPhilosophers(2);
        AtomicInteger eatCount = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(2);
        
        for (int i = 0; i < 2; i++) {
            final int id = i;
            new Thread(() -> {
                for (int j = 0; j < 20; j++) {
                    dp.eat(id);
                    eatCount.incrementAndGet();
                }
                done.countDown();
            }).start();
        }
        
        assertTrue(done.await(4, TimeUnit.SECONDS), "Should complete without deadlock");
        assertEquals(40, eatCount.get());
    }
}
