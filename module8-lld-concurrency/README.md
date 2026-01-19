# Module 8: LLD + Concurrency

## Learning Objectives

By completing this module, you will:
- Combine OOP design with thread-safety requirements
- Design systems that handle concurrent access correctly
- Practice the exact format of SDE2 LLD interviews

## Prerequisites

- Complete **module6-classic-problems** first
- Familiarity with SOLID principles and design patterns

## Problems

| Problem | Concurrency Challenge | Similar To |
|---------|----------------------|------------|
| **Seat Booking System** | Prevent double-booking with CAS | BookMyShow, movie tickets |
| **Parking Lot** | Multiple gates, atomic spot allocation | Classic LLD problem |
| **Pub-Sub System** | Concurrent publishers and subscribers | Mini-Kafka, event systems |

## How to Practice

1. **Read requirements** carefully
2. **Design classes first** (entities, services, repositories)
3. **Identify shared mutable state**
4. **Choose synchronization strategy** (locks, atomics, immutability)
5. **Implement and test**

## Interview Tips

- Start by clarifying requirements and constraints
- Draw class diagrams before coding
- Explicitly discuss thread-safety decisions
- Consider scalability (what if 10x traffic?)

## Common Patterns in LLD + Concurrency

- **Optimistic locking**: CAS for low-contention updates
- **Pessimistic locking**: Locks for complex transactions
- **Immutability**: Avoid shared mutable state where possible
- **Actor model**: Single-threaded actors with message passing
