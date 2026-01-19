package com.concurrency.solutions.tier3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * SOLUTION: Simple Thread Pool
 * 
 * 📚 REFERENCES:
 * - "Java Concurrency in Practice" by Goetz, Chapter 8 (Thread Pools)
 * - java.util.concurrent.ThreadPoolExecutor source code
 * 
 * KEY INSIGHT: Workers loop forever, taking tasks from a BlockingQueue.
 */
public class SimpleThreadPoolSolution {
    
    private final int poolSize;
    private final BlockingQueue<Runnable> taskQueue;
    private final List<Worker> workers;
    private volatile boolean isShutdown = false;
    
    public SimpleThreadPoolSolution(int poolSize) {
        this.poolSize = poolSize;
        this.taskQueue = new LinkedBlockingQueue<>();
        this.workers = new ArrayList<>(poolSize);
        
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker("Worker-" + i);
            workers.add(worker);
            worker.start();
        }
    }
    
    /**
     * Submit a task for execution.
     */
    public void submit(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool is shutdown");
        }
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while submitting task", e);
        }
    }
    
    /**
     * Graceful shutdown: stop accepting tasks, let current tasks complete.
     */
    public void shutdown() {
        isShutdown = true;
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
     * Worker thread implementation.
     * 
     * KEY POINTS:
     * 1. Loop while not shutdown OR has pending tasks
     * 2. Use BlockingQueue.take() to wait for tasks
     * 3. Handle exceptions to prevent worker death
     */
    private class Worker extends Thread {
        
        Worker(String name) {
            super(name);
        }
        
        @Override
        public void run() {
            while (!isShutdown || !taskQueue.isEmpty()) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    // Shutdown signal - check loop condition
                } catch (Exception e) {
                    // Don't let one bad task kill the worker!
                    System.err.println("Task failed: " + e.getMessage());
                }
            }
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
