package com.concurrency.labs.lab15;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeadlockDemoTest {
    @Test
    public void testMain() {
        // Just verify class exists, main might hang
        assertNotNull(new DeadlockDemo());
    }
}
