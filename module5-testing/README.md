# Module 5: Testing Concurrent Code

## Learning Objectives

By completing this module, you will understand:
- Why concurrent code is hard to test
- Stress testing techniques
- Race condition detection
- Deterministic testing approaches

## Prerequisites

- Complete **modules 1-4** first

## Labs

| Lab | Topic | Key Concepts | JCiP Reference |
|-----|-------|--------------|----------------|
| **lab18** | Stress Testing | High contention, load testing | Ch 12.1 (Testing for Correctness) |
| **lab19** | Race Detection | Thread sanitizers, assertions | Ch 12.1.2 (Resource Management) |
| **lab20** | Deterministic Testing | Controlled scheduling | Ch 12.2 (Testing for Performance) |

## How to Practice

1. Write tests that expose race conditions
2. Use stress testing to find intermittent bugs
3. Learn to read test failure patterns

## Common Mistakes to Watch For

- Tests that pass by accident (timing-dependent)
- Not testing edge cases (empty, full, single-element)
- Ignoring flaky test results
- Testing only the happy path

## Next Module

After completing this module, proceed to **module6-classic-problems**.
