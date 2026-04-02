package com.concurrency.labs.lab15;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeadlockPreventionTest {
    @Test
    public void testMain() {
        // main should not hang
        assertDoesNotThrow(() -> DeadlockPrevention.main(new String[]{}));
    }
}
