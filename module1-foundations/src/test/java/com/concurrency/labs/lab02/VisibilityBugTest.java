package com.concurrency.labs.lab02;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisibilityBugTest {
    @Test
    public void testVisibility() throws InterruptedException {
        VisibilityBug demo = new VisibilityBug();
        demo.startWorker();

        Thread.sleep(100);
        demo.stop();

        Thread.sleep(100);
        assertFalse(demo.isRunning());
    }
}
