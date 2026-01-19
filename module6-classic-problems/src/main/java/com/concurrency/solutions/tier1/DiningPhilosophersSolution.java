package com.concurrency.solutions.tier1;

/**
 * SOLUTION: Dining Philosophers (Resource Hierarchy)
 * 
 * 📚 REFERENCES:
 * - "Operating System Concepts" by Silberschatz, Chapter 7 (Deadlocks)
 * - https://en.wikipedia.org/wiki/Dining_philosophers_problem
 * 
 * KEY INSIGHT: Always acquire the lower-numbered fork first to break circular wait.
 */
public class DiningPhilosophersSolution {
    
    private final int numPhilosophers;
    private final Object[] forks;
    private int[] eatCount;
    
    public DiningPhilosophersSolution(int numPhilosophers) {
        this.numPhilosophers = numPhilosophers;
        this.forks = new Object[numPhilosophers];
        this.eatCount = new int[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new Object();
            eatCount[i] = 0;
        }
    }
    
    /**
     * Philosopher eats using resource hierarchy to prevent deadlock.
     * 
     * KEY POINTS:
     * 1. Calculate which fork is "lower" numbered
     * 2. Always acquire lower fork first
     * 3. This breaks circular wait - no deadlock possible!
     */
    public void eat(int philosopherId) {
        int leftFork = philosopherId;
        int rightFork = (philosopherId + 1) % numPhilosophers;
        
        // Resource hierarchy: always acquire lower-numbered fork first
        int firstFork = Math.min(leftFork, rightFork);
        int secondFork = Math.max(leftFork, rightFork);
        
        synchronized (forks[firstFork]) {
            synchronized (forks[secondFork]) {
                doEat(philosopherId);
            }
        }
    }
    
    private void doEat(int philosopherId) {
        eatCount[philosopherId]++;
        try {
            Thread.sleep(10); // Simulate eating
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public int getEatCount(int philosopherId) {
        return eatCount[philosopherId];
    }
    
    public int getNumPhilosophers() {
        return numPhilosophers;
    }
}
