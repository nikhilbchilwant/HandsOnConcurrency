package com.concurrency.problems.tier1;

/**
 * Classic Problem #1: Bounded Blocking Queue (with wait/notify)
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ✅ INTERVIEW RELEVANCE: HIGH PRIORITY │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ Companies: Rubrik, Dropbox, Amazon, Uber, Google │
 * │ Frequency: VERY HIGH - Asked in 70%+ of concurrency interviews │
 * │ Time Target: Implement from scratch in < 20 minutes │
 * │ │
 * │ WHY THIS IS CRITICAL: │
 * │ - Tests fundamental understanding of wait/notify │
 * │ - Exposes common mistakes (if vs while, notify vs notifyAll) │
 * │ - Foundation for Producer-Consumer pattern │
 * │ - Rubrik asks this exact problem in system coding rounds │
 * │ │
 * │ INTERVIEW TIP: Implement this version first (wait/notify), then │
 * │ mention "I could also implement this with ReentrantLock + Condition │
 * │ for better flexibility" as a follow-up. │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 🎤 INTERVIEW FOLLOW-UP QUESTIONS (Be ready for these!) │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ │
 * │ Q1: "Why use notifyAll() instead of notify()?" │
 * │ → notify() wakes ONE thread - might wake wrong type (producer vs │
 * │ consumer). notifyAll() is safer but less efficient. │
 * │ → CLEVER FOLLOW-UP: "When IS notify() sufficient?" │
 * │ Answer: When all waiters are equivalent (same condition). │
 * │ │
 * │ Q2: "What if I need put() to timeout after 5 seconds?" │
 * │ → Use wait(timeoutMs) and track remaining time in a loop. │
 * │ → TRAP: Don't forget to recalculate remaining time after each wake! │
 * │ long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5); │
 * │ while (full && System.nanoTime() < deadline) { │
 * │ wait(remaining); │
 * │ } │
 * │ │
 * │ Q3: "How would you make this fair (FIFO ordering of waiters)?" │
 * │ → wait/notify has NO fairness guarantees! │
 * │ → Use ReentrantLock(true) with Condition for fair locking. │
 * │ → TRADE-OFF: Fairness reduces throughput due to thread handoff. │
 * │ │
 * │ Q4: "Your queue has 1000 producers and 1 consumer. Problem?" │
 * │ → notifyAll() wakes ALL 1000 producers when one slot opens! │
 * │ → SOLUTION: Use separate Conditions (notFull, notEmpty) with │
 * │ ReentrantLock so we signal only relevant waiters. │
 * │ │
 * │ Q5: "What happens if a thread is interrupted while waiting?" │
 * │ → InterruptedException is thrown, must handle or propagate. │
 * │ → TRAP: If you catch and ignore, the interrupt flag is cleared! │
 * │ Always either propagate or call Thread.currentThread().interrupt() │
 * │ │
 * │ Q6: "Can this implementation handle null elements?" │
 * │ → Yes in this impl, but BAD PRACTICE. Use Optional or sentinel if │
 * │ needed. null often signals "empty" or "poison pill". │
 * │ │
 * │ Q7: "How does ArrayBlockingQueue differ from your implementation?" │
 * │ → Uses ReentrantLock + 2 Conditions (notEmpty, notFull) │
 * │ → Supports fair mode │
 * │ → More efficient signaling (only signal relevant waiters) │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * This is THE most important concurrency problem for interviews!
 * 
 * TODO: Implement a thread-safe bounded queue that:
 * - Blocks on put() when full
 * - Blocks on take() when empty
 * - Supports multiple producers and consumers
 * 
 * ⚠️ CRITICAL: Common mistakes to avoid:
 * 1. Using IF instead of WHILE for wait conditions (spurious wakeups!)
 * 2. Using notify() instead of notifyAll() (wrong thread might wake up!)
 * 3. Forgetting to handle InterruptedException properly
 * 
 * 💡 THINK: After implementing this version, implement another using
 * ReentrantLock + Condition for comparison. Which is cleaner?
 * 
 * 📝 NOTE: In production, use java.util.concurrent.ArrayBlockingQueue!
 * This exercise is for learning the fundamentals.
 * 
 * @param <E> element type
 */
public class BoundedBlockingQueue<E> {
    
    private final Object[] items;
    private int head;      // Index of next element to remove
    private int tail;      // Index of next slot to fill
    private int count;     // Number of elements in queue
    
    public BoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.items = new Object[capacity];
    }
    
    /**
     * TODO: Add an element, blocking if the queue is full.
     * 
     * 🔑 HINT - The correct pattern:
     * 
     *   synchronized (this) {
     *       while (count == items.length) {  // WHILE, not IF!
     *           wait();  // Release lock and wait
     *       }
     *       items[tail] = item;
     *       tail = (tail + 1) % items.length;  // Circular increment
     *       count++;
     *       notifyAll();  // Wake up waiting consumers
     *   }
     * 
     * 💡 THINK: Why must we use WHILE instead of IF?
     *   1. Spurious wakeups: Thread can wake without notify
     *   2. Multiple consumers: Another consumer might take the item first
     * 
     * @param item the element to add
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void put(E item) throws InterruptedException {
        // TODO: Implement blocking put
        // Step 1: Wait while full (use WHILE loop!)
        // Step 2: Add item at tail
        // Step 3: Update tail (circular)
        // Step 4: Increment count
        // Step 5: notifyAll()
    }
    
    /**
     * TODO: Remove and return an element, blocking if empty.
     * 
     * 📝 NOTE: The implementation mirrors put() but checks for empty
     * instead of full, and notifies producers instead of consumers.
     * 
     * ⚠️ AVOID: Returning null to indicate empty queue!
     *   Blocking queues should block, not return null.
     *   (Unless you implement a separate poll() with timeout)
     * 
     * @return the removed element
     * @throws InterruptedException if interrupted while waiting
     */
    @SuppressWarnings("unchecked")
    public synchronized E take() throws InterruptedException {
        // TODO: Implement blocking take
        // Step 1: Wait while empty (use WHILE loop!)
        // Step 2: Get item at head
        // Step 3: Clear slot (help GC)
        // Step 4: Update head (circular)
        // Step 5: Decrement count
        // Step 6: notifyAll()
        // Step 7: Return item
        return null;
    }
    
    /**
     * Returns the number of elements in the queue.
     */
    public synchronized int size() {
        return count;
    }
    
    /**
     * Returns true if the queue is empty.
     */
    public synchronized boolean isEmpty() {
        return count == 0;
    }
    
    /**
     * Returns true if the queue is full.
     */
    public synchronized boolean isFull() {
        return count == items.length;
    }
    
    /**
     * Returns the capacity of the queue.
     */
    public int capacity() {
        return items.length;
    }
}
