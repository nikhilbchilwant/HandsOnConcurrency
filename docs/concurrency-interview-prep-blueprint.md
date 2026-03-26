# Concurrency Interview Prep Blueprint

> A practical guide to transform the HandsOnConcurrency repo into an interview-ready resource.  
> Based on SDE2 interview experiences from: **Rubrik, Dropbox, Uber, Amazon, Google, Meta, Stripe**

---

## Part 1: Company-Specific Insights

### Which Companies Ask What

| Company | Concurrency Intensity | Signature Style |
|---------|----------------------|-----------------|
| **Rubrik** | ⭐⭐⭐⭐⭐ | Dedicated system coding + debugging rounds |
| **Dropbox** | ⭐⭐⭐⭐⭐ | Multi-part: solve first, then add concurrency |
| **Uber** | ⭐⭐⭐⭐ | "Machine Coding" - build mini-Kafka in 90 min |
| **Amazon** | ⭐⭐⭐ | Thread-safe data structures in system design |
| **Stripe** | ⭐⭐ | Production-ready code, implicit concurrency |
| **Google/Meta** | ⭐⭐ | Algorithm focus, concurrency in system design |

---

## Part 2: Topic Relevance Matrix

### ✅ HIGH PRIORITY - Asked by Multiple Companies

| Topic | Companies | Your Module |
|-------|-----------|-------------|
| **Bounded Blocking Queue** | Rubrik, Dropbox, Amazon, Uber | `module6-classic-problems/tier1` |
| **Rate Limiter (Token Bucket)** | Rubrik, Amazon, all API companies | `module6-classic-problems/tier3` |
| **Thread Pool Implementation** | Uber, Amazon | `module6-classic-problems/tier3` |
| **Delayed Scheduler** | Rubrik | `module6-classic-problems/tier3` |
| **Reader-Writer Lock** | Dropbox, Rubrik | `module6-classic-problems/tier1` |
| **Producer-Consumer** | All companies | `module1-foundations/lab04` |
| **Debugging Broken Code** | Rubrik, Dropbox | **⚠️ MISSING - Need to add** |

### ⚠️ MEDIUM PRIORITY - Good to Know

| Topic | Companies | Your Module |
|-------|-----------|-------------|
| **Dining Philosophers** | Academic favorite, shows deadlock thinking | `module6-classic-problems/tier1` |
| **CompletableFuture chains** | Uber, Amazon system design | `module3-executors-async` |
| **Fork/Join** | Google (algorithm optimization) | `module3-executors-async` |
| **LRU Cache (thread-safe)** | Amazon, Meta | `module6-classic-problems/tier3` |

### 📚 ACADEMIC - Rarely Asked (Add Relevance Notes)

| Topic | Why Academic | Your Module | Action |
|-------|--------------|-------------|--------|
| **TAS / TTAS / Backoff Spin Locks** | Hardware-level, Staff+ only | `module2-locks-atomics` | Add disclaimer |
| **ABA Problem** | CAS edge case, too niche | If exists | Add disclaimer |
| **Lock-Free Stack/Queue** | Impressive but overkill | If exists | Add disclaimer |
| **Memory Barriers** | JMM theory | If exists | Add disclaimer |

---

## Part 3: New Modules to Generate

### Module A: Debugging Labs (`module-debug`)

**Purpose:** Rubrik and Dropbox have dedicated debugging rounds.

**Structure:**
```
module-debug/
├── src/main/java/com/concurrency/debug/
│   ├── broken/
│   │   ├── BrokenBlockingQueue.java     # Bug: if instead of while
│   │   ├── BrokenRateLimiter.java       # Bug: check-then-act race
│   │   ├── DeadlockBanking.java         # Bug: nested locks wrong order
│   │   ├── MissingVolatile.java         # Bug: visibility issue
│   │   └── NotifyVsNotifyAll.java       # Bug: using notify() incorrectly
│   └── README.md                        # Instructions: Find N bugs
├── src/test/java/.../debug/
│   └── DebugTestHarness.java            # Stress tests that expose bugs
└── pom.xml
```

**Bugs to Include:**
1. `if (queue.isEmpty()) wait();` instead of `while`
2. `notify()` instead of `notifyAll()` with multiple waiters
3. Lock not released in `finally` block
4. Nested locks acquired in inconsistent order (deadlock)
5. Check-then-act: `if (map.containsKey(k)) return map.get(k);`
6. Missing `volatile` on shared flag
7. Double-checked locking done wrong

---

### Module B: Evolution Labs (`module-evolution`)

**Purpose:** Dropbox-style progression from simple to concurrent to optimized.

**Structure:**
```
module-evolution/
├── src/main/java/com/concurrency/evolution/
│   ├── ratelimiter/
│   │   ├── Step1_SingleThreaded.java    # No synchronization
│   │   ├── Step2_Synchronized.java      # Add synchronized
│   │   ├── Step3_LockBased.java         # ReentrantLock + Condition
│   │   └── Step4_Optimized.java         # Atomic + CAS
│   ├── cache/
│   │   ├── Step1_SimpleMap.java
│   │   ├── Step2_SynchronizedMap.java
│   │   ├── Step3_ReadWriteLock.java
│   │   └── Step4_ConcurrentHashMap.java
│   └── counter/
│       ├── Step1_Unsafe.java
│       ├── Step2_Synchronized.java
│       ├── Step3_AtomicLong.java
│       └── Step4_LongAdder.java
└── pom.xml
```

---

### Module C: Timed Challenges (`module-challenges`)

**Purpose:** Practice implementing from blank file under time pressure.

**Structure:**
```
module-challenges/
├── challenges/
│   ├── Challenge01_BlockingQueue.md     # 20 min target
│   ├── Challenge02_RateLimiter.md       # 25 min target
│   ├── Challenge03_ThreadPool.md        # 30 min target
│   ├── Challenge04_DelayedScheduler.md  # 30 min target
│   └── Challenge05_ReadWriteLock.md     # 25 min target
├── templates/                           # Empty skeletons to start from
│   ├── BlockingQueueTemplate.java
│   ├── RateLimiterTemplate.java
│   └── ...
├── solutions/                           # Reference after attempt
└── timer.py                             # Simple timer script
```

**Challenge Format (Example):**
```markdown
# Challenge 01: Bounded Blocking Queue

**Time Target:** 20 minutes  
**Companies:** Rubrik, Dropbox, Amazon, Uber

## Requirements
- `put(E e)` blocks if full
- `take()` returns E, blocks if empty
- Thread-safe without using java.util.concurrent.BlockingQueue

## Constraints
- Use only: synchronized, wait(), notify(), notifyAll()
- OR: ReentrantLock + Condition

## Evaluation
- [ ] Compiles without errors
- [ ] Passes basic single-threaded test
- [ ] Passes concurrent stress test (1000 ops, 10 threads)
- [ ] No deadlock under load
```

---

### Module D: Machine Coding (`module-machine-coding`)

**Purpose:** Uber-style 60-90 minute mini-system builds.

**Structure:**
```
module-machine-coding/
├── mini-pubsub/
│   ├── README.md                        # Requirements + time limit
│   ├── skeleton/
│   │   ├── Publisher.java
│   │   ├── Subscriber.java
│   │   └── MessageBroker.java
│   └── tests/
├── mini-executor/
│   ├── README.md
│   ├── skeleton/
│   └── tests/
└── mini-cache/
    ├── README.md
    ├── skeleton/
    └── tests/
```

---

## Part 4: Updates to Existing Modules

### Add Relevance Headers to Each Lab

**Format to add at top of each Java file:**
```java
/**
 * INTERVIEW RELEVANCE:
 * - Companies: [Rubrik, Dropbox, Amazon] or [Academic/Niche]
 * - Frequency: HIGH / MEDIUM / LOW
 * - Time to implement: ~20 minutes
 * 
 * KEY CONCEPTS:
 * - wait/notify pattern
 * - Bounded buffer
 */
```

### Modules to Update

| Module | Files to Update | Relevance Tag |
|--------|-----------------|---------------|
| `module1-foundations/lab04` | BoundedBuffer.java | HIGH - Rubrik, Dropbox |
| `module2-locks-atomics/lab05` | BoundedBufferWithLock.java | HIGH - Amazon, Uber |
| `module2-locks-atomics/lab07` | LockFreeStack.java | 📚 ACADEMIC - Staff+ only |
| `module6-classic-problems/tier1` | All files | HIGH |
| `module6-classic-problems/tier3` | TokenBucketRateLimiter.java | HIGH - All API companies |

---

## Part 5: Common Bug Patterns Reference

To add as `docs/common-bugs-cheatsheet.md`:

```java
// BUG 1: if instead of while (spurious wakeup)
if (queue.isEmpty()) { wait(); }  // ❌
while (queue.isEmpty()) { wait(); }  // ✅

// BUG 2: notify vs notifyAll
notify();  // ❌ May wake wrong thread type
notifyAll();  // ✅ Always safe for multiple waiter types

// BUG 3: Lock not in finally
lock.lock();
doWork();
lock.unlock();  // ❌ Not reached if exception
// ✅ Fix:
lock.lock();
try { doWork(); } finally { lock.unlock(); }

// BUG 4: Inconsistent lock ordering (DEADLOCK)
// Thread 1: lock(A) -> lock(B)
// Thread 2: lock(B) -> lock(A)  // ❌ DEADLOCK
// ✅ Fix: Always acquire locks in same order

// BUG 5: Check-then-act race
if (map.containsKey(key)) { return map.get(key); }  // ❌
// ✅ Fix: map.computeIfAbsent(key, k -> defaultValue);

// BUG 6: Missing volatile on flag
boolean running = true;  // ❌ Other threads may not see update
volatile boolean running = true;  // ✅

// BUG 7: Double-checked locking (broken without volatile)
if (instance == null) {
    synchronized(this) {
        if (instance == null) {
            instance = new Singleton();  // ❌ Without volatile
        }
    }
}
```

---

## Part 6: Implementation Checklist

### Phase 1: Create New Modules
- [ ] Create `module-debug/` with 5 broken implementations
- [ ] Create `module-evolution/` with rate limiter evolution (4 steps)
- [ ] Create `module-challenges/` with 5 timed challenges
- [ ] Create `module-machine-coding/` with mini-pubsub skeleton

### Phase 2: Update Existing Modules
- [ ] Add relevance headers to `module6-classic-problems/tier1/`
- [ ] Add relevance headers to `module6-classic-problems/tier3/`
- [ ] Add 📚 ACADEMIC disclaimer to `LockFreeStack.java`
- [ ] Add 📚 ACADEMIC disclaimer to any TAS/TTAS labs if they exist

### Phase 3: Documentation
- [ ] Create `docs/common-bugs-cheatsheet.md`
- [ ] Update `README.md` with new learning paths
- [ ] Add timer script for challenges

---

## Part 7: LeetCode Problems Reference

### Concurrency Problems to Practice
| # | Problem | Difficulty | Companies |
|---|---------|------------|-----------|
| 1114 | Print in Order | Easy | All |
| 1115 | Print FooBar Alternately | Medium | Dropbox |
| 1116 | Print Zero Even Odd | Medium | Dropbox |
| 1117 | Building H2O | Medium | Google |
| 1188 | Design Bounded Blocking Queue | Medium | Rubrik, Amazon |
| 1195 | Fizz Buzz Multithreaded | Medium | All |
| 1226 | The Dining Philosophers | Medium | Academic |
| 1242 | Web Crawler Multithreaded | Medium | Uber |

---

## Part 8: Modernizing JCiP for Java 17

> "Java Concurrency in Practice" is the bible of concurrency, but it was written for Java 5/6. Here is how to map its core lessons to Java 17+.

### 1. Immutability & Safe Publication (JCiP Ch 3)
- **Old Way:** Manually creating immutable classes with `private final` fields and explicit constructors.
- **Modern Way:** Use **Java 17 Records**. They are final by default, fields are final, and they provide safe publication through their canonical constructor.
- **Lab:** `module1-foundations/lab06`

### 2. State Management (JCiP Ch 4)
- **Old Way:** Using `int` constants or `enums` with complex state transitions.
- **Modern Way:** Use **Sealed Classes** and **Switch Expressions**. This provides compile-time exhaustiveness checks and cleaner concurrent state machine logic.
- **Lab:** `module1-foundations/lab07`

### 3. Read-Heavy Optimization (JCiP Ch 13)
- **Old Way:** `ReentrantReadWriteLock`.
- **Modern Way:** **StampedLock**. It provides "optimistic reading" which can significantly outperform RWLock by avoiding lock acquisition for reads unless a write actually occurs.
- **Lab:** `module2-locks-atomics/lab12`

### 4. Annotation-Driven Documentation
- Use JCiP-style annotations to document your intent during interviews.
- `@ThreadSafe`, `@NotThreadSafe`, `@GuardedBy("lock")`
- **Location:** `com.concurrency.annotations`

---

## Part 9: Implementation Status

| Module | Status | JCiP/Java 17 Focus |
|--------|--------|--------------------|
| `module1-foundations` | ✅ Updated | Records, Sealed Classes, JCiP Annotations |
| `module2-locks-atomics` | ✅ Updated | Synchronizers (Latch, Semaphore), StampedLock |
| `module9-debug` | ✅ Exists | Common bug patterns (wait/notify, visibility) |
| `module10-evolution` | ✅ Exists | Evolution from simple to optimized (CAS/Atomic) |

