package com.concurrency.problems.tier2;

/**
 * Classic Problem: Print Even-Odd with Two Threads
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ✅ INTERVIEW RELEVANCE: HIGH PRIORITY │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ Companies: Amazon, Microsoft, Goldman Sachs, Flipkart │
 * │ Frequency: VERY HIGH - Classic threading coordination question │
 * │ Time Target: Implement from scratch in < 15 minutes │
 * │ LeetCode: #1116 (Print Zero Even Odd - harder variant) │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 🎤 INTERVIEW FOLLOW-UP QUESTIONS (Be ready for these!) │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ │
 * │ Q1: "Why not just use a shared counter with volatile?" │
 * │ → volatile only ensures visibility, NOT atomicity of increment │
 * │ → Both threads might read same value, print same number! │
 * │ → INSIGHT: Need synchronization for read-modify-write operations │
 * │ │
 * │ Q2: "Can you solve this with Semaphores instead of wait/notify?" │
 * │ → Yes! oddSem starts with 1 permit, evenSem starts with 0 │
 * │ → Odd acquires oddSem, prints, releases evenSem │
 * │ → Even acquires evenSem, prints, releases oddSem │
 * │ → CLEANER: No explicit lock, no spurious wakeup handling │
 * │ │
 * │ Q3: "What if we need 3 threads: zero, even, odd (LeetCode 1116)?" │
 * │ → Same pattern but with 3 semaphores: zeroSem, oddSem, evenSem │
 * │ → Zero prints 0, then releases oddSem or evenSem based on next number │
 * │ → HARDER: Need to track which type of number comes next │
 * │ │
 * │ Q4: "How would you extend this to N threads printing in round-robin?" │
 * │ → Use array of N semaphores, each thread i releases semaphore (i+1)%N │
 * │ → Or use a shared turn variable with wait/notify │
 * │ → PATTERN: This generalizes to any thread coordination problem │
 * │ │
 * │ Q5: "What's wrong with busy-waiting here?" │
 * │ → while (turn != myTurn) { } // Burns CPU, wastes resources │
 * │ → SOLUTION: wait() releases CPU, gets woken when condition changes │
 * │ → INSIGHT: This is why wait/notify exists! │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * PROBLEM:
 * Print numbers 1 to N using two threads:
 * - Thread 1 prints only ODD numbers (1, 3, 5, ...)
 * - Thread 2 prints only EVEN numbers (2, 4, 6, ...)
 * - Numbers must be printed in order: 1, 2, 3, 4, 5, ...
 * 
 * TODO: Implement coordination between odd and even printer threads.
 * 
 * ⚠️ COMMON MISTAKES:
 * 1. Busy-waiting (while loop without wait()) - wastes CPU
 * 2. Using IF instead of WHILE for wait condition
 * 3. Forgetting to check bounds after waking up
 * 4. Using notify() instead of notifyAll()
 * 
 * 💡 THINK: Consider multiple approaches - wait/notify, Semaphores, or
 * Lock+Condition
 */
public class EvenOddPrinter {
    
    private final int max;
    private int current = 1;
    private boolean isOddTurn = true; // Odd numbers go first (1, 3, 5...)
    
    public EvenOddPrinter(int max) {
        this.max = max;
    }
    
    /**
     * TODO: Print odd numbers (1, 3, 5, ...) up to max.
     * 
     * 💡 THINK: How do you coordinate with the even thread?
     */
    public synchronized void printOdd() throws InterruptedException {
        // TODO: Implement odd number printing
        // Step 1: Loop while current <= max
        // Step 2: Wait while it's not odd's turn
        // Step 3: Print current (which is odd)
        // Step 4: Increment current
        // Step 5: Set isOddTurn = false
        // Step 6: notifyAll() to wake even thread
    }
    
    /**
     * TODO: Print even numbers (2, 4, 6, ...) up to max.
     * 
     * 📝 NOTE: Mirror of printOdd() but waits when isOddTurn is true.
     */
    public synchronized void printEven() throws InterruptedException {
        // TODO: Implement even number printing
        // Same pattern as printOdd() but:
        // - Wait while isOddTurn is TRUE
        // - After printing, set isOddTurn = true
    }
    
    /**
     * Demo: Run the even-odd printer.
     */
    public static void main(String[] args) throws InterruptedException {
        EvenOddPrinter printer = new EvenOddPrinter(10);
        
        Thread oddThread = new Thread(() -> {
            try {
                printer.printOdd();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "OddThread");
        
        Thread evenThread = new Thread(() -> {
            try {
                printer.printEven();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "EvenThread");
        
        oddThread.start();
        evenThread.start();
        
        oddThread.join();
        evenThread.join();
        
        System.out.println("Done! Expected output: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10");
    }
}
