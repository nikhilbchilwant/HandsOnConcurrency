package com.concurrency.labs.lab02;

/**
 * Lab 02: Visibility Fix with Volatile
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 💡 MOTIVATION: To fix the "zombie thread" problem, we need a way to     │
 * │ tell the JVM that a variable must be directly read/written to main      │
 * │ memory, bypassing CPU local caches.                                     │
 * │                                                                         │
 * │ 🎯 PROBLEM: You need to establish a "happens-before" relationship       │
 * │ between the stop() writer and the startWorker() reader without          │
 * │ using expensive heavyweight locking.                                    │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * TODO: Fix the visibility bug using the volatile keyword.
 * 
 * 📝 NOTE: volatile guarantees:
 *   1. VISIBILITY: Writes are immediately visible to all threads
 *   2. ORDERING: Prevents instruction reordering around volatile accesses
 * 
 * ⚠️ AVOID: volatile does NOT guarantee atomicity!
 *   - volatile int count; count++ is still NOT thread-safe!
 *   - Use volatile only for simple flags or single reads/writes
 * 
 * 💡 THINK: When is volatile sufficient vs when do you need synchronized?
 *   - Volatile: Single writer, or independent reads/writes
 *   - Synchronized: Compound actions (check-then-act, read-modify-write)
 */
public class VolatileFix {
    
    // TODO: Add the volatile keyword to fix the visibility problem
    // 🔑 HINT: Just add 'volatile' before 'boolean'
    private boolean running = true;
    
    /**
     * Starts a worker that loops while running is true.
     * 
     * 📝 NOTE: With volatile, each iteration will re-read 'running' from main memory,
     * ensuring the worker sees the updated value promptly.
     */
    public void startWorker() {
        new Thread(() -> {
            int iterations = 0;
            while (running) {
                iterations++;
            }
            System.out.println("Worker stopped after " + iterations + " iterations");
        }).start();
    }
    
    /**
     * Stops the worker. With volatile, this write is immediately visible.
     */
    public void stop() {
        running = false;
    }
    
    public boolean isRunning() {
        return running;
    }
}
