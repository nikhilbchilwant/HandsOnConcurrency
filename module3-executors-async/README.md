# Module 3: Executors and Async

## Learning Objectives

By completing this module, you will understand:
- ExecutorService lifecycle and thread pools
- CompletableFuture for async programming
- Fork/Join framework for parallel decomposition
- Fan-out/fan-in patterns

## Prerequisites

- Complete **module1-foundations** and **module2-locks-atomics** first

## Labs

| Lab | Topic | Key Concepts |
|-----|-------|--------------|
| **lab11** | ExecutorService | Fixed, cached, scheduled pools |
| **lab12** | CompletableFuture | thenApply, thenCompose, exceptionally |
| **lab13** | Fork/Join | RecursiveTask, work-stealing |
| **lab14** | Parallel Streams | When to use, pitfalls |
| **lab21** | Fan-Out/Fan-In | Scatter-gather pattern |

## How to Practice

1. Read the skeleton file and understand the problem
2. Implement the TODO sections
3. Run tests to validate your implementation
4. Consider edge cases (exceptions, timeouts)

## Common Mistakes to Watch For

- Not shutting down ExecutorService properly
- Blocking in CompletableFuture chains
- Using parallel streams inappropriately
- Ignoring exceptions in async code

## Next Module

After completing this module, proceed to **module4-liveness-production**.
