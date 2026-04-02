package com.concurrency.labs.lab21;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FanOutFanInTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> FanOutFanIn.main(new String[]{}));
    }
}
