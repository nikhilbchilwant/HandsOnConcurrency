package com.concurrency.labs.lab02;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VolatileFixTest {
    @Test
    public void testVisibilityFixed() throws InterruptedException {
        VolatileFix fix = new VolatileFix();
        fix.startWorker();

        Thread.sleep(100);
        fix.stop();

        Thread.sleep(100);
        assertFalse(fix.isRunning());
    }
}
