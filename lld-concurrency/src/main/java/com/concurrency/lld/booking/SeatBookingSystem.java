package com.concurrency.lld.booking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * LLD Problem: Concurrent Seat Booking System
 * 
 * Scenario: Movie theater with multiple shows, multiple users booking concurrently.
 * 
 * Requirements:
 *   1. View available seats for a show
 *   2. Book a seat (must prevent double-booking!)
 *   3. Cancel a booking
 * 
 * 📝 NOTE: This is the #1 LLD+Concurrency interview problem.
 *   The key challenge: Two users clicking "book" on the same seat simultaneously.
 * 
 * 💡 THINK: What happens if two threads try to book seat A1 at the same time?
 *   Thread 1: check(A1 available) → true
 *   Thread 2: check(A1 available) → true  (race condition!)
 *   Thread 1: mark A1 as booked
 *   Thread 2: mark A1 as booked → DOUBLE BOOKING!
 * 
 * Solutions:
 *   1. AtomicReference with CAS (compareAndSet)
 *   2. synchronized block per seat
 *   3. Lock per seat (fine-grained)
 */
public class SeatBookingSystem {
    
    /**
     * Seat status enum.
     */
    public enum SeatStatus {
        AVAILABLE,
        BOOKED,
        BLOCKED  // Temporarily held during payment
    }
    
    /**
     * A seat with atomic status for thread-safe updates.
     * 
     * 🔑 HINT: Using AtomicReference allows CAS (compare-and-set) updates.
     *   This is lock-free and high-performance!
     */
    public static class Seat {
        private final String seatId;
        private final AtomicReference<SeatStatus> status;
        private volatile String bookedBy;  // User who booked
        
        public Seat(String seatId) {
            this.seatId = seatId;
            this.status = new AtomicReference<>(SeatStatus.AVAILABLE);
            this.bookedBy = null;
        }
        
        public String getSeatId() {
            return seatId;
        }
        
        public SeatStatus getStatus() {
            return status.get();
        }
        
        public String getBookedBy() {
            return bookedBy;
        }
        
        /**
         * TODO: Implement atomic booking using CAS.
         * 
         * 🔑 HINT: Use compareAndSet to atomically change status:
         *   if (status.compareAndSet(AVAILABLE, BOOKED)) {
         *       this.bookedBy = userId;
         *       return true;
         *   }
         *   return false;
         * 
         * ⚠️ AVOID: Check-then-act without atomicity!
         *   // BROKEN - race condition
         *   if (status.get() == AVAILABLE) {
         *       status.set(BOOKED);
         *   }
         */
        public boolean book(String userId) {
            // TODO: Implement atomic booking
            // Use compareAndSet for atomic check-and-update
            if (status.compareAndSet(SeatStatus.AVAILABLE, SeatStatus.BOOKED)) {
                this.bookedBy = userId;
                return true;
            }
            return false;
        }
        
        /**
         * TODO: Implement cancellation.
         * 
         * 📝 NOTE: Only the user who booked can cancel.
         */
        public boolean cancel(String userId) {
            // TODO: Implement atomic cancellation
            // Only allow if booked by this user
            if (userId.equals(this.bookedBy)) {
                if (status.compareAndSet(SeatStatus.BOOKED, SeatStatus.AVAILABLE)) {
                    this.bookedBy = null;
                    return true;
                }
            }
            return false;
        }
    }
    
    /**
     * A show with multiple seats.
     */
    public static class Show {
        private final String showId;
        private final Map<String, Seat> seats;
        
        public Show(String showId, int rows, int seatsPerRow) {
            this.showId = showId;
            this.seats = new ConcurrentHashMap<>();
            
            // Initialize seats (A1, A2, ... B1, B2, ...)
            for (int row = 0; row < rows; row++) {
                char rowChar = (char) ('A' + row);
                for (int num = 1; num <= seatsPerRow; num++) {
                    String seatId = "" + rowChar + num;
                    seats.put(seatId, new Seat(seatId));
                }
            }
        }
        
        /**
         * Get available seats.
         */
        public long getAvailableCount() {
            return seats.values().stream()
                .filter(s -> s.getStatus() == SeatStatus.AVAILABLE)
                .count();
        }
        
        /**
         * TODO: Book a specific seat.
         * 
         * @return true if booking succeeded, false if seat unavailable
         */
        public boolean bookSeat(String seatId, String userId) {
            Seat seat = seats.get(seatId);
            if (seat == null) {
                throw new IllegalArgumentException("Invalid seat: " + seatId);
            }
            return seat.book(userId);
        }
        
        /**
         * TODO: Book multiple seats atomically (all-or-nothing).
         * 
         * 💡 THINK: What if user wants seats A1, A2, A3 together?
         *   If A1 and A2 succeed but A3 fails, we should rollback A1 and A2!
         * 
         * 🔑 HINT: Book all, if any fail, cancel the ones that succeeded.
         */
        public boolean bookSeats(String[] seatIds, String userId) {
            // TODO: Implement all-or-nothing booking
            // Track which seats we've booked in case we need to rollback
            java.util.List<Seat> booked = new java.util.ArrayList<>();
            
            for (String seatId : seatIds) {
                Seat seat = seats.get(seatId);
                if (seat == null || !seat.book(userId)) {
                    // Rollback previously booked seats
                    for (Seat s : booked) {
                        s.cancel(userId);
                    }
                    return false;
                }
                booked.add(seat);
            }
            return true;
        }
        
        public String getShowId() {
            return showId;
        }
    }
    
    /**
     * The booking system managing multiple shows.
     */
    private final Map<String, Show> shows = new ConcurrentHashMap<>();
    
    public void addShow(String showId, int rows, int seatsPerRow) {
        shows.put(showId, new Show(showId, rows, seatsPerRow));
    }
    
    public boolean bookSeat(String showId, String seatId, String userId) {
        Show show = shows.get(showId);
        if (show == null) {
            throw new IllegalArgumentException("Invalid show: " + showId);
        }
        return show.bookSeat(seatId, userId);
    }
    
    public static void main(String[] args) throws InterruptedException {
        SeatBookingSystem system = new SeatBookingSystem();
        system.addShow("MOVIE-7PM", 5, 10);  // 5 rows, 10 seats each
        
        // Simulate concurrent bookings for same seat
        String targetSeat = "A1";
        Thread[] users = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            final String userId = "User-" + i;
            users[i] = new Thread(() -> {
                boolean success = system.bookSeat("MOVIE-7PM", targetSeat, userId);
                System.out.println(userId + " booking " + targetSeat + ": " + 
                    (success ? "SUCCESS" : "FAILED (already booked)"));
            });
        }
        
        // Start all threads simultaneously
        for (Thread t : users) t.start();
        for (Thread t : users) t.join();
        
        // 💡 THINK: Exactly ONE user should succeed!
        System.out.println("\nOnly one user should have succeeded above.");
    }
}
