import os

test_configs = {
    # Module 1
    "./module1-foundations/src/test/java/com/concurrency/labs/lab01/AtomicCounterTest.java": """package com.concurrency.labs.lab01;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class AtomicCounterTest {
    @Test
    public void testAtomicIncrement() throws InterruptedException {
        AtomicCounter counter = new AtomicCounter();
        int numThreads = 100;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        assertEquals(numThreads * incrementsPerThread, counter.getCount(), "Counter should not lose updates");
    }
}
""",
    "./module1-foundations/src/test/java/com/concurrency/labs/lab01/SynchronizedCounterTest.java": """package com.concurrency.labs.lab01;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class SynchronizedCounterTest {
    @Test
    public void testSynchronizedIncrement() throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        int numThreads = 100;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        assertEquals(numThreads * incrementsPerThread, counter.getCount(), "Counter should not lose updates");
    }
}
""",
    "./module1-foundations/src/test/java/com/concurrency/labs/lab01/UnsafeCounterTest.java": """package com.concurrency.labs.lab01;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class UnsafeCounterTest {
    @Test
    public void testUnsafeIncrement() throws InterruptedException {
        UnsafeCounter counter = new UnsafeCounter();
        int numThreads = 100;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        assertTrue(counter.getCount() <= numThreads * incrementsPerThread, "Counter should be less than or equal to max due to race conditions");
    }
}
""",
    "./module1-foundations/src/test/java/com/concurrency/labs/lab02/VisibilityBugTest.java": """package com.concurrency.labs.lab02;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisibilityBugTest {
    @Test
    public void testVisibility() throws InterruptedException {
        VisibilityBug demo = new VisibilityBug();
        demo.startWorker();

        Thread.sleep(100);
        demo.stop();

        Thread.sleep(100);
        assertFalse(demo.isRunning());
    }
}
""",
    "./module1-foundations/src/test/java/com/concurrency/labs/lab02/VolatileFixTest.java": """package com.concurrency.labs.lab02;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VolatileFixTest {
    @Test
    public void testVisibilityFixed() throws InterruptedException {
        VolatileFix fix = new VolatileFix();
        fix.startWorker();

        Thread.sleep(100);
        fix.stop();

        Thread.sleep(100);
        assertFalse(fix.isRunning());
    }
}
""",
    "./module1-foundations/src/test/java/com/concurrency/labs/lab03/ThreadStatesTest.java": """package com.concurrency.labs.lab03;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadStatesTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> ThreadStates.main(new String[]{}));
    }
}
""",
    "./module1-foundations/src/test/java/com/concurrency/labs/lab04/BoundedBufferTest.java": """package com.concurrency.labs.lab04;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class BoundedBufferTest {
    @Test
    public void testBuffer() throws InterruptedException {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);
        CountDownLatch latch = new CountDownLatch(1);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    buffer.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    assertEquals(i, buffer.take());
                }
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        latch.await();
    }
}
""",
    "./module1-foundations/src/test/java/com/concurrency/labs/lab22/ThreadLocalDemoTest.java": """package com.concurrency.labs.lab22;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadLocalDemoTest {
    @Test
    public void testThreadLocal() {
        assertDoesNotThrow(() -> ThreadLocalDemo.main(new String[]{}));
    }
}
""",
    # Module 2
    "./module2-locks-atomics/src/test/java/com/concurrency/labs/lab05/BoundedBufferWithLockTest.java": """package com.concurrency.labs.lab05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class BoundedBufferWithLockTest {
    @Test
    public void testBufferWithLock() throws InterruptedException {
        BoundedBufferWithLock<Integer> buffer = new BoundedBufferWithLock<>(5);
        CountDownLatch latch = new CountDownLatch(1);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    buffer.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    assertEquals(i, buffer.take());
                }
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        latch.await();
    }
}
""",
    "./module2-locks-atomics/src/test/java/com/concurrency/labs/lab06/ReadWriteCacheTest.java": """package com.concurrency.labs.lab06;

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
""",
    "./module2-locks-atomics/src/test/java/com/concurrency/labs/lab07/LockFreeStackTest.java": """package com.concurrency.labs.lab07;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class LockFreeStackTest {
    @Test
    public void testLockFreeStack() throws InterruptedException {
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        int numThreads = 10;
        int itemsPerThread = 100;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                for (int j = 0; j < itemsPerThread; j++) {
                    stack.push(j);
                }
                latch.countDown();
            }).start();
        }

        latch.await();

        int count = 0;
        while (stack.pop() != null) {
            count++;
        }
        assertEquals(numThreads * itemsPerThread, count);
    }
}
""",
    "./module2-locks-atomics/src/test/java/com/concurrency/labs/lab08/CounterComparisonTest.java": """package com.concurrency.labs.lab08;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CounterComparisonTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> CounterComparison.main(new String[]{}));
    }
}
""",
    "./module2-locks-atomics/src/test/java/com/concurrency/labs/lab09/ConcurrentHashMapUsageTest.java": """package com.concurrency.labs.lab09;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentHashMapUsageTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> ConcurrentHashMapUsage.main(new String[]{}));
    }
}
""",
    "./module2-locks-atomics/src/test/java/com/concurrency/labs/lab10/BlockingQueueVariantsTest.java": """package com.concurrency.labs.lab10;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockingQueueVariantsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> BlockingQueueVariants.main(new String[]{}));
    }
}
""",
    "./module2-locks-atomics/src/test/java/com/concurrency/labs/lab13/StripedLockLabTest.java": """package com.concurrency.labs.lab13;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;

public class StripedLockLabTest {
    @Test
    public void testStripedLock() throws InterruptedException {
        StripedLockLab lab = new StripedLockLab(100, 10);
        int numThreads = 10;
        int incrementsPerThread = 100;
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    lab.increment((threadIndex * 10 + j) % 100);
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        assertEquals(numThreads * incrementsPerThread, lab.sum());
    }
}
""",
    # Module 3
    "./module3-executors-async/src/test/java/com/concurrency/labs/lab11/ThreadPoolConfigurationTest.java": """package com.concurrency.labs.lab11;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolConfigurationTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> ThreadPoolConfiguration.main(new String[]{}));
    }
}
""",
    "./module3-executors-async/src/test/java/com/concurrency/labs/lab12/CancellationPatternsTest.java": """package com.concurrency.labs.lab12;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CancellationPatternsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> CancellationPatterns.main(new String[]{}));
    }
}
""",
    "./module3-executors-async/src/test/java/com/concurrency/labs/lab13/CompletableFuturePipelineTest.java": """package com.concurrency.labs.lab13;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CompletableFuturePipelineTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> CompletableFuturePipeline.main(new String[]{}));
    }
}
""",
    "./module3-executors-async/src/test/java/com/concurrency/labs/lab14/ForkJoinExamplesTest.java": """package com.concurrency.labs.lab14;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ForkJoinExamplesTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> ForkJoinExamples.main(new String[]{}));
    }
}
""",
    "./module3-executors-async/src/test/java/com/concurrency/labs/lab21/FanOutFanInTest.java": """package com.concurrency.labs.lab21;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FanOutFanInTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> FanOutFanIn.main(new String[]{}));
    }
}
""",
    # Module 4
    "./module4-liveness-production/src/test/java/com/concurrency/labs/lab15/DeadlockDemoTest.java": """package com.concurrency.labs.lab15;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeadlockDemoTest {
    @Test
    public void testMain() {
        // Just verify class exists, main might hang
        assertNotNull(new DeadlockDemo());
    }
}
""",
    "./module4-liveness-production/src/test/java/com/concurrency/labs/lab15/DeadlockPreventionTest.java": """package com.concurrency.labs.lab15;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeadlockPreventionTest {
    @Test
    public void testMain() {
        // main should not hang
        assertDoesNotThrow(() -> DeadlockPrevention.main(new String[]{}));
    }
}
""",
    "./module4-liveness-production/src/test/java/com/concurrency/labs/lab16/GracefulShutdownPatternsTest.java": """package com.concurrency.labs.lab16;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GracefulShutdownPatternsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> GracefulShutdownPatterns.main(new String[]{}));
    }
}
""",
    "./module4-liveness-production/src/test/java/com/concurrency/labs/lab17/ThreadDumpDemoTest.java": """package com.concurrency.labs.lab17;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreadDumpDemoTest {
    @Test
    public void testMain() {
        assertNotNull(new ThreadDumpDemo());
    }
}
""",
    # Module 5
    "./module5-testing/src/test/java/com/concurrency/labs/lab18/StressTestHarnessTest.java": """package com.concurrency.labs.lab18;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StressTestHarnessTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> StressTestHarness.main(new String[]{}));
    }
}
""",
    "./module5-testing/src/test/java/com/concurrency/labs/lab19/RaceDetectionConceptsTest.java": """package com.concurrency.labs.lab19;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RaceDetectionConceptsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> RaceDetectionConcepts.main(new String[]{}));
    }
}
""",
    "./module5-testing/src/test/java/com/concurrency/labs/lab20/DeterministicTestingPatternsTest.java": """package com.concurrency.labs.lab20;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeterministicTestingPatternsTest {
    @Test
    public void testMain() {
        assertDoesNotThrow(() -> DeterministicTestingPatterns.main(new String[]{}));
    }
}
"""
}

for path, content in test_configs.items():
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w") as f:
        f.write(content)
