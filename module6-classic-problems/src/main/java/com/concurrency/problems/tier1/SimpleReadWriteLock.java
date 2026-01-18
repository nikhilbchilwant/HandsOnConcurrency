package com.concurrency.problems.tier1;

/**
 * Classic Problem #2: Custom Reader-Writer Lock
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ✅ INTERVIEW RELEVANCE: HIGH PRIORITY │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ Companies: Dropbox, Rubrik, Google │
 * │ Frequency: HIGH - Tests lock design and starvation prevention │
 * │ Time Target: Implement from scratch in < 25 minutes │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 🎤 INTERVIEW FOLLOW-UP QUESTIONS (Be ready for these!) │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ │
 * │ Q1: "Your impl has writer preference. What about reader starvation?" │
 * │ → Yes, continuous writers can starve readers! │
 * │ → SOLUTION: Alternate between reader/writer batches, or use fair mode │
 * │ → INSIGHT: ReentrantReadWriteLock(true) gives FIFO fairness │
 * │ │
 * │ Q2: "Can a reader upgrade to a writer lock?" │
 * │ → NO in most impls - would cause deadlock if 2 readers try! │
 * │ → Reader1 holds read, waits for write. Reader2 same = DEADLOCK │
 * │ → SOLUTION: Release read lock first, then acquire write lock │
 * │ → ADVANCED: StampedLock supports tryConvertToWriteLock() │
 * │ │
 * │ Q3: "Can a writer downgrade to a reader lock?" │
 * │ → YES - safe because writer has exclusive access │
 * │ → Pattern: Acquire write, do writes, acquire read, release write │
 * │ → INSIGHT: Downgrade avoids "gap" where another writer could sneak in │
 * │ │
 * │ Q4: "What's the difference between this and ReentrantReadWriteLock?" │
 * │ → RRWL supports: reentrancy, fair mode, tryLock, lockInterruptibly │
 * │ → This impl: simpler but no reentrancy (risk of self-deadlock!) │
 * │ → TRAP: If same thread calls lockRead() twice, it deadlocks here │
 * │ │
 * │ Q5: "When is ReadWriteLock slower than a single lock?" │
 * │ → When reads are short and contention is low │
 * │ → RW lock has overhead of tracking reader count │
 * │ → RULE: Only use RWLock when read time >> write time │
 * │ │
 * │ Q6: "What's StampedLock and when would you use it?" │
 * │ → Java 8 lock with optimistic reads (no locking for reads!) │
 * │ → Pattern: try optimistic, validate, fall back to read lock │
 * │ → USE WHEN: Read-heavy workload with very rare writes │
 * │ → TRAP: Not reentrant, more complex API │
 * │ │
 * │ Q7: "Why use notifyAll() instead of notify() in unlockWrite()?" │
 * │ → Multiple readers might be waiting - wake them all! │
 * │ → notify() wakes only one - other readers stay blocked │
 * │ → ADVANCED: Could optimize with separate conditions for readers/writers│
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * TODO: Implement a reader-writer lock from scratch.
 * 
 * 📝 NOTE: Rules for reader-writer locks:
 * - Multiple readers can hold the lock simultaneously
 * - Only one writer can hold the lock (exclusive)
 * - Writers and readers are mutually exclusive
 * 
 * ⚠️ AVOID: Writer starvation!
 * If readers keep coming, writers might wait forever.
 * 
 * 💡 THINK: How would you implement "writer preference"?
 * When a writer is waiting, new readers should block too!
 * 
 * @see java.util.concurrent.locks.ReentrantReadWriteLock for production use
 */
public class SimpleReadWriteLock {
    
    private int readers = 0;        // Number of active readers
    private int writers = 0;        // Number of active writers (0 or 1)
    private int writeRequests = 0;  // Number of waiting writers
    
    // 💡 THINK: Why track writeRequests separately from writers?
    // This lets us implement writer preference to prevent starvation!
    
    /**
     * TODO: Acquire the read lock.
     * 
     * 🔑 HINT: Readers can proceed if:
     *   - No active writers (writers == 0)
     *   - No waiting writers (writeRequests == 0) - for writer preference
     * 
     * 📝 NOTE: Without the writeRequests check, readers could starve writers!
     */
    public synchronized void lockRead() throws InterruptedException {
        // TODO: Implement read lock acquisition
        // Step 1: While there are writers OR waiting writers, wait
        // Step 2: Increment readers count
        
        // ⚠️ AVOID: This simple version allows reader starvation of writers:
        // while (writers > 0) { wait(); }
        // 
        // Better: Also check writeRequests to give writers priority
        while (writers > 0 || writeRequests > 0) {
            wait();
        }
        readers++;
    }
    
    /**
     * TODO: Release the read lock.
     * 
     * 📝 NOTE: When the last reader unlocks, notify waiting writers!
     */
    public synchronized void unlockRead() {
        // TODO: Implement read lock release
        readers--;
        if (readers == 0) {
            notifyAll(); // Wake up waiting writers
        }
    }
    
    /**
     * TODO: Acquire the write lock.
     * 
     * 🔑 HINT: Writers must wait for:
     *   - All readers to finish (readers == 0)
     *   - Any active writer to finish (writers == 0)
     * 
     * 💡 THINK: Why increment writeRequests before waiting?
     *   This signals to lockRead() that a writer is waiting!
     */
    public synchronized void lockWrite() throws InterruptedException {
        // TODO: Implement write lock acquisition
        writeRequests++;
        try {
            while (readers > 0 || writers > 0) {
                wait();
            }
            writers++;
        } finally {
            writeRequests--;
        }
    }
    
    /**
     * TODO: Release the write lock.
     */
    public synchronized void unlockWrite() {
        // TODO: Implement write lock release
        writers--;
        notifyAll(); // Wake up ALL waiting readers and writers
        
        // 💡 THINK: Could we use notify() instead of notifyAll()?
        // What would happen if we woke only one waiting thread?
    }
    
    // Diagnostic methods
    public synchronized int getReaderCount() {
        return readers;
    }
    
    public synchronized int getWriterCount() {
        return writers;
    }
    
    public synchronized int getWriteRequestCount() {
        return writeRequests;
    }
}
