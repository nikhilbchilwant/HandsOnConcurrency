package com.concurrency.solutions.lab11;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * SOLUTION: Synchronizers
 * 
 * Demonstrates the use of high-level synchronizers from java.util.concurrent.
 */
public class SynchronizerSolution {

    public void demoCountDownLatch() throws InterruptedException {
        int workerCount = 3;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(workerCount);

        for (int i = 0; i < workerCount; ++i) {
            new Thread(new Worker(startSignal, doneSignal, i)).start();
        }

        System.out.println("Main: Preparation complete. Releasing workers...");
        startSignal.countDown();
        doneSignal.await();
        System.out.println("Main: All workers finished.");
    }

    public void demoSemaphore() {
        Semaphore semaphore = new Semaphore(2);

        IntConsumer task = (id) -> {
            try {
                if (semaphore.tryAcquire(1, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Thread-" + id + " acquired permit. Working...");
                        Thread.sleep(500);
                    } finally {
                        System.out.println("Thread-" + id + " releasing permit.");
                        semaphore.release();
                    }
                } else {
                    System.out.println("Thread-" + id + " timed out waiting for permit.");
                }
            } catch (InterruptedException e) {
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

    public void demoCyclicBarrier() {
        int partyCount = 3;
        CyclicBarrier barrier = new CyclicBarrier(partyCount, () -> {
            System.out.println("Barrier tripped! All parties arrived.");
        });

        IntConsumer task = (id) -> {
            try {
                System.out.println("Worker-" + id + " waiting at barrier.");
                barrier.await();
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
                startSignal.await();
                System.out.println("Worker " + id + " is performing its task.");
                Thread.sleep(100);
                doneSignal.countDown();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
