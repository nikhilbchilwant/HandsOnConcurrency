package com.concurrency.problems.tier3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Tests for TokenBucketRateLimiter implementation.
 * 
 * Run with: mvn test -Dtest=TokenBucketRateLimiterTest
 */
class TokenBucketRateLimiterTest {

    @Test
    void testAcquireWithinCapacity() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 1.0);
        
        // Should be able to acquire all 5 tokens immediately
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire(), "Should acquire token " + (i + 1));
        }
    }

    @Test
    void testAcquireExceedsCapacity() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(3, 1.0);
        
        // Consume all tokens
        for (int i = 0; i < 3; i++) {
            assertTrue(limiter.tryAcquire());
        }
        
        // Next acquire should fail
        assertFalse(limiter.tryAcquire(), "Should reject when bucket is empty");
    }

    @Test
    @Timeout(5)
    void testTokenRefillOverTime() throws InterruptedException {
        // 10 tokens per second = 1 token every 100ms
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 10.0);
        
        // Consume all tokens
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire());
        }
        assertFalse(limiter.tryAcquire(), "Bucket should be empty");
        
        // Wait for refill (100ms should give us ~1 token)
        Thread.sleep(150);
        
        assertTrue(limiter.tryAcquire(), "Should have refilled at least 1 token");
    }

    @Test
    void testTokensCapAtMaximum() throws InterruptedException {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(3, 100.0);
        
        // Wait a long time - tokens should not exceed capacity
        Thread.sleep(200);
        
        double tokens = limiter.getAvailableTokens();
        assertTrue(tokens <= 3.0, "Tokens should be capped at capacity, but was: " + tokens);
    }

    @Test
    void testAcquireMultipleTokens() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 1.0);
        
        // Acquire 5 tokens at once
        assertTrue(limiter.tryAcquire(5), "Should acquire 5 tokens");
        
        // Acquire 5 more
        assertTrue(limiter.tryAcquire(5), "Should acquire remaining 5 tokens");
        
        // Should fail - no tokens left
        assertFalse(limiter.tryAcquire(1), "Should reject when empty");
    }

    @Test
    void testAcquireMultipleAllOrNothing() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 1.0);
        
        // Consume 3 tokens
        limiter.tryAcquire(3);
        
        // Try to acquire 3 more (only 2 available) - should fail completely
        assertFalse(limiter.tryAcquire(3), "Should reject if not enough tokens (all-or-nothing)");
        
        // The 2 remaining tokens should still be there
        assertTrue(limiter.tryAcquire(2), "Should still have 2 tokens");
    }

    @Test
    @Timeout(10)
    void testConcurrentAcquire() throws InterruptedException {
        // High capacity to avoid timing issues
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(100, 1000.0);
        
        int numThreads = 10;
        int acquiresPerThread = 10;
        AtomicInteger successCount = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < acquiresPerThread; j++) {
                        if (limiter.tryAcquire()) {
                            successCount.incrementAndGet();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        startLatch.countDown();
        assertTrue(endLatch.await(5, TimeUnit.SECONDS));
        executor.shutdown();
        
        // All 100 tokens should be acquired exactly
        assertEquals(100, successCount.get(), "Should acquire exactly 100 tokens");
    }
}
