package com.concurrency.annotations;

import java.lang.annotation.*;

/**
 * Indicates that a class is thread-safe and can be used concurrently by multiple threads.
 * Based on Java Concurrency in Practice (JCiP).
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ThreadSafe {}
