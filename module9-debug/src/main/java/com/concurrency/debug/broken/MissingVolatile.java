package com.concurrency.debug.broken;

/**
 * 🔴 BROKEN CODE - FIND THE BUG!
 * 
 * Expected bugs to find: 1
 * Time target: < 3 minutes
 */
public class MissingVolatile {
    
    // BUG: Missing volatile! Worker thread may never see running = false
    private boolean running = true;
    private int counter = 0;
    
    public void startWorker() {
        Thread worker = new Thread(() -> {
            while (running) {  // May be cached and never see false!
                counter++;
            }
        });
        worker.start();
    }
    
    public void stop() {
        running = false;  // Other thread might not see this!
    }
    
    public int getCounter() {
        return counter;
    }
}
