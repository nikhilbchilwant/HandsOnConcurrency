package com.concurrency.labs.lab18;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StressTestHarnessTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> StressTestHarness.main(new String[]{}));
    }
}
