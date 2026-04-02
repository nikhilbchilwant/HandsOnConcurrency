package com.concurrency.labs.lab09;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentHashMapUsageTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> ConcurrentHashMapUsage.main(new String[]{}));
    }
}
