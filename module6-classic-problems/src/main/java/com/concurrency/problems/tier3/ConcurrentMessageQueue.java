package com.concurrency.problems.tier3;

import java.util.concurrent.TimeUnit;

/**
 * Classic Problem: SQS-like Message Queue with Competing Consumers
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ ✅ INTERVIEW RELEVANCE: HIGH PRIORITY                                  │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ Companies: Amazon, Uber, Stripe, any company using message queues      │
 * │ Frequency: HIGH - Tests understanding of distributed systems patterns  │
 * │ Time Target: Implement from scratch in < 35 minutes                    │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ 🎤 INTERVIEW FOLLOW-UP QUESTIONS (Be ready for these!)                │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │                                                                        │
 * │ Q1: "How is this different from a simple BlockingQueue?"              │
 * │ → BlockingQueue: take() removes message immediately                   │
 * │ → SQS-style: receive() makes message INVISIBLE, not removed           │
 * │ → Message only deleted after explicit acknowledge()                    │
 * │ → INSIGHT: Allows retry if consumer crashes before processing         │
 * │                                                                        │
 * │ Q2: "What happens if consumer crashes before acknowledging?"          │
 * │ → Message becomes visible again after visibility timeout              │
 * │ → Another consumer can then receive and process it                    │
 * │ → GUARANTEES: At-least-once delivery (may see duplicates!)            │
 * │                                                                        │
 * │ Q3: "How would you implement exactly-once processing?"                │
 * │ → You CAN'T at the queue level! Consumer must handle it              │
 * │ → SOLUTION: Idempotency - processing same message twice = same result │
 * │ → PATTERN: Store message ID in DB before processing, check on receive │
 * │                                                                        │
 * │ Q4: "How does SQS handle message ordering?"                           │
 * │ → Standard SQS: NO ordering guarantee (best-effort)                   │
 * │ → FIFO SQS: Ordered within message group, dedup within 5 min         │
 * │ → TRADE-OFF: FIFO has lower throughput (~300 msg/sec vs unlimited)   │
 * │                                                                        │
 * │ Q5: "What's a Dead Letter Queue (DLQ)?"                               │
 * │ → Messages that fail processing N times go to DLQ                     │
 * │ → Prevents poison messages from blocking the queue                    │
 * │ → IMPLEMENTATION: Track receive count, move to DLQ when > maxReceives │
 * │                                                                        │
 * │ Q6: "How would you scale this to multiple machines?"                  │
 * │ → Need distributed lock or atomic DB operations                       │
 * │ → Redis: BRPOPLPUSH for atomic move between queues                   │
 * │ → DynamoDB: Conditional writes for visibility timeout                 │
 * │                                                                        │
 * │ Q7: "Long polling vs short polling?"                                  │
 * │ → Short: Return immediately (empty or with messages)                  │
 * │ → Long: Wait up to N seconds for messages to arrive                   │
 * │ → BENEFIT: Long polling reduces empty responses and API calls         │
 * └─────────────────────────────────────────────────────────────────────────┘
 * 
 * PROBLEM:
 *   Implement an in-memory message queue similar to AWS SQS:
 *   - Multiple producers can send messages
 *   - Multiple consumers COMPETE for messages (each message goes to ONE consumer)
 *   - Messages must be acknowledged after processing
 *   - Unacknowledged messages become visible again after timeout
 * 
 * KEY DIFFERENCE from BlockingQueue:
 *   BlockingQueue.take()  → Message is REMOVED immediately
 *   SQS.receive()         → Message is HIDDEN but not removed
 *   SQS.acknowledge()     → Message is REMOVED after processing
 * 
 * This enables AT-LEAST-ONCE delivery: if consumer crashes, message retries!
 * 
 * @param <T> message type
 */
public class ConcurrentMessageQueue<T> {
    
    /**
     * A message wrapper with metadata for visibility tracking.
     */
    public static class Message<T> {
        private final String messageId;
        private final T body;
        private final long enqueuedAt;
        
        // Visibility tracking
        private long visibleAt;        // When message becomes visible again
        private int receiveCount;      // How many times message was received
        private String receiptHandle;  // Token for acknowledging this receipt
        
        public Message(String messageId, T body) {
            this.messageId = messageId;
            this.body = body;
            this.enqueuedAt = System.currentTimeMillis();
            this.visibleAt = System.currentTimeMillis(); // Visible immediately
            this.receiveCount = 0;
        }
        
        public String getMessageId() { return messageId; }
        public T getBody() { return body; }
        public String getReceiptHandle() { return receiptHandle; }
        public int getReceiveCount() { return receiveCount; }
        
        // TODO: Add visibility management methods
    }
    
    private final int visibilityTimeoutMs;  // How long message stays invisible
    private final int maxReceiveCount;       // Max receives before DLQ
    
    // TODO: Add data structures for:
    // - Main queue of messages
    // - Map of message ID → Message for quick lookup
    // - Track in-flight (invisible) messages
    
    /**
     * Creates a message queue with specified visibility timeout.
     * 
     * @param visibilityTimeoutMs how long a received message stays invisible
     * @param maxReceiveCount max times a message can be received before DLQ
     */
    public ConcurrentMessageQueue(int visibilityTimeoutMs, int maxReceiveCount) {
        this.visibilityTimeoutMs = visibilityTimeoutMs;
        this.maxReceiveCount = maxReceiveCount;
        // TODO: Initialize data structures
    }
    
    /**
     * TODO: Send a message to the queue.
     * 
     * 🔑 HINT:
     *   - Generate unique message ID (UUID)
     *   - Wrap body in Message object
     *   - Add to queue (thread-safe!)
     * 
     * @param body the message content
     * @return the message ID
     */
    public String send(T body) {
        // TODO: Implement send
        // Step 1: Generate UUID for message ID
        // Step 2: Create Message wrapper
        // Step 3: Add to queue (synchronized or use concurrent collection)
        // Step 4: Return message ID
        return null;
    }
    
    /**
     * TODO: Receive a message from the queue.
     * 
     * 🔑 HINT:
     *   - Find a VISIBLE message (visibleAt <= now)
     *   - Mark it INVISIBLE (visibleAt = now + visibilityTimeout)
     *   - Generate receipt handle for this receive
     *   - Increment receive count
     *   - Return the message (or null if queue empty)
     * 
     * 📝 NOTE: Message is NOT removed! Just hidden temporarily.
     * 
     * @return a message, or null if no visible messages
     */
    public Message<T> receive() {
        // TODO: Implement receive
        // Step 1: Lock the queue
        // Step 2: Find first message where visibleAt <= now
        // Step 3: Update visibleAt = now + visibilityTimeoutMs
        // Step 4: Generate unique receipt handle (UUID)
        // Step 5: Increment receiveCount
        // Step 6: If receiveCount > maxReceiveCount, move to DLQ
        // Step 7: Return message (or null)
        return null;
    }
    
    /**
     * TODO: Receive with long polling (wait if queue is empty).
     * 
     * 💡 THINK: Use wait/notify or Condition to avoid busy waiting!
     * 
     * @param timeout max time to wait
     * @param unit time unit
     * @return a message, or null if timeout expires
     */
    public Message<T> receive(long timeout, TimeUnit unit) throws InterruptedException {
        // TODO: Implement long polling receive
        // Step 1: Try receive()
        // Step 2: If null, wait until timeout or notify
        // Step 3: Retry receive() after wake
        return null;
    }
    
    /**
     * TODO: Acknowledge successful processing of a message.
     * 
     * 🔑 HINT:
     *   - Validate receipt handle matches current receipt
     *   - Remove message from queue permanently
     *   - Return true if successful
     * 
     * ⚠️ AVOID: Accepting stale receipt handles!
     *   If message was re-received by another consumer, old handle is invalid.
     * 
     * @param receiptHandle the handle from receive()
     * @return true if acknowledged, false if handle invalid/expired
     */
    public boolean acknowledge(String receiptHandle) {
        // TODO: Implement acknowledge
        // Step 1: Find message by receipt handle
        // Step 2: Verify handle is still valid (not re-received)
        // Step 3: Remove message from queue
        // Step 4: Return success/failure
        return false;
    }
    
    /**
     * TODO: Extend visibility timeout (consumer needs more time).
     * 
     * @param receiptHandle the handle from receive()
     * @param additionalTimeMs additional time in milliseconds
     * @return true if extended, false if handle invalid
     */
    public boolean extendVisibility(String receiptHandle, int additionalTimeMs) {
        // TODO: Implement visibility extension
        return false;
    }
    
    /**
     * Returns approximate number of visible messages.
     */
    public int getApproximateMessageCount() {
        // TODO: Count messages where visibleAt <= now
        return 0;
    }
    
    /**
     * Returns number of messages currently being processed (invisible).
     */
    public int getInFlightCount() {
        // TODO: Count messages where visibleAt > now
        return 0;
    }
    
    // ==================== Demo ====================
    
    public static void main(String[] args) throws InterruptedException {
        ConcurrentMessageQueue<String> queue = new ConcurrentMessageQueue<>(5000, 3);
        
        // Producer
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                String msgId = queue.send("Task-" + i);
                System.out.println("Sent: Task-" + i + " (ID: " + msgId + ")");
            }
        }, "Producer");
        
        // Consumer 1
        Thread consumer1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                Message<String> msg = queue.receive();
                if (msg != null) {
                    System.out.println("Consumer1 received: " + msg.getBody());
                    // Simulate processing
                    try { Thread.sleep(100); } catch (InterruptedException e) { break; }
                    queue.acknowledge(msg.getReceiptHandle());
                    System.out.println("Consumer1 acknowledged: " + msg.getBody());
                }
            }
        }, "Consumer1");
        
        // Consumer 2 (competing with Consumer 1)
        Thread consumer2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                Message<String> msg = queue.receive();
                if (msg != null) {
                    System.out.println("Consumer2 received: " + msg.getBody());
                    try { Thread.sleep(100); } catch (InterruptedException e) { break; }
                    queue.acknowledge(msg.getReceiptHandle());
                    System.out.println("Consumer2 acknowledged: " + msg.getBody());
                }
            }
        }, "Consumer2");
        
        producer.start();
        producer.join();
        
        consumer1.start();
        consumer2.start();
        consumer1.join();
        consumer2.join();
        
        System.out.println("Remaining messages: " + queue.getApproximateMessageCount());
    }
}
