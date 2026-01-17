package com.concurrency.evolution.ratelimiter;

/**
 * Rate Limiter Evolution - Step 1: Single-Threaded
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 📝 STEP 1: Get the algorithm right first (no thread safety)            │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * TODO: Implement lazy-refill Token Bucket for single-threaded use.
 */
public class Step1_SingleThreaded {
    
    private final int capacity;
    private final double refillRatePerMs;
    
    private double availableTokens;
    private long lastRefillTimeMs;
    
    public Step1_SingleThreaded(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerMs = refillRatePerSecond / 1000.0;
        this.availableTokens = capacity;
        this.lastRefillTimeMs = System.currentTimeMillis();
    }
    
    /**
     * TODO: Implement lazy refill + try acquire.
     */
    public boolean tryAcquire() {
        long now = System.currentTimeMillis();
        long elapsedMs = now - lastRefillTimeMs;
        
        // TODO: Calculate tokens to add
        // double tokensToAdd = elapsedMs * refillRatePerMs;
        // availableTokens = Math.min(capacity, availableTokens + tokensToAdd);
        // lastRefillTimeMs = now;
        
        // TODO: Try to consume
        // if (availableTokens >= 1) {
        //     availableTokens--;
        //     return true;
        // }
        
        return false;
    }
    
    public double getAvailableTokens() {
        return availableTokens;
    }
}
