# HandsOnConcurrency: Improvement Notes for SDE2/SWE3 Level

> **Generated**: 2026-01-21  
> **Based on**: Analysis of `interview_experiences.txt`, `other_experiences.txt`, `github_system_design_questions.txt`

---

## Key Concurrency Questions from Real Interviews

| Company | Role | Problem/Concept | Line Reference |
|---------|------|-----------------|----------------|
| Microsoft | SDE2 | Implementing a thread pool | `interview_experiences.txt:808` |
| Alkira | Distinguished Engineer | Lockless circular queue, CAS, memory ordering | `other_experiences.txt:539` |
| JPMorgan | SDE3 | Java concurrency control (deep dive) | `interview_experiences.txt:1043` |
| project44 | SDE | Concurrency handling in movie ticket booking | `interview_experiences.txt:1300` |
| Walmart | SDE3 | Database concurrency, locking mechanisms, isolation levels | `interview_experiences.txt:1348` |
| Adobe | Computer Scientist | Java concurrency, object copying | `interview_experiences.txt:1421` |
| Razorpay | Lead | Load balancer implementation (120 min machine coding) | `interview_experiences.txt:977` |

---

## Gaps to Fill (Priority Order)

### 🔴 HIGH PRIORITY

1. **Lock-Free Data Structures**
   - Lock-free stack (AtomicReference + CAS)
   - Lock-free queue (Michael-Scott)
   - Lockless circular buffer ← directly asked at Alkira
   - **Interview Evidence**: Alkira Distinguished Engineer round

2. **Database Concurrency Concepts**
   - Optimistic locking (versioned updates)
   - Pessimistic locking (explicit locks)
   - Transaction isolation levels demo
   - **Interview Evidence**: Walmart SDE3

### 🟠 MEDIUM PRIORITY

3. **Load Balancer Implementation**
   - Round-robin with health checks
   - Machine coding style (120 min)
   - **Interview Evidence**: Razorpay Lead

4. **Deep Java Concurrency**
   - HashMap vs ConcurrentHashMap internals
   - Deep copy with thread safety
   - **Interview Evidence**: JPMorgan SDE3, Adobe

### 🟡 LOW PRIORITY (Deprioritize)

5. **Singleton Patterns (Tier 4)**
   - Rarely asked at SDE2+ level
   - More relevant for SDE1 interviews
   - Keep for completeness but don't emphasize

---

## Suggested New Problems

```
module6-classic-problems/
├── tier3/
│   ├── LoadBalancer.java              ← NEW (Razorpay)
│   └── DistributedJobScheduler.java   ← NEW
├── tier3b-lockfree/                   ← NEW TIER
│   ├── LockFreeStack.java
│   ├── LockFreeQueue.java
│   └── LockFreeCircularBuffer.java
└── tier3c-database-concurrency/       ← NEW TIER
    ├── OptimisticLocking.java
    ├── PessimisticLocking.java
    └── TransactionIsolationDemo.java
```

---

## Current Coverage Status

| Topic | Status | Notes |
|-------|--------|-------|
| Thread Pool | ✅ Covered | `SimpleThreadPool.java` |
| Rate Limiter | ✅ Covered | `TokenBucketRateLimiter.java` |
| Blocking Queue | ✅ Covered | `BoundedBlockingQueue.java` |
| LRU Cache | ✅ Covered | `ConcurrentLRUCache.java` |
| Lock-free structures | ❌ Missing | High priority gap |
| Database concurrency | ❌ Missing | High priority gap |
| Load Balancer | ❌ Missing | Medium priority |
| Singleton patterns | ✅ Covered | Consider deprioritizing |

---

## Action Items

- [ ] Add lock-free data structures (Tier 3B)
- [ ] Add database concurrency problems (Tier 3C)
- [ ] Add load balancer machine coding problem
- [ ] Update README to reflect SDE2/SWE3 focus
- [ ] Consider moving Singletons out of main interview prep path
