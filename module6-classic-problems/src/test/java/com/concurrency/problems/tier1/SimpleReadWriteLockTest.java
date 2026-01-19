package com.concurrency.problems.tier1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Tests for SimpleReadWriteLock implementation.
 * 
 * Run with: mvn test -Dtest=SimpleReadWriteLockTest
 */
class SimpleReadWriteLockTest {

    @Test
    void testSingleReader() throws InterruptedException {
        SimpleReadWriteLock lock = new SimpleReadWriteLock();
        
        lock.lockRead();
        assertEquals(1, lock.getReaderCount());
        assertEquals(0, lock.getWriterCount());
        
        lock.unlockRead();
        assertEquals(0, lock.getReaderCount());
    }

    @Test
    void testSingleWriter() throws InterruptedException {
        SimpleReadWriteLock lock = new SimpleReadWriteLock();
        
        lock.lockWrite();
        assertEquals(0, lock.getReaderCount());
        assertEquals(1, lock.getWriterCount());
        
        lock.unlockWrite();
        assertEquals(0, lock.getWriterCount());
    }

    @Test
    @Timeout(5)
    void testMultipleReadersAllowed() throws InterruptedException {
        SimpleReadWriteLock lock = new SimpleReadWriteLock();
        int numReaders = 5;
        CountDownLatch allAcquired = new CountDownLatch(numReaders);
        CountDownLatch release = new CountDownLatch(1);
        
        for (int i = 0; i < numReaders; i++) {
            new Thread(() -> {
                try {
                    lock.lockRead();
                    allAcquired.countDown();
                    release.await();
                    lock.unlockRead();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
        
        // All readers should acquire the lock
        assertTrue(allAcquired.await(2, TimeUnit.SECONDS), 
            "All readers should be able to acquire read lock simultaneously");
        assertEquals(numReaders, lock.getReaderCount());
        
        release.countDown();
    }

    @Test
    @Timeout(5)
    void testWriterExcludesReaders() throws InterruptedException {
        SimpleReadWriteLock lock = new SimpleReadWriteLock();
        AtomicInteger readerAcquired = new AtomicInteger(0);
        
        // Writer acquires lock first
        lock.lockWrite();
        
        // Reader tries to acquire
        Thread reader = new Thread(() -> {
            try {
                lock.lockRead();
                readerAcquired.incrementAndGet();
                lock.unlockRead();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        reader.start();
        
        Thread.sleep(200);
        
        // Reader should be blocked
        assertEquals(0, readerAcquired.get(), "Reader should be blocked while writer holds lock");
        assertEquals(Thread.State.WAITING, reader.getState());
        
        // Release writer
        lock.unlockWrite();
        reader.join(1000);
        
        assertEquals(1, readerAcquired.get(), "Reader should acquire after writer releases");
    }

    @Test
    @Timeout(5)
    void testWriterExcludesWriters() throws InterruptedException {
        SimpleReadWriteLock lock = new SimpleReadWriteLock();
        AtomicInteger writer2Acquired = new AtomicInteger(0);
        
        // Writer 1 acquires lock
        lock.lockWrite();
        
        // Writer 2 tries to acquire
        Thread writer2 = new Thread(() -> {
            try {
                lock.lockWrite();
                writer2Acquired.incrementAndGet();
                lock.unlockWrite();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        writer2.start();
        
        Thread.sleep(200);
        
        // Writer 2 should be blocked
        assertEquals(0, writer2Acquired.get(), "Second writer should be blocked");
        
        // Release writer 1
        lock.unlockWrite();
        writer2.join(1000);
        
        assertEquals(1, writer2Acquired.get(), "Second writer should acquire after first releases");
    }

    @Test
    @Timeout(5)
    void testReaderBlockedWhenWriterWaiting() throws InterruptedException {
        SimpleReadWriteLock lock = new SimpleReadWriteLock();
        CountDownLatch reader1Started = new CountDownLatch(1);
        CountDownLatch reader1Done = new CountDownLatch(1);
        AtomicInteger newReaderAcquired = new AtomicInteger(0);
        
        // Reader 1 holds the lock
        Thread reader1 = new Thread(() -> {
            try {
                lock.lockRead();
                reader1Started.countDown();
                reader1Done.await();
                lock.unlockRead();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        reader1.start();
        reader1Started.await();
        
        // Writer starts waiting
        Thread writer = new Thread(() -> {
            try {
                lock.lockWrite();
                lock.unlockWrite();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        writer.start();
        Thread.sleep(100);
        
        // New reader tries to acquire - should block due to writer waiting
        Thread newReader = new Thread(() -> {
            try {
                lock.lockRead();
                newReaderAcquired.incrementAndGet();
                lock.unlockRead();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        newReader.start();
        Thread.sleep(200);
        
        // New reader should be blocked (writer preference)
        assertEquals(0, newReaderAcquired.get(), 
            "New reader should be blocked when writer is waiting (writer preference)");
        
        // Cleanup
        reader1Done.countDown();
        reader1.join(1000);
        writer.join(1000);
        newReader.join(1000);
    }
}
