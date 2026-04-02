package com.concurrency.labs.lab16;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GracefulShutdownPatternsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> GracefulShutdownPatterns.main(new String[]{}));
    }
}
