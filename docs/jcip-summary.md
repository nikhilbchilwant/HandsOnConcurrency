# Java Concurrency in Practice (JCiP) - Interview Prep Summary

> **Modernized for Java 17**  
> Based on the classic by Brian Goetz, updated with modern Java features (Records, Sealed Classes, StampedLock).

---

## 1. Fundamentals of Thread Safety

### Core Principles
- **It's the State, Stupid:** Thread safety is about managing access to **shared, mutable state**.
- **Three Ways to Fix Unsafe State:**
  1. Don't share the state variable across threads (**Thread Confinement**).
  2. Make the state variable immutable (**Immutability** - *Use Java 17 Records!*).
  3. Use synchronization whenever accessing the state variable.

### Definitions
- **Atomicity:** Operations that appear to happen all at once (e.g., `AtomicInteger.incrementAndGet()`).
- **Visibility:** Ensuring that a write to a variable by one thread is visible to a read by another (e.g., `volatile`, `synchronized`).
- **Publication:** Making an object available outside its current scope. Avoid "this" escaping during construction.

---

## 2. Objects and Composition (JCiP Ch 3 & 4)

### Safe Publication Patterns
1. Initialize in a `static` initializer.
2. Store in a `volatile` field.
3. Store in a `final` field (*Java 17 Records are final by default*).
4. Guarded by a lock (e.g., `synchronized` or `ReentrantLock`).

### Delegating Thread Safety
Instead of managing every lock manually, delegate to thread-safe building blocks like `ConcurrentHashMap` or `CopyOnWriteArrayList`.

**Modern Twist:** Use **Java 17 Records** for thread-safe data carriers. Since they are immutable, they are inherently thread-safe and can be safely shared without synchronization.

---

## 3. Building Blocks (JCiP Ch 5)

### Synchronizers to Know
| Synchronizer | Purpose | Key Method |
|--------------|---------|------------|
| **CountDownLatch** | Wait for N events to complete | `countDown()`, `await()` |
| **Semaphore** | Limit access to N resources (permits) | `acquire()`, `release()` |
| **CyclicBarrier** | Wait for all N threads to reach a point | `await()` |
| **Phaser** | Reusable barrier for complex phases | `arriveAndAwaitAdvance()` |
| **Exchanger** | Swap data between two threads | `exchange()` |

### Modern Collections
- `ConcurrentHashMap`: High-performance, CAS-based (Java 8+).
- `BlockingQueue`: Foundational for Producer-Consumer.
- `ConcurrentSkipListMap`: Thread-safe alternative to `TreeMap`.

---

## 4. Task Execution (JCiP Ch 6, 7, 8)

### Thread Pools (ExecutorService)
- **FixedThreadPool:** Bound by number of threads.
- **CachedThreadPool:** Dynamic, can grow (risky if tasks are long).
- **ScheduledThreadPool:** For periodic tasks.
- **WorkStealingPool:** (Java 8+) Uses Fork/Join.

**Modern Twist:** **Virtual Threads (Java 21)** are the biggest evolution here, but for **Java 17**, focus on `CompletableFuture` for non-blocking async pipelines.

---

## 5. Liveness, Performance, and Testing

### Hazards
- **Deadlock:** Cycle of waiting. *Avoid by consistent lock ordering.*
- **Starvation:** Thread never gets CPU time.
- **Livelock:** Threads too busy responding to each other to make progress.

### Performance Techniques
1. **Lock Splitting:** Using different locks for independent state variables.
2. **Lock Striping:** Dividing a data structure into chunks, each with its own lock (e.g., `ConcurrentHashMap` segments).
3. **Lock Coarsening:** Merging adjacent synchronized blocks (JVM does this automatically).
4. **Optimistic Reading:** Using **StampedLock (Java 8+)** to avoid heavy locks when reads are frequent and writes are rare.

---

## 6. JCiP Annotations (Interview Bonus)
Show you are a pro by mentioning these:
- `@ThreadSafe`: The class is thread-safe.
- `@NotThreadSafe`: The class is NOT thread-safe.
- `@GuardedBy("lockName")`: The field/method must only be accessed with the specified lock held.

---

## 7. Java 17 Concurrency Checklist

- [ ] **Records:** Use for immutable message passing and state.
- [ ] **Sealed Classes:** Use for state machine states in concurrency.
- [ ] **StampedLock:** Use for read-heavy workloads instead of `ReentrantReadWriteLock`.
- [ ] **CompletableFuture:** Prefer over manual `Future` or `Thread` management.
- [ ] **VarHandle:** (Advanced) Modern replacement for `Unsafe` or `AtomicFieldUpdater`.
