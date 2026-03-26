package com.concurrency.annotations;

import java.lang.annotation.*;

/**
 * Indicates that a class is NOT thread-safe and should be used with external synchronization or within a single thread.
 * Based on Java Concurrency in Practice (JCiP).
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface NotThreadSafe {}
