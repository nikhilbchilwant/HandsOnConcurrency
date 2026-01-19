package com.concurrency.solutions.tier2;

/**
 * SOLUTION: Even-Odd Printer
 * 
 * 📚 REFERENCES:
 * - "Java Concurrency in Practice" by Goetz, Chapter 14 (Condition Queues)
 * 
 * KEY INSIGHT: Use a turn flag to coordinate between two threads.
 */
public class EvenOddPrinterSolution {
    
    private final int max;
    private int current = 1;
    private boolean isOddTurn = true;
    
    public EvenOddPrinterSolution(int max) {
        this.max = max;
    }
    
    /**
     * Print odd numbers (1, 3, 5, ...).
     * 
     * KEY POINTS:
     * 1. Wait while it's not odd's turn
     * 2. Print, increment, flip turn
     * 3. notifyAll to wake even thread
     */
    public synchronized void printOdd() throws InterruptedException {
        while (current <= max) {
            while (!isOddTurn && current <= max) {
                wait();
            }
            if (current <= max) {
                System.out.println(Thread.currentThread().getName() + ": " + current);
                current++;
                isOddTurn = false;
                notifyAll();
            }
        }
    }
    
    /**
     * Print even numbers (2, 4, 6, ...).
     */
    public synchronized void printEven() throws InterruptedException {
        while (current <= max) {
            while (isOddTurn && current <= max) {
                wait();
            }
            if (current <= max) {
                System.out.println(Thread.currentThread().getName() + ": " + current);
                current++;
                isOddTurn = true;
                notifyAll();
            }
        }
    }
    
    public int getCurrent() {
        return current;
    }
}
