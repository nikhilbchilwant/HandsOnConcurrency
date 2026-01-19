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

| Problem | Key Concepts | Companies |
|---------|--------------|-----------|
| **BoundedBlockingQueue** | wait/notify, guarded blocks | Rubrik, Amazon, Dropbox |
| **SimpleReadWriteLock** | Reader-writer synchronization | LinkedIn, Microsoft |
| **DiningPhilosophers** | Deadlock prevention | Google, Meta |

### 🟠 Tier 2: Thread Coordination

| Problem | Key Concepts | Companies |
|---------|--------------|-----------|
| **PrintInOrder** | Semaphores, condition variables | Amazon, Microsoft |
| **EvenOddPrinter** | Turn-based coordination | Goldman Sachs, Flipkart |

### 🟡 Tier 3: System Components (SDE2 Sweet Spot)

| Problem | Key Concepts | Companies |
|---------|--------------|-----------|
| **TokenBucketRateLimiter** | Lazy refill, time management | Stripe, Cloudflare |
| **SimpleThreadPool** | Worker threads, task queue | Uber, Amazon |
| **DelayedTaskScheduler** | PriorityQueue, timed waiting | Oracle, PayPal |
| **ConcurrentLRUCache** | Fine-grained locking | Meta, Netflix |
| **ConcurrentMessageQueue** | Visibility timeout, SQS-like | Amazon, Uber |

### 🟢 Tier 4: Java Specifics

| Problem | Key Concepts | Companies |
|---------|--------------|-----------|
| **DCL Singleton** | Double-checked locking, volatile | Common everywhere |
| **Holder Singleton** | Initialization-on-demand | Common everywhere |
| **Enum Singleton** | Enum-based singleton | Common everywhere |

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
