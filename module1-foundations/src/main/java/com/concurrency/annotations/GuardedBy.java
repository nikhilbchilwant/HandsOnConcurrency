package com.concurrency.annotations;

import java.lang.annotation.*;

/**
 * Indicates that the field or method should be accessed only when a specific lock is held.
 * Based on Java Concurrency in Practice (JCiP).
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface GuardedBy {
    /**
     * The name of the lock that guards the annotated field or method.
     */
    String value();
}
