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
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 🎤 INTERVIEW FOLLOW-UP QUESTIONS (Be ready for these!) │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ │
 * │ Q1: "What happens when the task queue is full?" │
 * │ → Current impl uses LinkedBlockingQueue (unbounded) - never full! │
 * │ → For bounded queue, need a REJECTION POLICY: │
 * │ - AbortPolicy: throw RejectedExecutionException │
 * │ - CallerRunsPolicy: caller thread runs the task (backpressure!) │
 * │ - DiscardPolicy: silently drop the task │
 * │ - DiscardOldestPolicy: drop oldest queued task │
 * │ → INSIGHT: CallerRunsPolicy slows down producer = natural backpressure│
 * │ │
 * │ Q2: "shutdown() vs shutdownNow() - what's the difference?" │
 * │ → shutdown(): Stop accepting new tasks, let queued tasks complete │
 * │ → shutdownNow(): Interrupt workers, return unexecuted tasks │
 * │ → TRAP: shutdown() doesn't interrupt workers - they finish current! │
 * │ │
 * │ Q3: "A task throws an exception. What happens to the worker?" │
 * │ → If uncaught, worker thread DIES - pool shrinks by one! │
 * │ → SOLUTION: Wrap task.run() in try-catch, log but continue │
 * │ → PRODUCTION: Use Thread.setUncaughtExceptionHandler() for cleanup │
 * │ │
 * │ Q4: "How would you implement a cached thread pool (grow/shrink)?" │
 * │ → Core threads + extra threads that die after idle timeout │
 * │ → poll(keepAlive, TimeUnit) instead of take() for non-core threads │
 * │ → INSIGHT: This is how Executors.newCachedThreadPool() works │
 * │ │
 * │ Q5: "Why use volatile for isShutdown but not for taskQueue?" │
 * │ → taskQueue is a BlockingQueue - already thread-safe internally │
 * │ → isShutdown is a simple boolean read/written from multiple threads │
 * │ → TRAP: volatile ensures visibility, NOT atomicity of check-then-act │
 * │ │
 * │ Q6: "How many threads should a pool have?" │
 * │ → CPU-bound: ~number of cores (Runtime.availableProcessors()) │
 * │ → IO-bound: cores * (1 + wait_time/compute_time), often 10x cores │
 * │ → INSIGHT: Little's Law - threads = throughput * latency │
 * │ │
 * │ Q7: "How would you add support for Callable<T> and Future<T>?" │
 * │ → Wrap Callable in FutureTask (implements Runnable & Future) │
 * │ → Return the FutureTask to caller for result/cancellation │
 * │ → TRAP: Calling get() before completion blocks the caller! │
 * │ │
 * │ Q8: "What's the danger of unbounded queues?" │
 * │ → OOM if tasks arrive faster than processed (memory keeps growing) │
 * │ → SOLUTION: Use bounded queue + rejection policy for backpressure │
 * │ → PRODUCTION: Monitor queue size and alert before OOM │
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
        throw new UnsupportedOperationException("TODO: Implement this method");
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
        throw new UnsupportedOperationException("TODO: Implement this method");
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
     * ⚠️ COMMON MISTAKES:
     * 1. Letting exceptions kill the worker thread
     * 2. Not checking shutdown flag after interrupt
     * 3. Busy-waiting instead of using blocking take()
     */
    private class Worker extends Thread {
        
        Worker(String name) {
            super(name);
        }
        
        @Override
        public void run() {
            // TODO: Implement the worker loop
            throw new UnsupportedOperationException("TODO: Implement this method");
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
