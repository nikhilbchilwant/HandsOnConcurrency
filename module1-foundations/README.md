# Module 1: Foundations

## Learning Objectives

By completing this module, you will understand:
- How race conditions occur and why they're dangerous
- Memory visibility issues between threads
- The happens-before relationship in Java Memory Model
- How to use `wait()` and `notify()` for thread coordination
- ThreadLocal for thread-isolated state

## Prerequisites

- Basic Java knowledge
- Understanding of what threads are

## Labs

| Lab | Topic | Key Concepts | JCiP Reference |
|-----|-------|--------------|----------------|
| **lab01** | Race Condition Demo | Shared mutable state, data races | Ch 2.2 (Race Conditions) |
| **lab02** | Visibility Problem | volatile, memory barriers | Ch 3.1 (Visibility) |
| **lab03** | Wait/Notify Basics | Guarded blocks, spurious wakeups | Ch 14.2 (Condition Queues) |
| **lab04** | Happens-Before | JMM ordering guarantees | Ch 16 (Java Memory Model) |
| **lab06** | Record Delegation | Immutability, thread-safety delegation | Ch 3.4 (Immutability), Ch 4.3 (Delegation) |
| **lab07** | Sealed States | Concurrent state machines | Ch 4.1 (Confinement) |
| **lab22** | ThreadLocal | Thread-isolated state, cleanup | Ch 3.3 (Thread Confinement) |

## How to Practice

1. Read the skeleton file and understand the problem
2. Implement the TODO sections
3. Run the main method to observe behavior
4. Compare with expected output

## Common Mistakes to Watch For

- Using `if` instead of `while` with `wait()`
- Forgetting to call `notifyAll()` after state changes
- Not handling `InterruptedException` properly
- Assuming visibility without synchronization

## Next Module

After completing this module, proceed to **module2-locks-atomics**.
