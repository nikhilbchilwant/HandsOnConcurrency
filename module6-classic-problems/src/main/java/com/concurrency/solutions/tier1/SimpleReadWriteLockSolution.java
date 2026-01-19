package com.concurrency.solutions.tier1;

/**
 * SOLUTION: Custom Reader-Writer Lock
 * 
 * 📚 REFERENCES:
 * - "Java Concurrency in Practice" by Goetz, Chapter 13 (Explicit Locks)
 * - https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/ReentrantReadWriteLock.html
 * 
 * KEY INSIGHT: Track writeRequests to implement writer preference and prevent writer starvation.
 */
public class SimpleReadWriteLockSolution {
    
    private int readers = 0;        // Number of active readers
    private int writers = 0;        // Number of active writers (0 or 1)
    private int writeRequests = 0;  // Number of waiting writers (for writer preference)
    
    /**
     * Acquire the read lock.
     * 
     * KEY POINTS:
     * 1. Block if there are active writers OR waiting writers (writer preference)
     * 2. Without writeRequests check, readers could starve writers
     */
    public synchronized void lockRead() throws InterruptedException {
        // Writer preference: also check writeRequests
        while (writers > 0 || writeRequests > 0) {
            wait();
        }
        readers++;
    }
    
    /**
     * Release the read lock.
     * 
     * KEY POINT: When last reader unlocks, notify waiting writers
     */
    public synchronized void unlockRead() {
        readers--;
        if (readers == 0) {
            notifyAll(); // Wake up waiting writers
        }
    }
    
    /**
     * Acquire the write lock.
     * 
     * KEY POINTS:
     * 1. Increment writeRequests BEFORE waiting (signals to lockRead)
     * 2. Wait for all readers and writers to finish
     * 3. Use try-finally to ensure writeRequests is decremented
     */
    public synchronized void lockWrite() throws InterruptedException {
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
     * Release the write lock.
     * 
     * KEY POINT: notifyAll() to wake ALL waiting readers and writers
     */
    public synchronized void unlockWrite() {
        writers--;
        notifyAll();
    }
    
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
