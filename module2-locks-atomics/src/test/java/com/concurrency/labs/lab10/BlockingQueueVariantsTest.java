package com.concurrency.labs.lab10;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockingQueueVariantsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> BlockingQueueVariants.main(new String[]{}));
    }
}
