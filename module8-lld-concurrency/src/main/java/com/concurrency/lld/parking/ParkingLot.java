package com.concurrency.lld.parking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LLD Problem: Thread-Safe Parking Lot
 * 
 * Scenario: Parking lot with multiple entry/exit gates operating concurrently.
 * 
 * Requirements:
 *   1. Multiple gates can accept cars simultaneously
 *   2. Never allocate more spots than capacity
 *   3. Track availability per floor/type
 * 
 * 📝 NOTE: Classic LLD problem - appears in almost every company.
 *   The concurrency twist: Multiple gates → concurrent spot allocation.
 * 
 * 💡 THINK: What if gate A and gate B both see "1 spot available"?
 *   Both try to allocate → over-allocation!
 * 
 * Solutions:
 *   1. AtomicInteger for counters
 *   2. Lock per floor
 *   3. Optimistic locking with retry
 */
public class ParkingLot {
    
    public enum VehicleType {
        MOTORCYCLE,
        CAR,
        TRUCK
    }
    
    public enum SpotType {
        SMALL,      // Motorcycle only
        MEDIUM,     // Car or Motorcycle
        LARGE       // Any vehicle
    }
    
    /**
     * A parking spot.
     */
    public static class ParkingSpot {
        private final String spotId;
        private final SpotType type;
        private volatile String vehicleId;  // null if empty
        
        public ParkingSpot(String spotId, SpotType type) {
            this.spotId = spotId;
            this.type = type;
            this.vehicleId = null;
        }
        
        public boolean isAvailable() {
            return vehicleId == null;
        }
        
        public String getSpotId() {
            return spotId;
        }
        
        public SpotType getType() {
            return type;
        }
        
        /**
         * TODO: Park a vehicle in this spot.
         * 
         * 🔑 HINT: Use synchronized for simple thread-safety.
         *   Or use a volatile check + synchronized block.
         */
        public synchronized boolean park(String vehicleId) {
            if (this.vehicleId != null) {
                return false;  // Already occupied
            }
            this.vehicleId = vehicleId;
            return true;
        }
        
        public synchronized boolean unpark(String vehicleId) {
            if (vehicleId.equals(this.vehicleId)) {
                this.vehicleId = null;
                return true;
            }
            return false;
        }
    }
    
    /**
     * A floor in the parking lot.
     * 
     * 📝 NOTE: Uses AtomicInteger for available count.
     *   This allows lock-free updates!
     */
    public static class ParkingFloor {
        private final String floorId;
        private final Map<String, ParkingSpot> spots;
        private final AtomicInteger availableSmall;
        private final AtomicInteger availableMedium;
        private final AtomicInteger availableLarge;
        
        public ParkingFloor(String floorId, int smallSpots, int mediumSpots, int largeSpots) {
            this.floorId = floorId;
            this.spots = new ConcurrentHashMap<>();
            this.availableSmall = new AtomicInteger(smallSpots);
            this.availableMedium = new AtomicInteger(mediumSpots);
            this.availableLarge = new AtomicInteger(largeSpots);
            
            // Initialize spots
            int spotNum = 1;
            for (int i = 0; i < smallSpots; i++) {
                String id = floorId + "-S" + spotNum++;
                spots.put(id, new ParkingSpot(id, SpotType.SMALL));
            }
            for (int i = 0; i < mediumSpots; i++) {
                String id = floorId + "-M" + spotNum++;
                spots.put(id, new ParkingSpot(id, SpotType.MEDIUM));
            }
            for (int i = 0; i < largeSpots; i++) {
                String id = floorId + "-L" + spotNum++;
                spots.put(id, new ParkingSpot(id, SpotType.LARGE));
            }
        }
        
        /**
         * TODO: Find and allocate a spot for a vehicle.
         * 
         * 💡 THINK: Race condition scenario:
         *   1. Check availableMedium.get() > 0 → true
         *   2. Another thread takes the last spot
         *   3. We try to find a spot → none available!
         * 
         * 🔑 HINT: Decrement counter FIRST, then find spot.
         *   If no spot found, increment counter back.
         *   Use compareAndSet for atomic decrement-if-positive.
         */
        public ParkingSpot findAndAllocate(VehicleType vehicleType, String vehicleId) {
            // Determine which spot types can fit this vehicle
            SpotType[] eligibleTypes = getEligibleSpotTypes(vehicleType);
            
            for (SpotType spotType : eligibleTypes) {
                // Try to reserve a spot of this type
                AtomicInteger counter = getCounter(spotType);
                
                // TODO: Atomically decrement if positive
                // 🔑 HINT: while loop with compareAndSet
                int current;
                do {
                    current = counter.get();
                    if (current <= 0) {
                        break;  // No spots of this type
                    }
                } while (!counter.compareAndSet(current, current - 1));
                
                if (current > 0) {
                    // We reserved one, now find the actual spot
                    for (ParkingSpot spot : spots.values()) {
                        if (spot.getType() == spotType && spot.park(vehicleId)) {
                            return spot;
                        }
                    }
                    // Couldn't find spot (shouldn't happen), restore counter
                    counter.incrementAndGet();
                }
            }
            return null;  // No spot available
        }
        
        public void release(ParkingSpot spot, String vehicleId) {
            if (spot.unpark(vehicleId)) {
                getCounter(spot.getType()).incrementAndGet();
            }
        }
        
        private SpotType[] getEligibleSpotTypes(VehicleType vehicleType) {
            return switch (vehicleType) {
                case MOTORCYCLE -> new SpotType[]{SpotType.SMALL, SpotType.MEDIUM, SpotType.LARGE};
                case CAR -> new SpotType[]{SpotType.MEDIUM, SpotType.LARGE};
                case TRUCK -> new SpotType[]{SpotType.LARGE};
            };
        }
        
        private AtomicInteger getCounter(SpotType type) {
            return switch (type) {
                case SMALL -> availableSmall;
                case MEDIUM -> availableMedium;
                case LARGE -> availableLarge;
            };
        }
        
        public int getTotalAvailable() {
            return availableSmall.get() + availableMedium.get() + availableLarge.get();
        }
    }
    
    // Parking lot with multiple floors
    private final Map<String, ParkingFloor> floors = new ConcurrentHashMap<>();
    
    public void addFloor(String floorId, int small, int medium, int large) {
        floors.put(floorId, new ParkingFloor(floorId, small, medium, large));
    }
    
    /**
     * Entry gate - find a spot for a vehicle.
     */
    public ParkingSpot enter(VehicleType type, String vehicleId) {
        for (ParkingFloor floor : floors.values()) {
            ParkingSpot spot = floor.findAndAllocate(type, vehicleId);
            if (spot != null) {
                return spot;
            }
        }
        return null;  // Lot is full
    }
    
    /**
     * Exit gate - release the spot.
     */
    public void exit(ParkingSpot spot, String vehicleId) {
        String floorId = spot.getSpotId().split("-")[0];
        ParkingFloor floor = floors.get(floorId);
        if (floor != null) {
            floor.release(spot, vehicleId);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        ParkingLot lot = new ParkingLot();
        lot.addFloor("F1", 5, 10, 3);  // 5 small, 10 medium, 3 large
        
        // Simulate multiple cars arriving at different gates
        Thread[] gates = new Thread[20];
        for (int i = 0; i < 20; i++) {
            final String vehicleId = "CAR-" + i;
            gates[i] = new Thread(() -> {
                ParkingSpot spot = lot.enter(VehicleType.CAR, vehicleId);
                if (spot != null) {
                    System.out.println(vehicleId + " parked at " + spot.getSpotId());
                } else {
                    System.out.println(vehicleId + " - LOT FULL");
                }
            }, "Gate-" + (i % 4));  // 4 gates
        }
        
        for (Thread t : gates) t.start();
        for (Thread t : gates) t.join();
        
        // 💡 THINK: 13 spots (10 medium + 3 large), 20 cars
        // Exactly 13 should park, 7 should see "LOT FULL"
    }
}
