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
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 🎤 INTERVIEW FOLLOW-UP QUESTIONS (Be ready for these!) │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ │
 * │ Q1: "Token Bucket vs Sliding Window - when would you use each?" │
 * │ → Token Bucket: Allows bursts (good for user experience) │
 * │ → Sliding Window: Smoother rate, better for strict SLAs │
 * │ → CLEVER INSIGHT: Token Bucket is O(1), Sliding Log is O(n) │
 * │ │
 * │ Q2: "Why use lazy refill instead of a background thread?" │
 * │ → Background thread wastes resources when system is idle │
 * │ → Lazy refill is O(1) per request, no extra threads │
 * │ → TRAP: Don't forget to cap at capacity (tokens shouldn't accumulate) │
 * │ │
 * │ Q3: "What if System.currentTimeMillis() is slow on your system?" │
 * │ → Use System.nanoTime() for elapsed time (monotonic, no syscall) │
 * │ → But nanoTime() has no absolute meaning - only for deltas │
 * │ → ADVANCED: Consider using Instant.now() for microsecond precision │
 * │ │
 * │ Q4: "How would you implement this for distributed systems?" │
 * │ → Central store (Redis) with atomic operations │
 * │ → Lua script for atomic refill + consume │
 * │ → TRADE-OFF: Network latency vs global consistency │
 * │ → ALTERNATIVE: Local rate limiters with eventual consistency │
 * │ │
 * │ Q5: "How do you handle clock skew in distributed rate limiting?" │
 * │ → Store timestamps on the server (Redis), not client │
 * │ → Use relative time (TTLs) instead of absolute timestamps │
 * │ → INSIGHT: This is why Redis SETNX + EXPIRE is popular │
 * │ │
 * │ Q6: "What if I need different rate limits per user tier?" │
 * │ → Store per-user bucket configuration │
 * │ → Use a Map<UserId, RateLimiter> with lazy initialization │
 * │ → WATCH OUT: Memory leak if you don't expire inactive users! │
 * │ │
 * │ Q7: "How do you gracefully degrade when rate limited?" │
 * │ → Return Retry-After header with seconds until token available │
 * │ → Use backoff: retryAfter = (1 - availableTokens) / refillRate │
 * │ → PRODUCTION: Consider returning 429 with exponential backoff hint │
 * │ │
 * │ Q8: "Can you implement acquire() that blocks until token available?" │
 * │ → Calculate wait time, use wait(ms), but watch for spurious wakeups! │
 * │ → ADVANCED: Guava's RateLimiter uses SmoothBursty for this │
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
     * ⚠️ COMMON MISTAKES:
     * 1. Using a background thread for refilling (overcomplicated!)
     * 2. Not synchronizing access to shared state
     * 3. Forgetting to update lastRefillTimestamp
     * 
     * 💡 THINK: Use LAZY REFILL - calculate tokens when needed, not continuously.
     * 
     * @return true if token acquired, false if rate limited
     */
    public synchronized boolean tryAcquire() {
        // TODO: Implement lazy refill + token acquisition
        
        // Step 1: Calculate elapsed time since last refill
        // long now = System.currentTimeMillis();
        // long elapsed = now - lastRefillTimestamp;
        
        // Step 2: Add tokens based on elapsed time (capped at capacity)
        // 💡 THINK: Why cap at capacity? To prevent token accumulation during idle periods
        
        // Step 3: Check and consume token if available
        
        // Step 4: Update timestamp
        
        throw new UnsupportedOperationException("TODO: Implement this method");
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
        throw new UnsupportedOperationException("TODO: Implement this method");
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
        throw new UnsupportedOperationException("TODO: Implement this method");
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
