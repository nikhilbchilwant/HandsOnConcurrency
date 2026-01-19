package com.concurrency.problems.tier2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Tests for EvenOddPrinter implementation.
 * 
 * Run with: mvn test -Dtest=EvenOddPrinterTest
 */
class EvenOddPrinterTest {

    @Test
    @Timeout(5)
    void testPrintUpTo10() throws InterruptedException {
        EvenOddPrinter printer = new EvenOddPrinter(10);
        CountDownLatch done = new CountDownLatch(2);
        
        Thread oddThread = new Thread(() -> {
            try {
                printer.printOdd();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();
            }
        }, "OddThread");
        
        Thread evenThread = new Thread(() -> {
            try {
                printer.printEven();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();
            }
        }, "EvenThread");
        
        oddThread.start();
        evenThread.start();
        
        assertTrue(done.await(3, TimeUnit.SECONDS), "Both threads should complete");
    }

    @Test
    @Timeout(5)
    void testPrintUpTo6() throws InterruptedException {
        EvenOddPrinter printer = new EvenOddPrinter(6);
        CountDownLatch done = new CountDownLatch(2);
        
        Thread oddThread = new Thread(() -> {
            try {
                printer.printOdd();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();
            }
        }, "OddThread");
        
        Thread evenThread = new Thread(() -> {
            try {
                printer.printEven();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();
            }
        }, "EvenThread");
        
        oddThread.start();
        evenThread.start();
        
        assertTrue(done.await(3, TimeUnit.SECONDS), "Both threads should complete for max=6");
    }

    @Test
    @Timeout(5)
    void testOddAndEvenThreadsBothComplete() throws InterruptedException {
        EvenOddPrinter printer = new EvenOddPrinter(4);
        CountDownLatch oddDone = new CountDownLatch(1);
        CountDownLatch evenDone = new CountDownLatch(1);
        
        Thread oddThread = new Thread(() -> {
            try {
                printer.printOdd();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                oddDone.countDown();
            }
        });
        
        Thread evenThread = new Thread(() -> {
            try {
                printer.printEven();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                evenDone.countDown();
            }
        });
        
        oddThread.start();
        evenThread.start();
        
        assertTrue(oddDone.await(2, TimeUnit.SECONDS), "Odd thread should complete");
        assertTrue(evenDone.await(2, TimeUnit.SECONDS), "Even thread should complete");
    }
}
