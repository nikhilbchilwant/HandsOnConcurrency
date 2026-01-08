package com.concurrency.lld.pubsub;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * LLD Problem: Pub-Sub Message Queue (Mini-Kafka)
 * 
 * Scenario: Event-driven system with topics, publishers, and subscribers.
 * 
 * Requirements:
 *   1. Create topics
 *   2. Subscribe handlers to topics
 *   3. Publish messages (delivered to all subscribers)
 *   4. Concurrent publishers and subscribers
 * 
 * 📝 NOTE: This tests producer-consumer at scale.
 *   The key challenge: Efficient delivery to many subscribers.
 * 
 * 💡 THINK: Should subscribers block the publisher?
 *   No! Publisher should return immediately.
 *   Delivery should be async via thread pool.
 * 
 * ⚠️ AVOID: Slow subscriber blocking fast publisher.
 *   Use per-subscriber queues or async delivery.
 */
public class PubSubSystem {
    
    /**
     * A message in the system.
     */
    public static class Message {
        private final String topic;
        private final String content;
        private final long timestamp;
        
        public Message(String topic, String content) {
            this.topic = topic;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getTopic() { return topic; }
        public String getContent() { return content; }
        public long getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return String.format("[%s] %s", topic, content);
        }
    }
    
    /**
     * A topic with its subscribers.
     * 
     * 📝 NOTE: Using CopyOnWriteArraySet for subscribers.
     *   - Thread-safe for iteration during publish
     *   - Subscribe/unsubscribe is rare, publish is frequent
     */
    public static class Topic {
        private final String name;
        private final Set<Consumer<Message>> subscribers;
        private final ExecutorService deliveryPool;
        
        public Topic(String name, int deliveryThreads) {
            this.name = name;
            this.subscribers = new CopyOnWriteArraySet<>();
            this.deliveryPool = Executors.newFixedThreadPool(deliveryThreads);
        }
        
        /**
         * TODO: Subscribe a handler to this topic.
         * 
         * 🔑 HINT: CopyOnWriteArraySet handles thread-safety.
         */
        public void subscribe(Consumer<Message> handler) {
            subscribers.add(handler);
        }
        
        public void unsubscribe(Consumer<Message> handler) {
            subscribers.remove(handler);
        }
        
        /**
         * TODO: Publish a message to all subscribers asynchronously.
         * 
         * 💡 THINK: Why async?
         *   If subscriber A takes 10 seconds to process,
         *   should subscriber B wait? No!
         * 
         * 🔑 HINT: Submit each delivery as a task to the thread pool.
         */
        public void publish(Message message) {
            for (Consumer<Message> subscriber : subscribers) {
                // TODO: Deliver asynchronously
                // Each subscriber gets its own delivery task
                deliveryPool.submit(() -> {
                    try {
                        subscriber.accept(message);
                    } catch (Exception e) {
                        // Log but don't crash - one bad subscriber shouldn't affect others
                        System.err.println("Subscriber error: " + e.getMessage());
                    }
                });
            }
        }
        
        public int getSubscriberCount() {
            return subscribers.size();
        }
        
        public void shutdown() {
            deliveryPool.shutdown();
        }
    }
    
    // Topics registry
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();
    private final int deliveryThreadsPerTopic;
    
    public PubSubSystem(int deliveryThreadsPerTopic) {
        this.deliveryThreadsPerTopic = deliveryThreadsPerTopic;
    }
    
    /**
     * Create a topic.
     */
    public void createTopic(String topicName) {
        topics.computeIfAbsent(topicName, 
            name -> new Topic(name, deliveryThreadsPerTopic));
    }
    
    /**
     * Subscribe to a topic.
     * 
     * @param topicName the topic to subscribe to
     * @param handler callback when message is received
     */
    public void subscribe(String topicName, Consumer<Message> handler) {
        Topic topic = topics.get(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("Topic not found: " + topicName);
        }
        topic.subscribe(handler);
    }
    
    /**
     * Publish a message to a topic.
     * 
     * 📝 NOTE: This returns immediately - delivery is async!
     */
    public void publish(String topicName, String content) {
        Topic topic = topics.get(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("Topic not found: " + topicName);
        }
        topic.publish(new Message(topicName, content));
    }
    
    public void shutdown() {
        for (Topic topic : topics.values()) {
            topic.shutdown();
        }
    }
    
    /**
     * Demo: Multiple publishers and subscribers on same topic.
     */
    public static void main(String[] args) throws InterruptedException {
        PubSubSystem pubsub = new PubSubSystem(4);
        
        // Create topics
        pubsub.createTopic("orders");
        pubsub.createTopic("notifications");
        
        // Subscribe multiple handlers to "orders"
        pubsub.subscribe("orders", msg -> {
            System.out.println("[Inventory] Processing: " + msg.getContent());
            sleep(100);  // Simulate work
        });
        
        pubsub.subscribe("orders", msg -> {
            System.out.println("[Analytics] Recording: " + msg.getContent());
            sleep(50);
        });
        
        pubsub.subscribe("orders", msg -> {
            System.out.println("[Notification] Alerting: " + msg.getContent());
        });
        
        // Multiple publishers sending concurrently
        Thread[] publishers = new Thread[5];
        for (int i = 0; i < 5; i++) {
            final int orderId = i;
            publishers[i] = new Thread(() -> {
                pubsub.publish("orders", "Order-" + orderId + " placed");
                System.out.println("Published Order-" + orderId);
            });
        }
        
        for (Thread t : publishers) t.start();
        for (Thread t : publishers) t.join();
        
        // Wait for async delivery
        Thread.sleep(500);
        
        pubsub.shutdown();
        System.out.println("\nAll messages delivered!");
        
        // 💡 THINK: Notice how publishers return immediately
        // but subscribers process at their own pace.
    }
    
    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
