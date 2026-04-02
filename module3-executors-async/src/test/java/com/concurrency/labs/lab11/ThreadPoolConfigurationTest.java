package com.concurrency.labs.lab11;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolConfigurationTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> ThreadPoolConfiguration.main(new String[]{}));
    }
}
