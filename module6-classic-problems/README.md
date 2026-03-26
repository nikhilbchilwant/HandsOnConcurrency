# Module 6: Classic Interview Problems

## Learning Objectives

By completing this module, you will be able to:
- Implement common concurrency interview problems from scratch
- Explain trade-offs between different synchronization approaches
- Recognize and avoid common concurrency pitfalls
- Complete implementations within interview time constraints

## Prerequisites

- Complete **modules 1-5** first (or have equivalent knowledge)

## Directory Structure

```
module6-classic-problems/
├── src/main/java/com/concurrency/
│   ├── problems/          ← Skeletons (YOU implement these)
│   │   ├── tier1/         # Core locking problems
│   │   ├── tier2/         # Thread coordination
│   │   ├── tier3/         # System components
│   │   └── tier4/         # Java specifics
│   └── solutions/         ← Reference implementations
│       ├── tier1/
│       ├── tier2/
│       └── tier3/
```

## Problems by Tier

### 🔴 Tier 1: Core Locking (MUST MASTER)

| Problem | Key Concepts | JCiP Reference |
|---------|--------------|----------------|
| **BoundedBlockingQueue** | wait/notify, guarded blocks | Ch 14 (Condition Queues) |
| **SimpleReadWriteLock** | Reader-writer synchronization | Ch 13.5 (Read-Write Locks) |
| **DiningPhilosophers** | Deadlock prevention | Ch 10.1 (Deadlock) |
| **UnisexBathroom** | Group exclusion, starvation prevention | Ch 14 (Condition Queues) |

### 🟠 Tier 2: Thread Coordination

| Problem | Key Concepts | JCiP Reference |
|---------|--------------|----------------|
| **PrintInOrder** | Semaphores, condition variables | Ch 5.5 (Synchronizers) |
| **EvenOddPrinter** | Turn-based coordination | Ch 14.2 (Condition Queues) |

### 🟡 Tier 3: System Components (SDE2 Sweet Spot)

| Problem | Key Concepts | JCiP Reference |
|---------|--------------|----------------|
| **TokenBucketRateLimiter** | Lazy refill, time management | Ch 11 (Performance) |
| **SimpleThreadPool** | Worker threads, task queue | Ch 8 (Thread Pools) |
| **DelayedTaskScheduler** | PriorityQueue, timed waiting | Ch 6 (Task Execution) |
| **ConcurrentLRUCache** | Fine-grained locking | Ch 11.4 (Lock Striping) |
| **ConcurrentMessageQueue** | Visibility timeout, SQS-like | Ch 5.3 (Blocking Queues) |

### 🟢 Tier 4: Java Specifics

| Problem | Key Concepts | JCiP Reference |
|---------|--------------|----------------|
| **DCL Singleton** | Double-checked locking, volatile | Ch 16.2.4 (Double-Checked) |
| **Holder Singleton** | Initialization-on-demand | Ch 16.2.3 (Safe Publication) |
| **Enum Singleton** | Enum-based singleton | Ch 16.2.3 (Safe Publication) |

## How to Practice

1. **Pick a problem** from `problems/` directory
2. **Read the problem statement** (class-level Javadoc)
3. **Implement the TODO methods** without looking at solutions
4. **Run tests** to validate your implementation
5. **Only after solving**, compare with `solutions/` package

> ⚠️ **Important**: Solutions are in a separate package. Don't peek until you've tried!

## Interview Tips

- Start with `synchronized` + wait/notify, then mention Lock+Condition as improvement
- Always explain trade-offs ("I used notifyAll for safety, but...")
- Know the stdlib equivalents ("I know ArrayBlockingQueue exists, but...")

## Next Module

After mastering these problems, try **module7-capstone** for integrated projects.
