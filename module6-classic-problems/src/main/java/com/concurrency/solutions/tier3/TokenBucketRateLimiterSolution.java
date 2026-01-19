package com.concurrency.solutions.tier3;

/**
 * SOLUTION: Token Bucket Rate Limiter
 * 
 * 📚 REFERENCES:
 * - "System Design Interview" by Alex Xu, Chapter 4 (Rate Limiting)
 * - https://en.wikipedia.org/wiki/Token_bucket
 * - Guava RateLimiter source code
 * 
 * KEY INSIGHT: Use LAZY REFILL - calculate tokens when needed, not via background thread.
 */
public class TokenBucketRateLimiterSolution {
    
    private final int capacity;
    private final double refillRatePerMs;
    
    private double availableTokens;
    private long lastRefillTimestamp;
    
    public TokenBucketRateLimiterSolution(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerMs = refillRatePerSecond / 1000.0;
        this.availableTokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }
    
    /**
     * Try to acquire one token using LAZY REFILL.
     * 
     * KEY POINTS:
     * 1. Calculate elapsed time since last refill
     * 2. Add tokens based on elapsed time (capped at capacity)
     * 3. Check and consume token if available
     * 4. Update timestamp
     */
    public synchronized boolean tryAcquire() {
        refill();
        
        if (availableTokens >= 1) {
            availableTokens -= 1;
            return true;
        }
        return false;
    }
    
    /**
     * Try to acquire multiple tokens (all-or-nothing).
     */
    public synchronized boolean tryAcquire(int tokens) {
        refill();
        
        if (availableTokens >= tokens) {
            availableTokens -= tokens;
            return true;
        }
        return false;
    }
    
    /**
     * Acquire one token, blocking if necessary.
     */
    public synchronized void acquire() throws InterruptedException {
        while (true) {
            refill();
            
            if (availableTokens >= 1) {
                availableTokens -= 1;
                return;
            }
            
            // Calculate wait time
            double tokensNeeded = 1 - availableTokens;
            long waitTimeMs = (long) Math.ceil(tokensNeeded / refillRatePerMs);
            
            if (waitTimeMs > 0) {
                wait(waitTimeMs);
            }
        }
    }
    
    /**
     * Lazy refill: calculate tokens based on elapsed time.
     */
    private void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTimestamp;
        
        if (elapsed > 0) {
            double tokensToAdd = elapsed * refillRatePerMs;
            availableTokens = Math.min(capacity, availableTokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }
    
    public synchronized double getAvailableTokens() {
        refill();
        return availableTokens;
    }
}
