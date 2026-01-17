package com.concurrency.evolution.ratelimiter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Rate Limiter Evolution - Step 3: Use ReentrantLock
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 📝 STEP 3: More flexibility with explicit locks                        │
 * │ Advantages: tryLock(), lockInterruptibly(), fairness option            │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * TODO: Replace synchronized with ReentrantLock.
 */
public class Step3_LockBased {
    
    private final int capacity;
    private final double refillRatePerMs;
    private final Lock lock = new ReentrantLock();
    
    private double availableTokens;
    private long lastRefillTimeMs;
    
    public Step3_LockBased(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerMs = refillRatePerSecond / 1000.0;
        this.availableTokens = capacity;
        this.lastRefillTimeMs = System.currentTimeMillis();
    }
    
    /**
     * TODO: Use lock.lock() and lock.unlock() in finally.
     */
    public boolean tryAcquire() {
        lock.lock();
        try {
            // TODO: Same logic as Step 2
            return false;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * BONUS: Non-blocking tryAcquire.
     */
    public boolean tryAcquireNonBlocking() {
        if (lock.tryLock()) {
            try {
                // TODO: Same logic
                return false;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
    
    public double getAvailableTokens() {
        lock.lock();
        try {
            return availableTokens;
        } finally {
            lock.unlock();
        }
    }
}
