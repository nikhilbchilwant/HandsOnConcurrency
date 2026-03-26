package com.concurrency.labs.lab11;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SynchronizerLabTest {

    @Test
    public void testCountDownLatch() throws InterruptedException {
        SynchronizerLab lab = new SynchronizerLab();
        // Just verify it runs without exception as it relies on stdout
        lab.demoCountDownLatch();
    }

    @Test
    public void testSemaphore() throws InterruptedException {
        SynchronizerLab lab = new SynchronizerLab();
        lab.demoSemaphore();
        // Give it some time to run
        Thread.sleep(1500);
    }

    @Test
    public void testCyclicBarrier() throws InterruptedException {
        SynchronizerLab lab = new SynchronizerLab();
        lab.demoCyclicBarrier();
        Thread.sleep(1000);
    }
}
