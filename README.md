# HandsOnConcurrency — Practice-First Java Concurrency for SDE2 Interviews

> **AI-Generated Repository**: This project was created with AI assistance.

Master Java concurrency through **hands-on practice**. This repository provides skeleton code for common interview problems — you implement the solutions.

---

## 📚 Learning Philosophy

This repo follows a **practice-first** approach:

1. **Solve before seeing solutions** — Problem skeletons are separate from canonical solutions
2. **Learn from common mistakes** — Each problem documents typical errors to avoid
3. **Test your implementation** — Run tests to validate your solution
4. **Compare with reference** — Only after solving, check the `solutions/` package

> 💡 **The struggle is where learning happens.** Resist the urge to peek at solutions early!

---

## Prerequisites

- Java 17+
- Maven 3.8+

## Quick Start

```bash
# Compile all modules
mvn compile

# Run all tests
mvn test

# Run a specific lab (example)
cd module1-foundations
mvn exec:java -Dexec.mainClass="com.concurrency.labs.lab01.RaceConditionDemo"
```

---

## 🗂️ Repository Structure

```
HandsOnConcurrency/
├── module1-foundations/        # Race conditions, visibility, wait/notify
├── module2-locks-atomics/      # Locks, atomics, ConcurrentHashMap
├── module3-executors-async/    # Executors, CompletableFuture, Fork/Join
├── module4-liveness-production/# Deadlock, graceful shutdown
├── module5-testing/            # Stress testing, race detection
├── module6-classic-problems/   # Interview classics (Queue, Cache, Pool)
│   ├── problems/               # ← Skeletons (YOU implement these)
│   └── solutions/              # ← Reference implementations
├── module7-capstone/           # Capstone projects
├── module8-lld-concurrency/    # LLD + Concurrency problems
├── module9-debug/              # Debugging exercises
└── module10-evolution/         # Java concurrency evolution
```

Each module has its own `README.md` with learning objectives and problem list.

---

## 🚀 How to Use This Repo

### For Learning (Recommended)

1. **Pick a module** — Start with `module1-foundations` if new to concurrency
2. **Read the module README** — Understand learning objectives
3. **Open a skeleton file** — Read the problem statement and TODO comments
4. **Implement your solution** — Don't look at solutions yet!
5. **Run tests** — Validate your implementation
6. **Compare with solution** — Learn from the reference implementation

### For Interview Prep (Fast Track)

Focus on `module6-classic-problems` — these are the most commonly asked:

| Priority | Problem | Concepts Tested |
|----------|---------|-----------------|
| 1 | Bounded Blocking Queue | wait/notify, state machine |
| 2 | Token Bucket Rate Limiter | Time management, lazy-fill |
| 3 | Thread-Safe LRU Cache | Fine-grained locking, CHM |
| 4 | Custom Thread Pool | Worker pattern, BlockingQueue |
| 5 | Dining Philosophers | Deadlock prevention |

---

## 📖 Module Overview

| Module | Focus | Key Concepts |
|--------|-------|--------------|
| **module1-foundations** | Thread basics | Race conditions, visibility, happens-before, wait/notify |
| **module2-locks-atomics** | Synchronization primitives | ReentrantLock, ReadWriteLock, Atomics, ConcurrentHashMap |
| **module3-executors-async** | Thread management | ExecutorService, CompletableFuture, Fork/Join |
| **module4-liveness-production** | Production concerns | Deadlock detection, graceful shutdown, thread dumps |
| **module5-testing** | Verification | Stress testing, race detection, deterministic testing |
| **module6-classic-problems** | Interview problems | BlockingQueue, RateLimiter, ThreadPool, LRU Cache |
| **module7-capstone** | Integration projects | Pipeline, Orchestrator, Scheduler |
| **module8-lld-concurrency** | LLD + Threads | Booking system, Parking lot, Pub-Sub |
| **module9-debug** | Debugging skills | Finding and fixing concurrency bugs |
| **module10-evolution** | Java history | Virtual threads, structured concurrency |

---

## 🎯 Classic Problems by Tier

### 🔴 Tier 1: Core Locking (MUST MASTER)
- Bounded Blocking Queue
- Custom Reader-Writer Lock
- Dining Philosophers

### 🟠 Tier 2: Thread Coordination
- Print In Order
- Even-Odd Printer
- Cyclic Barrier (MapReduce)

### 🟡 Tier 3: System Components (SDE2 Sweet Spot)
- Thread-Safe LRU Cache
- Token Bucket Rate Limiter
- Custom Thread Pool
- Delayed Task Scheduler
- SQS-like Message Queue

### 🟢 Tier 4: Java Specifics
- Double-Checked Locking Singleton
- Initialization-on-Demand Holder
- Enum Singleton

---

## 🔧 In-Code Comment Guide

All skeleton files use these markers:

| Marker | Meaning |
|--------|---------|
| `// TODO:` | What you need to implement |
| `// ⚠️ COMMON MISTAKES:` | Typical errors to avoid |
| `// 💡 THINK:` | Consider alternative approaches |
| `// ⚠️ AVOID:` | Anti-patterns to reject |
| `// 📝 NOTE:` | Important concepts |

---

## 🏢 LLD + Concurrency Problems

These combine OOP class design with thread-safety — exactly what SDE2 interviews test:

| Problem | Concurrency Challenge | Similar To |
|---------|----------------------|------------|
| **Seat Booking System** | Prevent double-booking (CAS) | BookMyShow, movie tickets |
| **Parking Lot** | Multiple gates, atomic allocation | Classic LLD problem |
| **Pub-Sub System** | Concurrent publishers/subscribers | Mini-Kafka, event systems |

---

## 📚 Recommended Reading

- **"Java Concurrency in Practice"** by Brian Goetz — The definitive reference
- **"The Art of Multiprocessor Programming"** by Herlihy & Shavit — Deep theory
- [Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/) — Official Oracle docs

---

## 🤝 Contributing

Contributions welcome! Please maintain the **practice-first** philosophy:
- Problem skeletons should NOT contain solution logic
- Solutions go in separate `solutions/` package
- Include common mistakes to help learners

---

## 📝 License

MIT License — See `LICENSE` file.
