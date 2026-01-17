package com.concurrency.evolution.ratelimiter;

/**
 * Rate Limiter Evolution - Step 2: Add Synchronized
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 📝 STEP 2: Make it thread-safe with synchronized                       │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * TODO: Copy Step 1 and add synchronized keyword.
 */
public class Step2_Synchronized {
    
    private final int capacity;
    private final double refillRatePerMs;
    
    private double availableTokens;
    private long lastRefillTimeMs;
    
    public Step2_Synchronized(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerMs = refillRatePerSecond / 1000.0;
        this.availableTokens = capacity;
        this.lastRefillTimeMs = System.currentTimeMillis();
    }
    
    /**
     * TODO: Same as Step 1, but with synchronized.
     */
    public synchronized boolean tryAcquire() {
        // TODO: Copy from Step 1 - synchronized handles thread safety
        return false;
    }
    
    public synchronized double getAvailableTokens() {
        return availableTokens;
    }
}
