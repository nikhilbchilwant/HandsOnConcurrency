# Module 9: Debugging Practice

> 🔴 **Rubrik & Dropbox have dedicated debugging rounds!**  
> Practice finding bugs in concurrent code without running it.

## How to Use This Module

1. **Open a broken file** (e.g., `BrokenBlockingQueue.java`)
2. **Read the code carefully** - DO NOT run it yet
3. **Find all the bugs** - Write them down
4. **Check your answers** against the bug list in the README
5. **Fix the bugs** and verify with tests

## Time Target

| File | Expected Bugs | Time to Find All |
|------|---------------|------------------|
| BrokenBlockingQueue.java | 3 | < 5 minutes |
| BrokenRateLimiter.java | 2 | < 5 minutes |
| DeadlockBanking.java | 2 | < 5 minutes |
| MissingVolatile.java | 1 | < 3 minutes |
| NotifyVsNotifyAll.java | 1 | < 3 minutes |

## Bug Patterns Covered

1. `if` instead of `while` for wait conditions
2. `notify()` instead of `notifyAll()`
3. Lock not released in `finally`
4. Inconsistent lock ordering (deadlock)
5. Check-then-act race condition
6. Missing `volatile`
