package com.concurrency.problems.tier1;

/**
 * Classic Problem #3: Dining Philosophers
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ⚠️ INTERVIEW RELEVANCE: MEDIUM PRIORITY │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ Companies: Academic favorite, occasionally Amazon, Google │
 * │ Frequency: MEDIUM - Tests deadlock reasoning, not often coded │
 * │ Time Target: Explain solutions in < 10 minutes │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 🎤 INTERVIEW FOLLOW-UP QUESTIONS (Be ready for these!) │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ │
 * │ Q1: "What are the 4 conditions for deadlock?" │
 * │ → 1. MUTUAL EXCLUSION: Resource can't be shared │
 * │ → 2. HOLD AND WAIT: Holding one, waiting for another │
 * │ → 3. NO PREEMPTION: Can't forcibly take a resource │
 * │ → 4. CIRCULAR WAIT: A→B→C→...→A │
 * │ → INSIGHT: Break ANY ONE condition to prevent deadlock │
 * │ │
 * │ Q2: "Which condition does resource hierarchy break?" │
 * │ → CIRCULAR WAIT - everyone acquires in same order, no cycle │
 * │ → INSIGHT: Philosopher N breaks the cycle by grabbing fork 0 first │
 * │ │
 * │ Q3: "What's the Waiter solution and which condition does it break?" │
 * │ → Semaphore with N-1 permits (only N-1 philosophers try at once) │
 * │ → Breaks HOLD AND WAIT - if can't get permit, don't hold any fork │
 * │ → TRADE-OFF: Limits parallelism (bottleneck at semaphore) │
 * │ │
 * │ Q4: "How would you detect deadlock in a running system?" │
 * │ → Build wait-for graph, check for cycles │
 * │ → JVM: jstack shows thread states and lock owners │
 * │ → PRODUCTION: Use ThreadMXBean.findDeadlockedThreads() │
 * │ │
 * │ Q5: "What's livelock? Give an example." │
 * │ → Threads actively changing state but making no progress │
 * │ → Example: Both philosophers pick up fork, see other fork taken, │
 * │ put down fork, retry... forever in sync! │
 * │ → SOLUTION: Add random delay before retry (backoff) │
 * │ │
 * │ Q6: "How does tryLock with timeout help prevent deadlock?" │
 * │ → tryLock(timeout): If can't get lock in time, release held locks │
 * │ → Breaks HOLD AND WAIT - if partial acquire fails, release all │
 * │ → CAVEAT: Can still livelock if all timeout at same rate │
 * │ │
 * │ Q7: "What's a real-world system where this pattern applies?" │
 * │ → Database transactions: lock rows in consistent order │
 * │ → Bank transfers: lock accounts by account number │
 * │ → INSIGHT: The pattern isn't about eating - it's about lock ordering!│
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * Five philosophers sit at a round table. Each needs TWO forks to eat.
 * Forks are placed between each pair of philosophers.
 * 
 * ⚠️ PROBLEM: If each philosopher picks up their left fork first,
 * they all wait for the right fork → DEADLOCK!
 * 
 * TODO: Implement a solution that prevents deadlock.
 * 
 * 💡 SOLUTIONS (implement one or more):
 * 
 * 1. RESOURCE HIERARCHY (implemented here):
 * Always pick up the lower-numbered fork first.
 * Philosopher 4 picks up fork 0 before fork 4 (breaks the cycle!)
 * 
 * 2. WAITER (Semaphore):
 * A "waiter" permits only 4 philosophers to try eating at once.
 * (See DiningPhilosophersWaiter.java)
 * 
 * 3. ASYMMETRIC:
 * Odd philosophers pick left first, even pick right first.
 * 
 * 📝 NOTE: This problem demonstrates the FOUR conditions for deadlock:
 * 1. Mutual Exclusion: Forks can't be shared
 * 2. Hold and Wait: Holding one fork while waiting for another
 * 3. No Preemption: Can't take a fork from another philosopher
 * 4. Circular Wait: A→B→C→D→E→A (broken by resource hierarchy!)
 */
public class DiningPhilosophers {
    
    private final int numPhilosophers;
    private final Object[] forks;
    
    public DiningPhilosophers(int numPhilosophers) {
        this.numPhilosophers = numPhilosophers;
        this.forks = new Object[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new Object();
        }
    }
    
    /**
     * TODO: Implement eating with deadlock prevention.
     * 
     * 🔑 HINT - Resource Hierarchy Solution:
     *   - Always acquire the lower-numbered fork first
     *   - This breaks the circular wait condition!
     * 
     * Example for Philosopher 2 (between forks 2 and 3):
     *   - First lock: fork[2] (lower number)
     *   - Second lock: fork[3] (higher number)
     * 
     * Example for Philosopher 4 (between forks 4 and 0):
     *   - First lock: fork[0] (lower number!)
     *   - Second lock: fork[4] (higher number)
     * 
     * 💡 THINK: Why does this prevent deadlock?
     *   All philosophers try to acquire a "lower" fork first.
     *   Someone will always succeed → No circular wait!
     */
    public void eat(int philosopherId) {
        int leftFork = philosopherId;
        int rightFork = (philosopherId + 1) % numPhilosophers;
        
        // TODO: Determine which fork to pick up first (lower numbered)
        // 💡 HINT: Use Math.min() and Math.max() for resource hierarchy
        // int firstFork = Math.min(leftFork, rightFork);
        // int secondFork = Math.max(leftFork, rightFork);

        // TODO: Acquire forks in order using nested synchronized blocks
        // synchronized (forks[firstFork]) {
        // synchronized (forks[secondFork]) {
        // doEat(philosopherId);
        // }
        // }
        
        throw new UnsupportedOperationException("TODO: Implement this method");
    }
    
    private void doEat(int philosopherId) {
        // Simulate eating
        System.out.println("Philosopher " + philosopherId + " is eating");
        try {
            Thread.sleep(10); // Simulate eating time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Creates and starts philosopher threads.
     * Each philosopher thinks, then eats, in a loop.
     */
    public void startDining(int rounds) {
        Thread[] philosophers = new Thread[numPhilosophers];
        
        for (int i = 0; i < numPhilosophers; i++) {
            final int id = i;
            philosophers[i] = new Thread(() -> {
                for (int r = 0; r < rounds; r++) {
                    think(id);
                    eat(id);
                }
            }, "Philosopher-" + i);
        }
        
        // Start all philosophers
        for (Thread t : philosophers) {
            t.start();
        }
        
        // Wait for all to complete
        for (Thread t : philosophers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void think(int philosopherId) {
        // Simulate thinking
        try {
            Thread.sleep((long) (Math.random() * 10));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
