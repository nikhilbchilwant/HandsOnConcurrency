/*
 * TOLock.java
 *
 * Created on January 21, 2006, 12:04 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package spin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Scott Time-out Lock
 *
 * @author Maurice Herlihy
 */
public class TOLock implements Lock {
    private static final Logger log = LogManager.getLogger(TOLock.class);
    static QNode AVAILABLE = new QNode();
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myNode;

    public TOLock() {
        tail = new AtomicReference<QNode>(null);
        // thread-local field
        myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long startTime = System.nanoTime();
        long patience = TimeUnit.NANOSECONDS.convert(time, unit);
        log.info("Trying to get the lock in {}", patience);
        QNode qnode = new QNode();
        myNode.set(qnode);    // remember for unlock
        qnode.pred = null;
        QNode pred = tail.getAndSet(qnode);
        if (pred == null || pred.pred == AVAILABLE) {
            log.info("Got the lock");
            return true;  // lock was free; just return
        }
        while (System.nanoTime() - startTime < patience) {
            //Go back until you go to the node holding the lock
            QNode predPred = pred.pred;
            if (predPred == AVAILABLE) {
                log.info("Got the lock during the spin");
                return true;
            } else if (predPred != null) {  // skip predecessors
                log.info("Skipping the predecessor, predPred={}", predPred);
                pred = predPred;
            }
            //If predPred is null, keep looping until it is AVAILABLE node until patience timeout
            //Question: why do we go back to the pred holding the lock? Won't all threads unlock if they all spin on the same node?
        }
        // timed out; reclaim or abandon own node
        if (!tail.compareAndSet(qnode, pred))
            qnode.pred = pred;
        log.info("Failed to get the lock");
        return false;
    }

    public void unlock() {
        QNode qnode = myNode.get();
        if (!tail.compareAndSet(qnode, null))
            qnode.pred = AVAILABLE;
    }

    // any class that implements lock must provide these methods
    public void lock() {
        try {
            tryLock(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public Condition newCondition() {
        throw new java.lang.UnsupportedOperationException();
    }

    public boolean tryLock() {
        try {
            return tryLock(0, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
            return false;
        }
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new java.lang.UnsupportedOperationException();
    }

    static class QNode {    // Queue node inner class
        //Instead of Flag, they are using available node
        public QNode pred = null;
    }
}


