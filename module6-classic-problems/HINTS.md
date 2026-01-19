# Progressive Hints Guide

> ⚠️ **STOP!** Before looking at hints, try solving for at least **20 minutes** on your own.
> The struggle is where learning happens!

---

## How to Use This Guide

1. **Try first** - Spend 20+ minutes attempting the problem
2. **Level 1 Hint** - Conceptual direction (no code)
3. **Level 2 Hint** - Pattern/approach guidance
4. **Level 3 Hint** - Pseudocode (last resort before solution)
5. **Solution** - Located in `com.concurrency.solutions` package

---

## Tier 1: Core Locking (MUST MASTER)

### BoundedBlockingQueue

<details>
<summary>🔵 Level 1: Conceptual</summary>

Think about:
- What state needs to be protected?
- When should a producer wait? When should a consumer wait?
- How do you wake up waiting threads?

</details>

<details>
<summary>🟡 Level 2: Pattern</summary>

Use the **guarded blocks** pattern:
- `synchronized` for mutual exclusion
- `wait()` to release lock and sleep
- `notifyAll()` to wake waiters
- **CRITICAL**: Use `while` loop, not `if`!

</details>

<details>
<summary>🔴 Level 3: Pseudocode</summary>

```
put(item):
    synchronized(this):
        while (queue is full):
            wait()
        add item at tail
        tail = (tail + 1) % capacity
        count++
        notifyAll()

take():
    synchronized(this):
        while (queue is empty):
            wait()
        item = get from head
        head = (head + 1) % capacity
        count--
        notifyAll()
        return item
```

</details>

---

### SimpleReadWriteLock

<details>
<summary>🔵 Level 1: Conceptual</summary>

Think about:
- Multiple readers can read simultaneously
- Writers need exclusive access
- How do you track readers vs writers?

</details>

<details>
<summary>🟡 Level 2: Pattern</summary>

Track state with counters:
- `readers` - count of active readers
- `writers` - count of active writers (0 or 1)
- `writeRequests` - pending write requests (for fairness)

</details>

<details>
<summary>🔴 Level 3: Pseudocode</summary>

```
lockRead():
    synchronized(this):
        while (writers > 0 || writeRequests > 0):
            wait()
        readers++

unlockRead():
    synchronized(this):
        readers--
        if (readers == 0):
            notifyAll()

lockWrite():
    synchronized(this):
        writeRequests++
        while (readers > 0 || writers > 0):
            wait()
        writeRequests--
        writers++

unlockWrite():
    synchronized(this):
        writers--
        notifyAll()
```

</details>

---

### DiningPhilosophers

<details>
<summary>🔵 Level 1: Conceptual</summary>

Think about:
- What causes deadlock? (4 conditions)
- How can you break the circular wait?

</details>

<details>
<summary>🟡 Level 2: Pattern</summary>

**Resource Hierarchy Solution**:
- Number the forks 0 to N-1
- Always pick up the lower-numbered fork first
- This breaks circular wait!

</details>

<details>
<summary>🔴 Level 3: Pseudocode</summary>

```
eat(philosopherId):
    leftFork = philosopherId
    rightFork = (philosopherId + 1) % N
    
    // Always acquire lower-numbered fork first
    firstFork = min(leftFork, rightFork)
    secondFork = max(leftFork, rightFork)
    
    synchronized(forks[firstFork]):
        synchronized(forks[secondFork]):
            // Eat!
```

</details>

---

## Tier 2: Thread Coordination

### EvenOddPrinter

<details>
<summary>🔵 Level 1: Conceptual</summary>

Think about:
- How do two threads take turns?
- What's the shared "turn" indicator?

</details>

<details>
<summary>🟡 Level 2: Pattern</summary>

Use a **turn flag**:
- `isOddTurn = true` initially
- Odd thread waits while `!isOddTurn`
- Even thread waits while `isOddTurn`

</details>

<details>
<summary>🔴 Level 3: Pseudocode</summary>

```
printOdd():
    synchronized(this):
        while (current <= max):
            while (!isOddTurn && current <= max):
                wait()
            if (current <= max):
                print(current)
                current++
                isOddTurn = false
                notifyAll()

printEven():
    // Mirror of printOdd with isOddTurn flipped
```

</details>

---

## Tier 3: System Components (SDE2 Sweet Spot)

### TokenBucketRateLimiter

<details>
<summary>🔵 Level 1: Conceptual</summary>

Think about:
- Don't use a background refill thread!
- Calculate tokens "lazily" when requested

</details>

<details>
<summary>🟡 Level 2: Pattern</summary>

**Lazy Refill**:
- Store `lastRefillTimestamp`
- On each request, calculate elapsed time
- Add tokens based on `elapsed * refillRate`
- Cap at `capacity`

</details>

<details>
<summary>🔴 Level 3: Pseudocode</summary>

```
tryAcquire():
    synchronized(this):
        now = currentTimeMillis()
        elapsed = now - lastRefillTimestamp
        tokensToAdd = elapsed * refillRatePerMs
        availableTokens = min(capacity, availableTokens + tokensToAdd)
        lastRefillTimestamp = now
        
        if (availableTokens >= 1):
            availableTokens--
            return true
        return false
```

</details>

---

### SimpleThreadPool

<details>
<summary>🔵 Level 1: Conceptual</summary>

Think about:
- Workers are threads that loop forever
- Tasks go into a BlockingQueue
- Workers `take()` from queue (blocks when empty)

</details>

<details>
<summary>🟡 Level 2: Pattern</summary>

**Worker Loop**:
```
while (!shutdown):
    task = queue.take()  // Blocks if empty
    task.run()           // Execute in worker thread
```

**Shutdown**:
- Set flag, interrupt all workers
- Workers check flag after interrupt

</details>

<details>
<summary>🔴 Level 3: Pseudocode</summary>

```
Worker.run():
    while (!isShutdown):
        try:
            task = taskQueue.take()
            task.run()
        catch InterruptedException:
            // Shutdown signal, exit loop

shutdown():
    isShutdown = true
    for worker in workers:
        worker.interrupt()
```

</details>

---

### DelayedTaskScheduler

<details>
<summary>🔵 Level 1: Conceptual</summary>

Think about:
- Use a PriorityQueue ordered by execution time
- How to wait until the next task is due?

</details>

<details>
<summary>🟡 Level 2: Pattern</summary>

**Scheduler Loop**:
- Peek at earliest task
- Calculate delay until it's due
- `wait(delay)` or use `Condition.awaitNanos()`
- If still earliest when waking, execute it

</details>

<details>
<summary>🔴 Level 3: Pseudocode</summary>

```
run():
    while (!shutdown):
        synchronized(lock):
            while (queue.isEmpty()):
                wait()
            
            task = queue.peek()
            delay = task.scheduledTime - currentTime
            
            if (delay > 0):
                wait(delay)  // Re-check after wake
            else:
                queue.poll()
                // Execute task OUTSIDE lock!
        
        task.run()  // Don't hold lock during execution!
```

</details>

---

### ConcurrentMessageQueue (SQS-like)

<details>
<summary>🔵 Level 1: Conceptual</summary>

Think about:
- Messages aren't removed on receive - just hidden
- Need visibility timeout and receipt handles
- `acknowledge()` actually removes the message

</details>

<details>
<summary>🟡 Level 2: Pattern</summary>

**Visibility Tracking**:
- Each message has `visibleAt` timestamp
- `receive()` sets `visibleAt = now + timeout`
- `acknowledge()` removes message if receipt handle matches

</details>

<details>
<summary>🔴 Level 3: Pseudocode</summary>

```
receive():
    synchronized(this):
        for msg in queue:
            if (msg.visibleAt <= now):
                msg.visibleAt = now + visibilityTimeout
                msg.receiptHandle = UUID.randomUUID()
                msg.receiveCount++
                return msg
        return null

acknowledge(receiptHandle):
    synchronized(this):
        msg = findByReceiptHandle(receiptHandle)
        if (msg != null && msg.receiptHandle == receiptHandle):
            queue.remove(msg)
            return true
        return false
```

</details>

---

## 🎯 Interview Tips

1. **Start simple** - Implement with `synchronized` first, then mention Lock+Condition as improvement
2. **Explain trade-offs** - "I used notifyAll() for safety, but in production I'd use separate Conditions"
3. **Know the stdlib** - "I know ArrayBlockingQueue exists, but I'm showing I understand the internals"
