package com.concurrency.labs.lab11;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Lab 11: Synchronizers
 * 
 * Demonstrates the use of high-level synchronizers from java.util.concurrent.
 * These tools provide more robust coordination patterns than wait/notify.
 * 
 * JCiP REFERENCE:
 * - Ch 5.5: Synchronizers
 */
public class SynchronizerLab {

    /**
     * Demonstrates a CountDownLatch.
     * 💡 THINK: Can a CountDownLatch be reset once it reaches zero?
     */
    public void demoCountDownLatch() throws InterruptedException {
        int workerCount = 3;
        // TODO: Initialize a latch that waits for one "start" signal
        CountDownLatch startSignal = null;
        // TODO: Initialize a latch that waits for all workers to finish
        CountDownLatch doneSignal = null;

        for (int i = 0; i < workerCount; ++i) {
            new Thread(new Worker(startSignal, doneSignal, i)).start();
        }

        System.out.println("Main: Preparation complete. Releasing workers...");
        // TODO: Release the workers
        
        // TODO: Wait for all workers to signal completion
        
        System.out.println("Main: All workers finished.");
    }

    /**
     * Demonstrates a Semaphore.
     * 💡 THINK: How is a Semaphore different from a simple Lock?
     */
    public void demoSemaphore() {
        // TODO: Initialize a semaphore with 2 permits
        Semaphore semaphore = null;

        IntConsumer task = (id) -> {
            try {
                // TODO: Acquire a permit (with a timeout)
                // If acquired:
                //   try { print "Working"; sleep; } finally { release; }
                // else:
                //   print "Timed out"
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        };

        for (int i = 0; i < 4; i++) {
            final int id = i;
            new Thread(() -> task.accept(id)).start();
        }
    }
    
    @FunctionalInterface
    interface IntConsumer {
        void accept(int i);
    }

    /**
     * Demonstrates a CyclicBarrier.
     * 💡 THINK: What happens to a CyclicBarrier after the barrier is tripped?
     */
    public void demoCyclicBarrier() {
        int partyCount = 3;
        // TODO: Initialize a barrier that runs a specific message when tripped
        CyclicBarrier barrier = null;

        IntConsumer task = (id) -> {
            try {
                System.out.println("Worker-" + id + " waiting at barrier.");
                // TODO: Wait for others at the barrier
                System.out.println("Worker-" + id + " proceeding to next phase.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < partyCount; i++) {
            final int id = i;
            new Thread(() -> task.accept(id)).start();
        }
    }

    static class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;
        private final int id;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal, int id) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
            this.id = id;
        }

        public void run() {
            try {
                // TODO: Wait for start signal
                System.out.println("Worker " + id + " is performing its task.");
                Thread.sleep(100);
                // TODO: Signal completion
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizerLab lab = new SynchronizerLab();
        lab.demoCountDownLatch();
        Thread.sleep(1000);
        lab.demoSemaphore();
        Thread.sleep(2000);
        lab.demoCyclicBarrier();
    }
}
