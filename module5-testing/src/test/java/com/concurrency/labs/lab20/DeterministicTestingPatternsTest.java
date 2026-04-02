package com.concurrency.labs.lab20;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeterministicTestingPatternsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> DeterministicTestingPatterns.main(new String[]{}));
    }
}
