package com.concurrency.solutions.tier1;

/**
 * SOLUTION: Bounded Blocking Queue (with wait/notify)
 * 
 * 📚 REFERENCES:
 * - "Java Concurrency in Practice" by Goetz, Chapter 14 (Building Custom Synchronizers)
 * - https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html
 * 
 * This is the reference implementation. Compare your solution with this after
 * you've attempted the problem in the `problems` package.
 */
public class BoundedBlockingQueueSolution<E> {
    
    private final Object[] items;
    private int head;      // Index of next element to remove
    private int tail;      // Index of next slot to fill
    private int count;     // Number of elements in queue
    
    public BoundedBlockingQueueSolution(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.items = new Object[capacity];
    }
    
    /**
     * Add an element, blocking if the queue is full.
     * 
     * KEY POINTS:
     * 1. Use WHILE (not IF) for the wait condition - handles spurious wakeups
     * 2. Use notifyAll() to wake all waiters - safe for multiple producers/consumers
     * 3. Circular buffer using modulo arithmetic
     */
    public synchronized void put(E item) throws InterruptedException {
        // Wait while full (use WHILE loop for spurious wakeups!)
        while (count == items.length) {
            wait();
        }
        // Add item at tail
        items[tail] = item;
        // Update tail (circular)
        tail = (tail + 1) % items.length;
        // Increment count
        count++;
        // Wake up waiting consumers
        notifyAll();
    }
    
    /**
     * Remove and return an element, blocking if empty.
     * 
     * KEY POINTS:
     * 1. Mirror of put() but checks for empty instead of full
     * 2. Clear the slot after taking (help GC)
     * 3. Notify producers that a slot is available
     */
    @SuppressWarnings("unchecked")
    public synchronized E take() throws InterruptedException {
        // Wait while empty (use WHILE loop for spurious wakeups!)
        while (count == 0) {
            wait();
        }
        // Get item at head
        E item = (E) items[head];
        // Clear slot (help GC)
        items[head] = null;
        // Update head (circular)
        head = (head + 1) % items.length;
        // Decrement count
        count--;
        // Wake up waiting producers
        notifyAll();
        // Return item
        return item;
    }
    
    public synchronized int size() {
        return count;
    }
    
    public synchronized boolean isEmpty() {
        return count == 0;
    }
    
    public synchronized boolean isFull() {
        return count == items.length;
    }
    
    public int capacity() {
        return items.length;
    }
}
