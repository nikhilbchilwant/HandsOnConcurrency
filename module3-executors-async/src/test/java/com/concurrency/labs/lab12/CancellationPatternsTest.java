package com.concurrency.labs.lab12;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CancellationPatternsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> CancellationPatterns.main(new String[]{}));
    }
}
