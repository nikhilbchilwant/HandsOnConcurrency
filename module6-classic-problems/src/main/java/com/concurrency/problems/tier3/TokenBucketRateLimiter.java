package com.concurrency.problems.tier3;

/**
 * Classic Problem #7: Token Bucket Rate Limiter
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ✅ INTERVIEW RELEVANCE: HIGH PRIORITY │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ Companies: Rubrik, Amazon, Stripe, Cloudflare, any API company │
 * │ Frequency: HIGH - Common in system design AND coding rounds │
 * │ Time Target: Implement from scratch in < 25 minutes │
 * │ │
 * │ WHY THIS IS CRITICAL: │
 * │ - Every API needs rate limiting │
 * │ - Tests time-based reasoning + thread safety │
 * │ - Common follow-up: "How would you make this distributed?" │
 * │ │
 * │ INTERVIEW PROGRESSION: │
 * │ 1. Start with single-threaded Token Bucket │
 * │ 2. Add synchronized for thread safety │
 * │ 3. Discuss Sliding Window as alternative │
 * │ 4. Discuss distributed rate limiting (Redis + Lua scripts) │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * TODO: Implement a rate limiter using the Token Bucket algorithm.
 * 
 * 📝 NOTE: Token Bucket works like this:
 * - Bucket has a maximum capacity of tokens
 * - Tokens are added at a fixed rate (e.g., 10 per second)
 * - Each request consumes one token
 * - If no tokens available, request is rejected (or blocked)
 * 
 * ⚠️ AVOID: Using a background thread to add tokens every second!
 * This is inefficient and adds unnecessary complexity.
 * 
 * 💡 THINK: Use LAZY REFILL instead!
 * When a request arrives, calculate how many tokens SHOULD have been
 * added since the last request based on elapsed time.
 * 
 * tokensToAdd = (currentTime - lastRefillTime) * refillRate
 * availableTokens = min(capacity, availableTokens + tokensToAdd)
 * 
 * This is how production rate limiters work (Guava RateLimiter, etc.)
 */
public class TokenBucketRateLimiter {
    
    private final int capacity;           // Maximum tokens in bucket
    private final double refillRatePerMs; // Tokens added per millisecond
    
    private double availableTokens;       // Current token count
    private long lastRefillTimestamp;     // Time of last refill
    
    /**
     * Creates a rate limiter with given capacity and refill rate.
     * 
     * @param capacity maximum tokens the bucket can hold
     * @param refillRatePerSecond tokens added per second
     */
    public TokenBucketRateLimiter(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerMs = refillRatePerSecond / 1000.0;
        this.availableTokens = capacity; // Start full
        this.lastRefillTimestamp = System.currentTimeMillis();
    }
    
    /**
     * TODO: Try to acquire one token, returning immediately.
     * 
     * 🔑 HINT - Implementation pattern:
     *   1. synchronized(this) - must be thread-safe!
     *   2. Refill tokens based on elapsed time
     *   3. Check if at least 1 token available
     *   4. If yes, consume token and return true
     *   5. If no, return false
     * 
     * 📝 NOTE: The key insight is LAZY REFILL:
     *   long now = System.currentTimeMillis();
     *   long elapsed = now - lastRefillTimestamp;
     *   double tokensToAdd = elapsed * refillRatePerMs;
     *   availableTokens = Math.min(capacity, availableTokens + tokensToAdd);
     *   lastRefillTimestamp = now;
     * 
     * @return true if token acquired, false if rate limited
     */
    public synchronized boolean tryAcquire() {
        // TODO: Implement lazy refill + token acquisition
        
        // Step 1: Calculate elapsed time since last refill
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTimestamp;
        
        // Step 2: Add tokens based on elapsed time (capped at capacity)
        // 💡 THINK: Why cap at capacity? To prevent token accumulation during idle periods
        
        // Step 3: Check and consume token if available
        
        // Step 4: Update timestamp
        
        return false; // TODO: Replace with actual logic
    }
    
    /**
     * TODO: Try to acquire multiple tokens.
     * 
     * 💡 THINK: Should this be atomic? What if someone asks for 5 tokens
     * but only 3 are available?
     * 
     * @param tokens number of tokens to acquire
     * @return true if all tokens acquired, false otherwise
     */
    public synchronized boolean tryAcquire(int tokens) {
        // TODO: Implement acquiring multiple tokens
        // ⚠️ AVOID: Acquiring partial tokens - it should be all or nothing!
        return false;
    }
    
    /**
     * TODO (BONUS): Acquire one token, blocking if necessary.
     * 
     * 🔑 HINT: Calculate how long to wait for a token to become available:
     *   if (availableTokens < 1) {
     *       double tokensNeeded = 1 - availableTokens;
     *       long waitTimeMs = (long) (tokensNeeded / refillRatePerMs);
     *       wait(waitTimeMs);
     *   }
     * 
     * ⚠️ AVOID: Busy waiting (while loop without wait)!
     */
    public synchronized void acquire() throws InterruptedException {
        // TODO: Implement blocking acquire
        // 💡 THINK: Is there a race condition if multiple threads wait?
    }
    
    /**
     * Returns the current number of available tokens (approximate).
     */
    public synchronized double getAvailableTokens() {
        // Refill first to get accurate count
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTimestamp;
        double tokensToAdd = elapsed * refillRatePerMs;
        return Math.min(capacity, availableTokens + tokensToAdd);
    }
}
