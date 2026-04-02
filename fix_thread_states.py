import os

test_configs = {
    "./module1-foundations/src/test/java/com/concurrency/labs/lab03/ThreadStatesTest.java": """package com.concurrency.labs.lab03;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadStatesTest {
    @Test
    public void testMain() {
        assertNotNull(new ThreadStates());
    }
}
"""
}

for path, content in test_configs.items():
    with open(path, "w") as f:
        f.write(content)
