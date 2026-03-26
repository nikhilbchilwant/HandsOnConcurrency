package com.concurrency.solutions.lab07;

import com.concurrency.annotations.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;

/**
 * SOLUTION: Sealed States
 * 
 * Demonstrates using Java 17 Sealed Classes for safe and predictable state management.
 */
public class SealedStateSolution {

    public sealed interface TaskState permits New, Running, Completed, Failed {}

    public record New() implements TaskState {}
    public record Running(long startTime) implements TaskState {}
    public record Completed(Object result, long duration) implements TaskState {}
    public record Failed(Throwable error) implements TaskState {}

    @ThreadSafe
    public static class TaskStateMachine {
        private final AtomicReference<TaskState> state = new AtomicReference<>(new New());

        public TaskState getState() {
            return state.get();
        }

        public void start() {
            state.updateAndGet(s -> {
                if (s instanceof New) {
                    return new Running(System.currentTimeMillis());
                }
                return s;
            });
        }

        public void complete(Object result) {
            state.updateAndGet(s -> {
                if (s instanceof Running r) {
                    return new Completed(result, System.currentTimeMillis() - r.startTime());
                }
                return s;
            });
        }

        public void fail(Throwable error) {
            state.updateAndGet(s -> {
                if (s instanceof New || s instanceof Running) {
                    return new Failed(error);
                }
                return s;
            });
        }
    }
}
