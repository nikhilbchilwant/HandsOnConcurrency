package com.concurrency.debug.broken;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 🔴 BROKEN CODE - FIND THE BUGS!
 * 
 * This is a banking transfer system with INTENTIONAL BUGS.
 * Your task: Find the concurrency bugs that cause DEADLOCK.
 * 
 * Expected bugs to find: 2
 * Time target: < 5 minutes
 */
public class DeadlockBanking {
    
    public static class Account {
        private final int id;
        private final Lock lock = new ReentrantLock();
        private double balance;
        
        public Account(int id, double initialBalance) {
            this.id = id;
            this.balance = initialBalance;
        }
        
        public int getId() { return id; }
        public double getBalance() { return balance; }
        
        public void deposit(double amount) { balance += amount; }
        
        public void withdraw(double amount) {
            if (balance >= amount) {
                balance -= amount;
            } else {
                throw new IllegalStateException("Insufficient funds");
            }
        }
        
        public Lock getLock() { return lock; }
    }
    
    /**
     * Transfer money from one account to another.
     * 
     * 🔴 BUG #1: DEADLOCK - Inconsistent lock ordering
     * 🔴 BUG #2: Lock not released in finally
     */
    public void transfer(Account from, Account to, double amount) {
        // BUG #1: If thread1 does transfer(A,B) and thread2 does transfer(B,A)
        // they can deadlock!
        from.getLock().lock();
        to.getLock().lock();
        
        // BUG #2: No try-finally means lock won't be released on exception
        from.withdraw(amount);
        to.deposit(amount);
        
        to.getLock().unlock();
        from.getLock().unlock();
    }
}
