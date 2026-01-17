# Common Concurrency Bug Patterns Cheatsheet

> Quick reference for debugging rounds (Rubrik, Dropbox)  
> **Time target:** Find 3 bugs in < 10 minutes

---

## 🔴 Critical Bugs (Memorize These)

### 1. `if` Instead of `while` (Spurious Wakeup)

```java
// ❌ BUG: Thread can wake without notify (spurious wakeup)
if (queue.isEmpty()) {
    wait();
}
// After wake, queue might STILL be empty!

// ✅ FIX: Always use while
while (queue.isEmpty()) {
    wait();
}
```

**Why it fails:** JVM can wake threads without `notify()` being called. Also, another consumer might have taken the item first.

---

### 2. `notify()` Instead of `notifyAll()`

```java
// ❌ BUG: May wake wrong thread type
notify();  // What if it wakes a producer when consumer should run?

// ✅ FIX: Wake all, let them re-check
notifyAll();
```

**When `notify()` is OK:** Only when there's exactly one condition AND all waiters are equivalent.

---

### 3. Lock Not in `finally`

```java
// ❌ BUG: Lock never released if exception
lock.lock();
doWork();  // throws exception
lock.unlock();  // Never reached!

// ✅ FIX: Always use try-finally
lock.lock();
try {
    doWork();
} finally {
    lock.unlock();
}
```

---

### 4. Inconsistent Lock Ordering (Deadlock)

```java
// ❌ BUG: Thread 1 locks A→B, Thread 2 locks B→A
void transfer(Account from, Account to) {
    synchronized(from) {
        synchronized(to) {
            // DEADLOCK if called with swapped accounts!
        }
    }
}

// ✅ FIX: Always lock in consistent order (by ID, hash, etc.)
void transfer(Account from, Account to) {
    Account first = from.id < to.id ? from : to;
    Account second = from.id < to.id ? to : from;
    synchronized(first) {
        synchronized(second) {
            // Safe
        }
    }
}
```

---

### 5. Check-Then-Act Race Condition

```java
// ❌ BUG: Key might be removed between check and get
if (map.containsKey(key)) {
    return map.get(key);  // NPE possible!
}

// ✅ FIX: Atomic operation
return map.computeIfAbsent(key, k -> createDefault());

// Or for simple checks:
V value = map.get(key);
if (value != null) {
    return value;
}
```

---

### 6. Missing `volatile`

```java
// ❌ BUG: Other threads may never see update (visibility issue)
private boolean running = true;

// ✅ FIX: volatile ensures visibility across threads
private volatile boolean running = true;
```

**When to use volatile:**
- Simple flags (`boolean running`, `boolean cancelled`)
- NOT for compound operations (`count++` is NOT atomic even with volatile!)

---

### 7. Double-Checked Locking (Without Volatile)

```java
// ❌ BUG: Without volatile, partially constructed object may be visible
private static Singleton instance;

if (instance == null) {
    synchronized(Singleton.class) {
        if (instance == null) {
            instance = new Singleton();  // Broken!
        }
    }
}

// ✅ FIX: Add volatile
private static volatile Singleton instance;

// ✅ BETTER: Use holder idiom (simpler, no synchronization needed)
private static class Holder {
    static final Singleton INSTANCE = new Singleton();
}
public static Singleton getInstance() {
    return Holder.INSTANCE;
}
```

---

## 🟡 Subtle Bugs (Harder to Spot)

### 8. Returning Mutable State

```java
// ❌ BUG: Caller can modify internal list
public List<String> getItems() {
    return items;  // Not thread-safe!
}

// ✅ FIX: Return copy or unmodifiable view
public List<String> getItems() {
    synchronized(this) {
        return new ArrayList<>(items);  // Copy
    }
}
// Or:
return Collections.unmodifiableList(new ArrayList<>(items));
```

---

### 9. Synchronizing on Wrong Object

```java
// ❌ BUG: Each thread locks on a different object
public void process(Account account) {
    synchronized(new Object()) {  // Useless!
        account.update();
    }
}

// ❌ BUG: Locking on mutable field
private String name;
synchronized(name) { ... }  // Breaks if name changes!

// ✅ FIX: Lock on stable object
private final Object lock = new Object();
synchronized(lock) { ... }
```

---

### 10. Thread.sleep() Inside Lock

```java
// ❌ BUG: Holding lock while sleeping
synchronized(this) {
    Thread.sleep(1000);  // Other threads blocked!
}

// ✅ FIX: Use wait() with timeout, or restructure code
synchronized(this) {
    while (!condition) {
        wait(1000);  // Releases lock while waiting
    }
}
```

---

## 📊 Quick Reference Table

| Bug | Symptom | Fix | Frequency |
|-----|---------|-----|-----------|
| `if` vs `while` | Random failures | Use `while` | ⭐⭐⭐⭐⭐ |
| `notify` vs `notifyAll` | Threads stuck | Use `notifyAll()` | ⭐⭐⭐⭐ |
| Missing `finally` | Lock never released | `try { } finally { unlock }` | ⭐⭐⭐⭐ |
| Lock ordering | Deadlock | Lock by consistent order | ⭐⭐⭐ |
| Check-then-act | NPE, wrong values | Atomic operations | ⭐⭐⭐⭐ |
| Missing `volatile` | Infinite loops | Add `volatile` | ⭐⭐⭐ |
| Bad double-check | NPE on singleton | Add `volatile` or use holder | ⭐⭐ |
| Mutable return | Data corruption | Return copy | ⭐⭐ |
| Wrong lock object | No protection | Use `final` lock object | ⭐⭐ |
| Sleep in lock | Bottleneck | Use `wait()` instead | ⭐ |

---

## 🎯 Debugging Interview Strategy

### When Given Broken Code:

1. **First Pass (30 seconds):** Scan for obvious patterns
   - Look for `if (...)  wait()` - should be `while`
   - Look for `notify()` - should likely be `notifyAll()`
   - Look for locks without `try-finally`

2. **Second Pass (1 minute):** Check thread safety
   - Are shared variables protected?
   - Is there check-then-act?
   - Are nested locks in consistent order?

3. **Third Pass (1 minute):** Check visibility
   - Are flags `volatile`?
   - Is mutable state returned directly?

### What to Say:

> "I see three issues here:
> 1. Line 15 uses `if` instead of `while` for the wait condition - this can fail on spurious wakeup
> 2. Line 20 uses `notify()` but there are multiple waiter types, so it should be `notifyAll()`
> 3. The lock on line 8 isn't released in a finally block, so an exception will cause a deadlock"

---

## 📚 Related LeetCode

| # | Problem | Tests |
|---|---------|-------|
| 1114 | Print in Order | wait/notify ordering |
| 1115 | Print FooBar Alternately | Condition signaling |
| 1116 | Print Zero Even Odd | Multiple conditions |
| 1188 | Design Bounded Blocking Queue | All core patterns |

---

*Memorize the top 7 bugs. They cover 90% of debugging interview questions.*
