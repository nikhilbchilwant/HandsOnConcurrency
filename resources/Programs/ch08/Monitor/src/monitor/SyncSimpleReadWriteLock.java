/*
 * SimpleReadWriteLock.java
 *
 * Created on January 9, 2006, 7:11 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package monitor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author Maurice Herlihy
 */
public class SyncSimpleReadWriteLock implements ReadWriteLock {

    int readers;
    boolean writer;
    Lock readLock;
    Lock writeLock;

    public SyncSimpleReadWriteLock() {
        writer = false;
        readers = 0;
        readLock = new ReadLock();
        writeLock = new WriteLock();
    }

    public Lock readLock() {
        return readLock;
    }

    public Lock writeLock() {
        return writeLock;
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    class ReadLock implements Lock {
        public void lock() {
            synchronized (this) {
                while (writer) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
                readers++;
            }
        }

        public void unlock() {
            synchronized (this) {
                readers--;
                if (readers == 0)
                    notifyAll();
            }
        }

        public void lockInterruptibly() throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        public boolean tryLock() {
            throw new UnsupportedOperationException();
        }

        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }

    protected class WriteLock implements Lock {
        public void lock() {
            synchronized (this) {
                while (readers > 0) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
                writer = true;
            }
        }

        public void unlock() {
            writer = false;
            notifyAll();
        }

        public void lockInterruptibly() throws InterruptedException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean tryLock() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Condition newCondition() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
