# Module 7: Capstone Projects

## Learning Objectives

By completing this module, you will:
- Integrate multiple concurrency concepts into complete systems
- Design thread-safe architectures from scratch
- Handle real-world concerns (shutdown, error handling, monitoring)

## Prerequisites

- Complete **modules 1-6** first

## Projects

| Project | Description | JCiP Reference |
|---------|-------------|----------------|
| **Pipeline** | Multi-stage data processing pipeline | Ch 5.3 (Producer-Consumer) |
| **Orchestrator** | Task orchestration with dependencies | Ch 6, 8 (Executors/Pools) |
| **Scheduler** | Full-featured task scheduler | Ch 6 (Task Execution) |

## How to Practice

1. Read the project requirements carefully
2. Design your architecture before coding
3. Implement incrementally, testing each component
4. Consider edge cases and failure modes

## Evaluation Criteria

- **Correctness**: Does it work under concurrent access?
- **Performance**: No unnecessary blocking or contention?
- **Robustness**: Handles errors and shutdown gracefully?
- **Clarity**: Is the code understandable?

## Next Module

After completing capstone projects, try **module8-lld-concurrency** for LLD interview practice.
