package com.concurrency.labs.lab06;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReadWriteCacheTest {
    @Test
    public void testCache() {
        ReadWriteCache<String, Integer> cache = new ReadWriteCache<>();
        cache.put("A", 1);
        assertEquals(1, cache.get("A"));
    }
}
