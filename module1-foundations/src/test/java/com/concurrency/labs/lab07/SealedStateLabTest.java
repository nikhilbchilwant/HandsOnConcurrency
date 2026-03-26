package com.concurrency.labs.lab07;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.concurrency.labs.lab07.SealedStateLab.*;

public class SealedStateLabTest {

    @Test
    public void testInitialState() {
        TaskStateMachine sm = new TaskStateMachine();
        assertTrue(sm.getState() instanceof New);
    }

    @Test
    public void testTransitionToRunning() {
        TaskStateMachine sm = new TaskStateMachine();
        sm.start();
        assertTrue(sm.getState() instanceof Running);
    }

    @Test
    public void testTransitionToCompleted() {
        TaskStateMachine sm = new TaskStateMachine();
        sm.start();
        sm.complete("Success");
        TaskState state = sm.getState();
        assertTrue(state instanceof Completed);
        assertEquals("Success", ((Completed) state).result());
    }

    @Test
    public void testTransitionToFailed() {
        TaskStateMachine sm = new TaskStateMachine();
        Exception err = new RuntimeException("Error");
        sm.fail(err);
        TaskState state = sm.getState();
        assertTrue(state instanceof Failed);
        assertEquals(err, ((Failed) state).error());
    }

    @Test
    public void testInvalidTransitions() {
        TaskStateMachine sm = new TaskStateMachine();
        // New -> Completed should be invalid according to logic (only Running -> Completed)
        sm.complete("Early");
        assertTrue(sm.getState() instanceof New);

        sm.start();
        sm.complete("Done");
        assertTrue(sm.getState() instanceof Completed);
        
        // Completed -> Running should be invalid
        sm.start();
        assertTrue(sm.getState() instanceof Completed);
    }
}
