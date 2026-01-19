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

| Lab | Topic | Key Concepts |
|-----|-------|--------------|
| **lab01** | Race Condition Demo | Shared mutable state, data races |
| **lab02** | Visibility Problem | volatile, memory barriers |
| **lab03** | Wait/Notify Basics | Guarded blocks, spurious wakeups |
| **lab04** | Happens-Before | JMM ordering guarantees |
| **lab22** | ThreadLocal | Thread-isolated state, cleanup |

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
