package com.concurrency.labs.lab13;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CompletableFuturePipelineTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> CompletableFuturePipeline.main(new String[]{}));
    }
}
