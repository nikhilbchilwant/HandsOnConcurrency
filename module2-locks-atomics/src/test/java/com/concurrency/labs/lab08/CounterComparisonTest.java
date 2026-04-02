package com.concurrency.labs.lab08;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CounterComparisonTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> CounterComparison.main(new String[]{}));
    }
}
