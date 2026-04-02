package com.concurrency.labs.lab02;

/**
 * Lab 02: Visibility Bug Demonstration
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 💡 MOTIVATION: In modern systems, CPUs have multiple cores with local   │
 * │ caches. To optimize performance, the JVM might not immediately write    │
 * │ variable changes to main memory, or re-read them in every loop.         │
 * │                                                                         │
 * │ 🎯 PROBLEM: This class contains a "zombie thread" bug where a worker    │
 * │ never sees a "stop" signal from another thread. You need to identify    │
 * │ why the 'running' flag change isn't reaching the worker.                │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * This class demonstrates a visibility problem where one thread's
 * writes may not be visible to another thread.
 * 
 * 📝 NOTE: Without volatile or synchronization, the JVM is allowed to:
 *   - Cache variables in CPU registers
 *   - Reorder instructions for optimization
 *   
 * Run the test - the reader thread may NEVER see running = false!
 * 
 * 💡 THINK: Why doesn't this always fail? The JVM's behavior depends on
 * hardware, JIT compilation, and timing. That's what makes concurrency bugs
 * so dangerous - they may only appear in production under load!
 */
public class VisibilityBug {
    
    // ⚠️ AVOID: This field is read by one thread and written by another
    // without any memory synchronization - classic visibility bug!
    private boolean running = true;
    
    /**
     * Starts a worker that loops while running is true.
     * 
     * 📝 NOTE: The JIT compiler might hoist the read of 'running' out of the loop,
     * effectively turning this into: if (running) { while(true) {...} }
     */
    public void startWorker() {
        throw new UnsupportedOperationException("TODO");
/*
new Thread(() -> {
            int iterations = 0;
            // 💡 THINK: Why might this loop never terminate even after stop() is called?
            while (running) {
                iterations++;
                // Without any synchronization point, the thread may never
                // re-read 'running' from main memory
            }
            System.out.println("Worker stopped after " + iterations + " iterations");
        }).start();
*/
    }
    
    /**
     * Attempts to stop the worker.
     * 
     * 📝 NOTE: This write might never be visible to the worker thread!
     */
    public void stop() {
        throw new UnsupportedOperationException("TODO");
    }
    
    public boolean isRunning() {
        throw new UnsupportedOperationException("TODO");
    }
}
