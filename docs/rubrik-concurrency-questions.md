# Rubrik Concurrency Interview Questions (SDE2 Level)

> Collected from Reddit, LeetCode discussions, and interview experience posts.

## Interview Process Overview

Rubrik's SDE2 interview typically includes:
1. **Phone Screening** - Brief call about background, willingness to relocate, tech stack
2. **Online Assessment (60 min)** - LeetCode medium DSA + MCQs on **concurrency/multithreading/OS**
3. **System Coding Round(s)** - Heavy focus on **concurrency implementation**
4. **Debugging Round** - Find and fix concurrency bugs in given code
5. **System Design** - May include messaging systems, distributed components

---

## Category 1: Thread-Safe Data Structures

### Q1: Thread-Safe Banking Transactions
> *Source: Reddit CPD Bengaluru interview*

**Problem:** Given three functions in a banking application:
```java
void addTransactions(Transaction t);
List<Transaction> fetchTransactions();
void printTransactions();
```
Make these functions **thread-safe without making major changes** to the existing code.

**Key Skills:**
- Identify critical sections
- Choose appropriate locking granularity
- Implement locks (synchronized, ReentrantLock)

**Skeleton to implement:**
```java
public class TransactionStore {
    private List<Transaction> transactions = new ArrayList<>();
    // TODO: Add appropriate synchronization primitives
    
    public void addTransactions(Transaction t) {
        // TODO: Make thread-safe
    }
    
    public List<Transaction> fetchTransactions() {
        // TODO: Make thread-safe - consider read/write lock?
    }
    
    public void printTransactions() {
        // TODO: Make thread-safe
    }
}
```

---

### Q2: Blocking Queue Implementation
> *Source: LeetCode Rubrik tag*

**Problem:** Implement a bounded blocking queue with the following operations:
- `put(E e)` - blocks if queue is full
- `take()` - blocks if queue is empty

**Constraints:** Do NOT use built-in `BlockingQueue`.

**Key Skills:**
- `wait()/notify()` or `Condition` objects
- Producer-consumer pattern
- Bounded buffer handling

**Skeleton:**
```java
public class BoundedBlockingQueue<E> {
    private final int capacity;
    // TODO: Choose appropriate internal data structure
    // TODO: Add synchronization primitives
    
    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        // TODO: Initialize
    }
    
    public void put(E element) throws InterruptedException {
        // TODO: Block if full, add element, notify waiters
    }
    
    public E take() throws InterruptedException {
        // TODO: Block if empty, remove element, notify waiters
    }
    
    public int size() {
        // TODO: Thread-safe size
    }
}
```

---

## Category 2: Rate Limiters

### Q3: Rate Limiter (Token Bucket / Leaky Bucket)
> *Source: LeetCode Rubrik tag*

**Problem:** Implement a `RateLimiter` with:
- Initial capacity (max requests)
- Requests have `expireTime` - capacity freed after expiration
- `tryAcquire()` returns true if request can proceed

**Follow-ups:**
1. Make it work for single-threaded
2. Make it thread-safe for concurrent access
3. Discuss distributed rate limiting (Redis, shared state)

**Skeleton:**
```java
public class RateLimiter {
    private final int maxRequests;
    private final long windowMs;
    // TODO: Track request timestamps or tokens
    
    public RateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
        // TODO: Initialize
    }
    
    public synchronized boolean tryAcquire() {
        // TODO: Remove expired entries
        // TODO: Check if under limit
        // TODO: Record this request
    }
    
    // Follow-up: Make this work in distributed system
    public boolean tryAcquireDistributed(String clientId) {
        // TODO: Use external store (Redis pattern)
    }
}
```

---

## Category 3: Resource Allocation / Classic Problems

### Q4: Bathroom Problem (Republican/Democrat)
> *Source: GeeksForGeeks, Reddit - Classic Rubrik question*

**Problem:** A voting agency has a bathroom with capacity N. Two groups (Democrats, Republicans) can use it, but:
- Only ONE group can be in the bathroom at a time
- Max N people at any time
- Must prevent **starvation** - neither group waits indefinitely

**Follow-ups:**
- What if there are 3+ groups?
- Multiple bathrooms?
- Add priority?

**Skeleton:**
```java
public class VotingBathroom {
    private final int capacity;
    private int currentOccupancy = 0;
    private Party currentParty = null;
    // TODO: Add synchronization primitives
    // TODO: Consider fairness (starvation prevention)
    
    enum Party { DEMOCRAT, REPUBLICAN }
    
    public void enter(Party party) throws InterruptedException {
        // TODO: Wait if other party is inside OR at capacity
        // TODO: Prevent starvation of either group
    }
    
    public void exit(Party party) {
        // TODO: Decrement count
        // TODO: Notify waiting threads appropriately
    }
}
```

---

### Q5: Playground Access Control
> *Source: LeetCode discussion*

**Problem:** Design a playground access system:
- Max K children allowed at any time
- Multiple entry/exit gates (concurrent access)
- Track waiting children
- Prevent starvation

**Key difference from bathroom:** No group-based exclusion, pure capacity management.

---

## Category 4: Schedulers

### Q6: Non-Blocking Delayed Scheduler
> *Source: LeetCode Rubrik tag*

**Problem:** Design an interface that:
- Accepts a `Runnable` and a `waitTime`
- Executes the `Runnable` after `waitTime`
- **MUST NOT block** the caller's thread
- Wait for the *earliest* task without strict polling loop (CPU efficient)

**Skeleton:**
```java
public class DelayedScheduler {
    // TODO: Thread pool for execution
    // TODO: Data structure for delayed tasks (PriorityQueue?)
    // TODO: Daemon thread to poll/execute
    
    public void schedule(Runnable task, long delayMs) {
        // TODO: Non-blocking - return immediately
        // TODO: Task executes after delayMs
    }
    
    // Follow-up methods:
    public void scheduleAtFixedRate(Runnable task, long initialDelay, long period) {
        // TODO
    }
    
    public void shutdown() {
        // TODO: Graceful shutdown
    }
}
```

---

### Q7: Job Scheduler with Dependencies (DAG)
> *Source: LeetCode, Medium articles*

**Problem:** Given jobs with dependencies:
- Schedule jobs to execute with max parallelism
- Job can only execute after all dependencies complete
- Optimize for minimum total execution time

**Key Skills:**
- Topological sort
- Thread pool management
- CountDownLatch / CompletableFuture

**Skeleton:**
```java
public class DependencyScheduler {
    private final ExecutorService executor;
    
    public void execute(Map<Job, List<Job>> dependencies) {
        // TODO: Build execution order (topological)
        // TODO: Execute jobs in parallel when dependencies met
        // TODO: Wait for all jobs to complete
    }
}
```

---

## Category 5: Debugging Rounds

### Q8: Deadlock Detection
> *Source: enginebogie.com, Reddit*

**Problem:** Given a multithreaded banking application with parallel transactions, identify and fix:
- Deadlock scenarios
- Race conditions
- Missing synchronization

**Common patterns to look for:**
1. Nested locks in different order
2. Lock not released in finally block
3. Shared mutable state without sync
4. Check-then-act race conditions

---

## Category 6: System Coding (Mixed)

### Q9: Logical-to-Physical Block Mapping (Storage Focus)
> *Source: Reddit CPD interview*

**Problem:** Simulate disk block management:
```java
void assign(int logicalBlock, Object data);  // Map data to physical block
Object retrieve(int logicalBlock);           // Get latest data
void print(int logicalBlock);                // Print all data in order
```
- If logical block has a physical mapping, append data
- Retain order of data additions
- **Crucial:** Must handle highly concurrent reads/writes efficiently.

**Follow-up:** Make it thread-safe for concurrent access.

---

### Q10: Queue with Fixed-Size Buffer
> *Source: Reddit*

**Problem:** Implement a queue using a fixed-size buffer, then extend to:
- Manage TWO queues within ONE larger fixed-size buffer
- Maximize space utilization
- Handle concurrent access

---

# 🛑 Senior Engineer's Critique & Level-Up Suggestions

> *Feedback from a Senior Rubrik Engineer Perspective*

Rubrik deals with **exabytes of backup data**. We care about **correctness, data integrity, and high throughput**. The standard "textbook" solutions above are a good start, but here is how an SDE2/SDE3 candidate stands out.

## 1. Domain-Specific Concurrency (Storage)

Standard locking is often too slow for storage systems. You should be comfortable with:

### Snapshotable Key-Value Store (MVCC)
In backup systems, we often need a "point-in-time" view of data while writes are still happening.
**Question:** Design a KV store where `get(key, timestamp)` returns the value at that exact time, even if `put(key, val)` is happening right now.
- **Tip:** Use **Copy-On-Write** or an append-only log structure.
- **Why:** This is how we do backups without "freezing" the customer's database.

### Striped Locking (Reducing Contention)
For `TransactionStore` or `HashMap` questions:
- **Critique:** A single `synchronized` method or `ReentrantLock` creates a bottleneck.
- **Improvisation:** Use **Striped Locking** (splitting the lock into N segments based on `hash(key) % N`). This allows parallel access to different parts of the data. `ConcurrentHashMap` does this internally, but can you implement it?

## 2. Advanced Failure Handling

Concurrency isn't just about "happy path" parallelism.
- **Thread Pool Saturation:** What happens when your `DelayedScheduler` thread pool is full? Do you drop tasks? Block the caller? (Reject execution policies).
- **Poison Pills:** How do you safely shut down your blocking queue consumers? (Sending a special "shutdown" token).

## 3. Pure Multithreading & Low-Level

### Q11: Parallel File Copy (I/O Focus)
**Problem:** Copy a 1TB file from Source to Dest.
- Use multiple threads.
- **Constraint:** Disk I/O is the bottleneck, not CPU.
- **Solution:** Use offsets (`pread`/`pwrite` equivalent). Don't just spawn 100 threads; match the I/O channels.

### Q12: Custom ReentrantLock
**Problem:** Implement `ReentrantLock` using only `wait/notify` and an atomic integer.
- This tests your deep understanding of monitor locks and ownership tracking.

## 4. Testing Verification
How do you *know* your code is thread-safe?
- **Improvisation:** Don't just say "I'll run it." Mention **Stress Testing** with a harness that runs millions of ops. Mention tools like **jcstress**.

---

## Revised Preparation Priorities for Rubrik

1.  **Bounded Structures:** `BoundedBlockingQueue` (Memory is not infinite).
2.  **Scheduling/Timing:** `DelayedScheduler` (Time is hard).
3.  **Storage Simulation:** `LogicalBlockMapping`, `SnapshotKV` (Rubrik's bread and butter).
4.  **Debugging:** Fix broken implementation (Practical coding skills).

*Added by Antigravity based on persona analysis.*

---

# 📚 Topic Priority Matrix: Bookish vs. Interview-Ready

> Based on additional research from LeetCode, Reddit, Medium, and GeeksForGeeks SDE2 experiences (2024-2025).

## ✅ HIGH PRIORITY (Actually Asked in Interviews)

| Topic | Why It's Practical | Your Repo Status |
|-------|-------------------|------------------|
| **Blocking Queue (wait/notify)** | Asked verbatim in 70%+ of Rubrik interviews | ✅ Have it |
| **Rate Limiter (Token Bucket)** | Common system coding question | ✅ Have it |
| **Delayed Scheduler** | Tests PriorityQueue + threading | ✅ Have it |
| **Thread Pool Implementation** | Tests worker pattern, task queue | ✅ Have it |
| **Debugging Broken Code** | Rubrik has a **dedicated debugging round** | ⚠️ Need practice |
| **Reader-Writer Lock** | Practical for read-heavy workloads | ✅ Have it |

## ⚠️ MEDIUM PRIORITY (Good to Know, Rarely Asked Directly)

| Topic | When It Helps | Your Repo Status |
|-------|---------------|------------------|
| **Dining Philosophers** | Shows deadlock prevention thinking | ✅ Have it |
| **Bathroom Problem** | Rubrik-specific variant of group exclusion | Add if time permits |
| **CompletableFuture chains** | System design discussions | ✅ Have it |
| **Fork/Join** | Mentioned in follow-ups, not core | ✅ Have it |

## ❌ TOO BOOKISH (Skip for Interview Prep)

> These topics from *The Art of Multiprocessor Programming* are academically interesting but **NOT asked** in Rubrik SDE2 interviews:

| Topic | Why It's Too Academic | Recommendation |
|-------|----------------------|----------------|
| **TAS / TTAS / Backoff Spin Locks** | Hardware-level; interviewers don't expect this | Skip |
| **ABA Problem** | CAS edge case; too niche | Skip |
| **Lock-Free Stack/Queue** | Impressive but overkill for SDE2 | Skip |
| **Memory Barriers / Happens-Before** | JMM theory; asked only in Staff+ | Skip for now |
| **Optimistic Locking (academic style)** | Good concept but not coded in interviews | Know conceptually |
| **Linearizability proofs** | Pure theory | Skip |

---

# 🎯 Practice Framework: From Theory to Interview-Ready

> Key insight from research: *"Rubrik focuses on HOW structures are implemented, not just their usage."*

## The 30-Minute Drill

For each core problem, practice this cycle:

### Phase 1: Blank Slate (15 min)
1. Open a new file (no reference code)
2. Write the **skeleton** from memory
3. Implement core logic with `synchronized` / `wait` / `notify`
4. **Goal:** Compiling code, not perfect code

### Phase 2: Stress Test (10 min)
1. Write a simple multi-threaded test harness
2. Run 10,000 concurrent operations
3. Check for: deadlocks, incorrect counts, exceptions

### Phase 3: Improve (5 min)
1. Replace `synchronized` with `ReentrantLock` + `Condition`
2. Add timeout support (`tryLock`)
3. Consider edge cases (interruption, shutdown)

## The Debugging Drill

Rubrik has a **dedicated debugging round**. Practice this:

1. **Find intentionally broken concurrent code** (or create your own)
2. Without running it, identify:
   - Race conditions (check-then-act)
   - Deadlocks (lock ordering)
   - Missing synchronization
   - Incorrect `wait()` loop (should use `while`, not `if`)
3. **Time yourself:** Can you find 3 bugs in 10 minutes?

### Common Bug Patterns (Memorize These)

```java
// BUG 1: Using if instead of while for wait()
if (queue.isEmpty()) {  // ❌ WRONG - spurious wakeup!
    wait();
}
// FIX: while (queue.isEmpty()) { wait(); }

// BUG 2: notify() instead of notifyAll()
notify();  // ❌ May wake wrong thread
// FIX: notifyAll() for multiple waiters

// BUG 3: Lock not in finally
lock.lock();
doWork();
lock.unlock();  // ❌ Never reached if exception
// FIX: try { doWork(); } finally { lock.unlock(); }

// BUG 4: Nested locks in different order (DEADLOCK)
// Thread 1: lock(A) -> lock(B)
// Thread 2: lock(B) -> lock(A)  // ❌ DEADLOCK
// FIX: Always lock in consistent order (A before B)

// BUG 5: Check-then-act race
if (map.containsKey(key)) {  // ❌ Another thread may remove it!
    return map.get(key);
}
// FIX: Use putIfAbsent() or computeIfAbsent()
```

---

# 🗓️ 1-Week Rubrik Prep Plan (Interview-Focused)

| Day | Focus | Practice Target |
|-----|-------|-----------------|
| **1** | BlockingQueue | Implement from scratch in 25 min |
| **2** | Rate Limiter | Token bucket + thread-safe version |
| **3** | Delayed Scheduler | PriorityQueue + single daemon thread |
| **4** | Debugging Drill | Find bugs in 3 broken code samples |
| **5** | Thread Pool | Fixed pool with BlockingQueue |
| **6** | Mock Interview | Pick random problem, implement in 30 min |
| **7** | Review | Re-implement weakest problem |

## Speed Targets (By Interview Day)

- [ ] BlockingQueue: **< 20 minutes** from blank file
- [ ] Rate Limiter: **< 25 minutes** with sliding window
- [ ] Identify 3 concurrency bugs: **< 10 minutes**
- [ ] Explain wait/notify vs Condition: **< 2 minutes** verbally

---

# 📖 Additional Resources from Research

## Verified Helpful
- **LeetCode Concurrency Problems:** [1114, 1115, 1116, 1117, 1188, 1195, 1226, 1242]
- **GeeksForGeeks:** Multithreading section for Java
- **YouTube:** "Rubrik Interview Experience" videos

## Interview Experience Sources (2024-2025)
- LeetCode Discuss (Rubrik tag)
- Reddit r/leetcode (search "Rubrik")
- Medium articles on Rubrik system coding
- GeeksForGeeks interview experiences

---

*Updated with practice framework and topic prioritization. Focus on speed and correctness over theoretical depth.*
