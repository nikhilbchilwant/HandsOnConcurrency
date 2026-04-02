package com.concurrency.labs.lab19;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RaceDetectionConceptsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> RaceDetectionConcepts.main(new String[]{}));
    }
}
