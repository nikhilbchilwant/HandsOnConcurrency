package com.concurrency.debug.broken;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 🔴 BROKEN CODE - FIND THE BUG!
 * 
 * Expected bugs to find: 1
 * Time target: < 3 minutes
 */
public class NotifyVsNotifyAll {
    
    private final Queue<String> queue = new LinkedList<>();
    private final int capacity = 5;
    
    /**
     * 🔴 THE BUG: Using notify() instead of notifyAll()
     * 
     * With multiple producers AND consumers, notify() may wake
     * the wrong type of thread!
     */
    public synchronized void produce(String item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(item);
        notify();  // BUG: Should be notifyAll()
    }
    
    public synchronized String consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        String item = queue.poll();
        notify();  // BUG: Should be notifyAll()
        return item;
    }
}
