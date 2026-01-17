package com.concurrency.debug.broken;

/**
 * 🔴 BROKEN CODE - FIND THE BUGS!
 * 
 * This is a rate limiter with INTENTIONAL BUGS.
 * 
 * Expected bugs to find: 2
 * Time target: < 5 minutes
 */
public class BrokenRateLimiter {
    
    private final int maxRequests;
    private final long windowMs;
    private int currentCount;
    private long windowStartTime;
    
    public BrokenRateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
        this.currentCount = 0;
        this.windowStartTime = System.currentTimeMillis();
    }
    
    /**
     * Try to acquire a permit.
     * 
     * 🔴 BUG #1: CHECK-THEN-ACT RACE CONDITION (no synchronization!)
     * 🔴 BUG #2: windowStartTime not updated when window resets
     */
    public boolean tryAcquire() {
        long now = System.currentTimeMillis();
        
        if (now - windowStartTime >= windowMs) {
            currentCount = 0;
            // BUG #2: Missing windowStartTime = now;
        }
        
        // BUG #1: This whole method is not synchronized!
        // Check-then-act race between if and increment
        if (currentCount < maxRequests) {
            currentCount++;
            return true;
        }
        
        return false;
    }
}
