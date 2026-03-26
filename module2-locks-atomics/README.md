# Module 2: Locks and Atomics

## Learning Objectives

By completing this module, you will understand:
- ReentrantLock vs synchronized
- ReadWriteLock for read-heavy workloads
- Atomic classes (AtomicInteger, AtomicReference)
- ConcurrentHashMap internals and usage
- BlockingQueue implementations

## Prerequisites

- Complete **module1-foundations** first

## Labs

| Lab | Topic | Key Concepts | JCiP Reference |
|-----|-------|--------------|----------------|
| **lab05** | ReentrantLock | Lock/unlock, tryLock, fairness | Ch 13.1 (ReentrantLock) |
| **lab06** | ReadWriteLock | Read vs write locks | Ch 13.5 (Read-Write Locks) |
| **lab07** | Atomic Classes | CAS operations, AtomicInteger | Ch 15 (Atomics/CAS) |
| **lab08** | Counter Comparison | Synchronized vs Lock vs Atomic | Ch 15.3 (Atomic vs Locks) |
| **lab09** | ConcurrentHashMap | Thread-safe map, compute methods | Ch 5.2 (Concurrent Collections) |
| **lab10** | BlockingQueue | Producer-consumer pattern | Ch 5.3 (Blocking Queues) |
| **lab11** | Synchronizers | CountDownLatch, Semaphore | Ch 5.5 (Synchronizers) |
| **lab12** | Modern Locks | StampedLock, Optimistic Reading | Ch 13 (Explicit Locks Extension) |
| **lab13** | Lock Striping | Performance, reduced lock contention | Ch 11.4 (Performance Optimization) |

## How to Practice

1. Read the skeleton file and understand the problem
2. Implement the TODO sections
3. Run tests to validate your implementation
4. Compare with solution if stuck

## Common Mistakes to Watch For

- Forgetting to unlock in a finally block
- Using wrong lock granularity (too coarse or too fine)
- Not understanding CAS failure semantics
- Mixing synchronized and Lock on same object

## Next Module

After completing this module, proceed to **module3-executors-async**.
