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

| Lab | Topic | Key Concepts |
|-----|-------|--------------|
| **lab05** | ReentrantLock | Lock/unlock, tryLock, fairness |
| **lab06** | ReadWriteLock | Read vs write locks, lock upgrading |
| **lab07** | Atomic Classes | CAS operations, AtomicInteger |
| **lab08** | Counter Comparison | Synchronized vs Lock vs Atomic |
| **lab09** | ConcurrentHashMap | Thread-safe map, compute methods |
| **lab10** | BlockingQueue | Producer-consumer pattern |

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
