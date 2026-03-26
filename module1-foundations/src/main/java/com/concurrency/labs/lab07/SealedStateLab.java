package com.concurrency.labs.lab07;

import com.concurrency.annotations.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Lab 07: Sealed States
 * 
 * Demonstrates using Java 17 Sealed Classes for safe and predictable state management
 * in concurrent state machines.
 */
public class SealedStateLab {

    /**
     * Sealed interface defining possible states for a task.
     * Using sealed classes provides exhaustive type checking.
     */
    public sealed interface TaskState permits New, Running, Completed, Failed {}

    public record New() implements TaskState {}
    public record Running(long startTime) implements TaskState {}
    public record Completed(Object result, long duration) implements TaskState {}
    public record Failed(Throwable error) implements TaskState {}

    /**
     * A thread-safe state machine using sealed states.
     * 💡 THINK: How do sealed classes prevent "rogue" states from being introduced?
     */
    @ThreadSafe
    public static class TaskStateMachine {
        private final AtomicReference<TaskState> state = new AtomicReference<>(new New());

        public TaskState getState() {
            return state.get();
        }

        public void start() {
            // TODO: Transition to Running state if it was New.
            // 💡 THINK: Why is state.updateAndGet() better than if/set?
        }

        public void complete(Object result) {
            // TODO: Transition to Completed state only if it was Running.
            // Be sure to record the final result and the duration.
        }

        public void fail(Throwable error) {
            // TODO: Transition to Failed state if it was New or Running.
        }
    }

    public static void main(String[] args) {
        TaskStateMachine sm = new TaskStateMachine();
        System.out.println("Initial State: " + sm.getState());
        
        // After implementing, this should work correctly:
        // sm.start();
        // sm.complete("Execution successful");
        // System.out.println("Final State: " + sm.getState());
    }
}
