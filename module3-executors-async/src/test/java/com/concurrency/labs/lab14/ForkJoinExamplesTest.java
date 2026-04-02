package com.concurrency.labs.lab14;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ForkJoinExamplesTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> ForkJoinExamples.main(new String[]{}));
    }
}
