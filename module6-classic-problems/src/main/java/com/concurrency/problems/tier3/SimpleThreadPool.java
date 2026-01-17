package com.concurrency.problems.tier3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Classic Problem #8: Custom Thread Pool
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ✅ INTERVIEW RELEVANCE: HIGH PRIORITY │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ Companies: Uber, Amazon, Dropbox │
 * │ Frequency: HIGH - Tests understanding of Executor framework │
 * │ Time Target: Implement from scratch in < 30 minutes │
 * │ │
 * │ WHY THIS IS CRITICAL: │
 * │ - Foundation for understanding ThreadPoolExecutor │
 * │ - Tests BlockingQueue + Worker thread pattern │
 * │ - Common follow-up: "How would you implement shutdown?" │
 * │ │
 * │ INTERVIEW TIP: Mention rejection policies (CallerRunsPolicy, etc.) │
 * │ as a follow-up to show production awareness. │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * TODO: Implement a fixed-size thread pool from scratch.
 * 
 * 📝 NOTE: This is how java.util.concurrent.ThreadPoolExecutor works!
 * Understanding this helps you configure thread pools correctly.
 * 
 * Components needed:
 * 1. A BlockingQueue to hold submitted tasks
 * 2. A fixed number of Worker threads
 * 3. Each Worker loops forever, taking tasks from the queue
 * 
 * 💡 THINK: Why use a BlockingQueue instead of a regular Queue?
 * - BlockingQueue.take() blocks when empty (no busy waiting!)
 * - BlockingQueue.put() can block when full (backpressure)
 * 
 * ⚠️ AVOID: Busy waiting!
 * // BAD - wastes CPU cycles
 * while (queue.isEmpty()) { // spin }
 * 
 * // GOOD - thread sleeps until item available
 * task = queue.take();
 */
public class SimpleThreadPool {
    
    private final int poolSize;
    private final BlockingQueue<Runnable> taskQueue;
    private final List<Worker> workers;
    private volatile boolean isShutdown = false;
    
    /**
     * Creates a thread pool with the specified number of threads.
     * 
     * @param poolSize number of worker threads
     */
    public SimpleThreadPool(int poolSize) {
        this.poolSize = poolSize;
        this.taskQueue = new LinkedBlockingQueue<>();
        this.workers = new ArrayList<>(poolSize);
        
        // TODO: Create and start worker threads
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker("Worker-" + i);
            workers.add(worker);
            worker.start();
        }
    }
    
    /**
     * TODO: Submit a task for execution.
     * 
     * 🔑 HINT: Just add to the BlockingQueue!
     * Workers will pick it up automatically.
     * 
     * @param task the task to execute
     * @throws IllegalStateException if pool is shutdown
     */
    public void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool is shutdown");
        }
        
        // TODO: Add task to queue
        // 💡 THINK: Should we use offer() or put()?
        //   - offer(): Returns false if queue is full
        //   - put(): Blocks until space available
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while submitting task", e);
        }
    }
    
    /**
     * TODO: Shutdown the pool gracefully.
     * 
     * 📝 NOTE: Graceful shutdown means:
     *   1. Stop accepting new tasks
     *   2. Let current tasks complete
     *   3. Interrupt workers waiting for tasks
     * 
     * 💡 THINK: Why interrupt workers?
     *   They might be blocked on queue.take() - interrupt wakes them up
     *   to check the shutdown flag.
     */
    public void shutdown() {
        isShutdown = true;
        
        // TODO: Interrupt all workers so they can exit
        for (Worker worker : workers) {
            worker.interrupt();
        }
    }
    
    /**
     * Wait for all workers to complete.
     */
    public void awaitTermination() throws InterruptedException {
        for (Worker worker : workers) {
            worker.join();
        }
    }
    
    /**
     * TODO: The Worker thread implementation.
     * 
     * 🔑 HINT: The run() method should:
     *   1. Loop while not shutdown (or has pending tasks)
     *   2. Take a task from the queue (blocks if empty)
     *   3. Execute the task
     *   4. Handle exceptions gracefully (don't let one bad task kill the worker!)
     */
    private class Worker extends Thread {
        
        Worker(String name) {
            super(name);
        }
        
        @Override
        public void run() {
            // TODO: Implement the worker loop
            while (!isShutdown || !taskQueue.isEmpty()) {
                try {
                    // 📝 NOTE: take() blocks until a task is available
                    Runnable task = taskQueue.take();
                    
                    // Execute the task
                    // ⚠️ AVOID: Letting exceptions propagate and kill the worker!
                    try {
                        task.run();
                    } catch (Exception e) {
                        // Log but don't rethrow - worker should continue
                        System.err.println("Task failed: " + e.getMessage());
                    }
                    
                } catch (InterruptedException e) {
                    // Interrupted - check shutdown flag
                    // 💡 THINK: Should we restore the interrupt flag?
                    // Usually yes, but here we're checking isShutdown anyway
                    if (isShutdown) {
                        break;
                    }
                }
            }
            System.out.println(getName() + " terminated");
        }
    }
    
    public int getPoolSize() {
        return poolSize;
    }
    
    public int getQueueSize() {
        return taskQueue.size();
    }
    
    public boolean isShutdown() {
        return isShutdown;
    }
}
