package com.concurrency.labs.lab22;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadLocalDemoTest {
    @Test
    public void testThreadLocal() {
        assertDoesNotThrow(() -> ThreadLocalDemo.main(new String[]{}));
    }
}
