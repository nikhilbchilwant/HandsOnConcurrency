package com.concurrency.debug.broken;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 🔴 BROKEN CODE - FIND THE BUGS!
 * 
 * This is a blocking queue implementation with INTENTIONAL BUGS.
 * Your task: Find all the concurrency bugs WITHOUT running the code.
 * 
 * Expected bugs to find: 3
 * Time target: < 5 minutes
 * 
 * Companies that ask debugging questions: Rubrik, Dropbox
 */
public class BrokenBlockingQueue<E> {
    
    private final Queue<E> queue = new LinkedList<>();
    private final int capacity;
    
    public BrokenBlockingQueue(int capacity) {
        this.capacity = capacity;
    }
    
    /**
     * Add an element, blocking if full.
     * 
     * 🔴 BUG(S) IN THIS METHOD - CAN YOU FIND THEM?
     */
    public synchronized void put(E element) throws InterruptedException {
        // BUG #1: Using 'if' instead of 'while' - spurious wakeup!
        if (queue.size() == capacity) {
            wait();
        }
        
        queue.add(element);
        
        // BUG #2: Using notify() instead of notifyAll()
        notify();
    }
    
    /**
     * Remove and return an element, blocking if empty.
     */
    public synchronized E take() throws InterruptedException {
        // Same bug as put()
        if (queue.isEmpty()) {
            wait();
        }
        
        E element = queue.poll();
        notify();
        
        // BUG #3: Not checking if poll() returned null
        return element;
    }
    
    public synchronized int size() {
        return queue.size();
    }
}
