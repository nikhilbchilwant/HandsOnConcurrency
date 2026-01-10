package com.concurrency.labs.lab21;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Lab 21: Fan-Out / Fan-In Pattern (Scatter-Gather)
 * 
 * 📝 NOTE: This is the "Service Aggregator" pattern - extremely common in interviews!
 *   Example: "Design a travel site that queries 5 airlines and returns cheapest flight"
 * 
 * Pattern:
 *   Fan-Out: Distribute work to N parallel workers
 *   Fan-In: Collect and merge results from all workers
 * 
 * 💡 THINK: Google/Amazon interview question:
 *   "You have 10 product services. Fetch data from all in parallel,
 *    return results within 500ms timeout, gracefully handle failures."
 * 
 * Key concepts:
 *   - Parallel execution with ExecutorService
 *   - Timeout handling per-task and overall
 *   - Partial results (some succeed, some fail)
 *   - Result aggregation
 */
public class FanOutFanIn {
    
    // ========== APPROACH 1: ExecutorService + Futures ==========
    
    /**
     * Fan-out to multiple workers, fan-in results.
     * 
     * @param inputs items to process
     * @param processor function to apply to each item
     * @param executor thread pool to use
     * @param timeoutMs max time to wait for ALL results
     * @return list of successful results (partial if some fail)
     * 
     * 🔑 HINT: This is the core interview pattern!
     */
    public static <I, O> List<O> scatterGather(
            List<I> inputs,
            Function<I, O> processor,
            ExecutorService executor,
            long timeoutMs) {
        
        // Fan-Out: Submit all tasks
        List<Future<O>> futures = inputs.stream()
            .map(input -> executor.submit(() -> processor.apply(input)))
            .collect(Collectors.toList());
        
        // Fan-In: Collect results with timeout
        List<O> results = new ArrayList<>();
        long deadline = System.currentTimeMillis() + timeoutMs;
        
        for (Future<O> future : futures) {
            try {
                long remaining = deadline - System.currentTimeMillis();
                if (remaining <= 0) {
                    // Timeout - cancel remaining futures
                    futures.forEach(f -> f.cancel(true));
                    break;
                }
                O result = future.get(remaining, TimeUnit.MILLISECONDS);
                results.add(result);
            } catch (TimeoutException e) {
                future.cancel(true);
                // Continue to get whatever results we can
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (ExecutionException e) {
                // Log error, continue with partial results
                System.err.println("Task failed: " + e.getCause().getMessage());
            }
        }
        
        return results;
    }
    
    // ========== APPROACH 2: CompletableFuture (Modern Java) ==========
    
    /**
     * Same pattern using CompletableFuture - cleaner API.
     * 
     * 💡 THINK: Interview tip - mention both approaches!
     *   "I could use Futures for fine-grained control, or CompletableFuture
     *    for cleaner composition. Let me show the CF approach..."
     */
    public static <I, O> CompletableFuture<List<O>> scatterGatherAsync(
            List<I> inputs,
            Function<I, O> processor,
            Executor executor) {
        
        // Fan-Out: Create all async tasks
        List<CompletableFuture<O>> futures = inputs.stream()
            .map(input -> CompletableFuture.supplyAsync(
                () -> processor.apply(input), executor))
            .collect(Collectors.toList());
        
        // Fan-In: Wait for all and collect
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }
    
    /**
     * With timeout and partial results.
     * 
     * ⚠️ AVOID: allOf() fails entirely if ANY task fails.
     *   For partial results, handle each future individually.
     */
    public static <I, O> List<O> scatterGatherWithTimeout(
            List<I> inputs,
            Function<I, O> processor,
            Executor executor,
            long timeoutMs) {
        
        List<CompletableFuture<O>> futures = inputs.stream()
            .map(input -> CompletableFuture
                .supplyAsync(() -> processor.apply(input), executor)
                .orTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> null))  // Return null on failure
            .collect(Collectors.toList());
        
        // Wait for all (with overall timeout)
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            // Timeout or interruption - collect whatever completed
        }
        
        // Collect non-null results
        return futures.stream()
            .filter(f -> f.isDone() && !f.isCompletedExceptionally())
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    // ========== REAL INTERVIEW EXAMPLE: Price Aggregator ==========
    
    /**
     * Interview scenario: Query multiple price sources in parallel.
     */
    interface PriceSource {
        double getPrice(String productId) throws Exception;
        String getName();
    }
    
    /**
     * TODO: Implement a price aggregator.
     * 
     * Requirements:
     *   1. Query all sources in parallel
     *   2. Return results within timeout
     *   3. Handle source failures gracefully
     *   4. Return best (lowest) price found
     */
    public static OptionalDouble findBestPrice(
            String productId,
            List<PriceSource> sources,
            ExecutorService executor,
            long timeoutMs) {
        
        // Fan-Out: Query all sources
        List<Future<Double>> futures = sources.stream()
            .map(source -> executor.submit(() -> {
                System.out.println("Querying " + source.getName() + "...");
                return source.getPrice(productId);
            }))
            .collect(Collectors.toList());
        
        // Fan-In: Collect prices
        List<Double> prices = new ArrayList<>();
        long deadline = System.currentTimeMillis() + timeoutMs;
        
        for (int i = 0; i < futures.size(); i++) {
            Future<Double> future = futures.get(i);
            String sourceName = sources.get(i).getName();
            
            try {
                long remaining = Math.max(0, deadline - System.currentTimeMillis());
                Double price = future.get(remaining, TimeUnit.MILLISECONDS);
                System.out.println(sourceName + " returned: $" + price);
                prices.add(price);
            } catch (TimeoutException e) {
                System.out.println(sourceName + " timed out");
                future.cancel(true);
            } catch (Exception e) {
                System.out.println(sourceName + " failed: " + e.getMessage());
            }
        }
        
        // Return best price
        return prices.stream().mapToDouble(d -> d).min();
    }
    
    // ========== DEMO ==========
    
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // Demo 1: Simple scatter-gather
        System.out.println("=== Simple Scatter-Gather ===");
        List<Integer> inputs = List.of(1, 2, 3, 4, 5);
        List<Integer> results = scatterGather(
            inputs,
            n -> {
                sleep(100 * n);  // Simulate varying work
                return n * n;
            },
            executor,
            1000
        );
        System.out.println("Squares: " + results);
        
        // Demo 2: Price aggregator with failures
        System.out.println("\n=== Price Aggregator ===");
        List<PriceSource> sources = List.of(
            new MockPriceSource("Amazon", 99.99, 100),
            new MockPriceSource("eBay", 89.99, 200),
            new MockPriceSource("SlowStore", 79.99, 2000),  // Will timeout
            new MockPriceSource("FailStore", -1, 50)        // Will throw
        );
        
        OptionalDouble best = findBestPrice("PRODUCT-123", sources, executor, 500);
        
        if (best.isPresent()) {
            System.out.println("\nBest price: $" + best.getAsDouble());
        } else {
            System.out.println("\nNo prices found!");
        }
        
        executor.shutdown();
    }
    
    static class MockPriceSource implements PriceSource {
        private final String name;
        private final double price;
        private final long delayMs;
        
        MockPriceSource(String name, double price, long delayMs) {
            this.name = name;
            this.price = price;
            this.delayMs = delayMs;
        }
        
        @Override
        public double getPrice(String productId) throws Exception {
            sleep(delayMs);
            if (price < 0) throw new RuntimeException("Service unavailable");
            return price;
        }
        
        @Override
        public String getName() { return name; }
    }
    
    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
