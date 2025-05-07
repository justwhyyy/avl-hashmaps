package test;

import main.datastructure.HashTableWithAVL;
import main.hash.HashFunction;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class HashMapComparisonTest {
    private static final int NUM_OPERATIONS = 100000; 
    private static final int NUM_RUNS = 5; 
    public static void main(String[] args) {
        //running all tests
        randomInsertionTest();
        sequentialInsertionTest();
        searchTest();
        deletionTest();
        collisionTest();
        memoryUsageTest();
    }
    
    private static void randomInsertionTest() {
        System.out.println("\n===== Random Insertion Performance Test =====");
        
        //running multiple times and take average
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            HashTableWithAVL<Integer, String> avlHashTable = new HashTableWithAVL<>();
            HashMap<Integer, String> standardHashMap = new HashMap<>();
            
            Random random = new Random(run); // Same seed for both maps on each run
            
            //measure AVL HashMap insertion time
            long startTime = System.nanoTime();
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                int key = random.nextInt(NUM_OPERATIONS * 10);
                avlHashTable.insert(key, "Value-" + key);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap insertion time
            startTime = System.nanoTime();
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                int key = random.nextInt(NUM_OPERATIONS * 10);
                standardHashMap.put(key, "Value-" + key);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        //print average results
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            totalAVLTime / (NUM_RUNS * 1_000_000.0), totalHashMapTime / (NUM_RUNS * 1_000_000.0));
        System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap%n",
            (double) totalAVLTime / totalHashMapTime);
    }
    
    private static void sequentialInsertionTest() {
        System.out.println("\n===== Sequential Insertion Performance Test =====");
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            HashTableWithAVL<Integer, String> avlHashTable = new HashTableWithAVL<>();
            HashMap<Integer, String> standardHashMap = new HashMap<>();
            
            //measure AVL HashMap insertion time
            long startTime = System.nanoTime();
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                avlHashTable.insert(i, "Value-" + i);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap insertion time
            startTime = System.nanoTime();
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                standardHashMap.put(i, "Value-" + i);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        //print average results
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            totalAVLTime / (NUM_RUNS * 1_000_000.0), totalHashMapTime / (NUM_RUNS * 1_000_000.0));
        System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap%n",
            (double) totalAVLTime / totalHashMapTime);
    }
    
    private static void searchTest() {
        System.out.println("\n===== Search Performance Test =====");
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            HashTableWithAVL<Integer, String> avlHashTable = new HashTableWithAVL<>();
            HashMap<Integer, String> standardHashMap = new HashMap<>();
            
            //prepare data - insert the same data in both maps
            Random random = new Random(run);
            List<Integer> keys = new ArrayList<>();
            
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                int key = random.nextInt(NUM_OPERATIONS * 10);
                avlHashTable.insert(key, "Value-" + key);
                standardHashMap.put(key, "Value-" + key);
                
                //save keys for search test (50% chance)
                if (random.nextBoolean()) {
                    keys.add(key);
                }
            }
            
            //adding some non-existent keys
            for (int i = 0; i < NUM_OPERATIONS / 10; i++) {
                keys.add(NUM_OPERATIONS * 10 + i); // Keys that don't exist
            }
            
            //measure AVL HashMap search time
            long startTime = System.nanoTime();
            for (Integer key : keys) {
                avlHashTable.search(key);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap search time
            startTime = System.nanoTime();
            for (Integer key : keys) {
                standardHashMap.get(key);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        //printing avg results
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            totalAVLTime / (NUM_RUNS * 1_000_000.0), totalHashMapTime / (NUM_RUNS * 1_000_000.0));
        System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap%n",
            (double) totalAVLTime / totalHashMapTime);
    }
    
    private static void deletionTest() {
        System.out.println("\n===== Deletion Performance Test =====");
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            HashTableWithAVL<Integer, String> avlHashTable = new HashTableWithAVL<>();
            HashMap<Integer, String> standardHashMap = new HashMap<>();
            
            //prepare data 
            Random random = new Random(run);
            List<Integer> keys = new ArrayList<>();
            
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                int key = random.nextInt(NUM_OPERATIONS * 10);
                avlHashTable.insert(key, "Value-" + key);
                standardHashMap.put(key, "Value-" + key);
                
                //save keys for deletion test
                if (random.nextInt(3) == 0) {
                    keys.add(key);
                }
            }
            
            //measure AVL HashMap deletion time
            long startTime = System.nanoTime();
            for (Integer key : keys) {
                avlHashTable.delete(key);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap deletion time
            startTime = System.nanoTime();
            for (Integer key : keys) {
                standardHashMap.remove(key);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        //print average results
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            totalAVLTime / (NUM_RUNS * 1_000_000.0), totalHashMapTime / (NUM_RUNS * 1_000_000.0));
        System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap%n",
            (double) totalAVLTime / totalHashMapTime);
    }
    
    private static void collisionTest() {
        System.out.println("\n===== Collision Handling Test =====");
        
        //creating a bad hash function that always returns the same value
        class BadHashFunction implements HashFunction<Integer> {
            @Override
            public int hash(Integer key) {
                return 42; // Always returns the same hash
            }
        }
        
        //create class for bad hash keys in standard HashMap
        class BadHashKey {
            private final int value;
            
            public BadHashKey(int value) {
                this.value = value;
            }
            
            @Override
            public int hashCode() {
                return 42; 
            }
            
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                BadHashKey other = (BadHashKey) obj;
                return value == other.value;
            }
        }
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            //reset tables
            HashTableWithAVL<Integer, String> avlHashTable = 
                new HashTableWithAVL<Integer, String>(16, 0.75, new BadHashFunction());
            
            //use BadHashKey for standard HashMap
            HashMap<BadHashKey, String> standardHashMap = new HashMap<>();
            
            //num of operations for collision test (reduced for performance)
            int collisionOps = NUM_OPERATIONS / 10;
            
            //measure AVL HashMap with collisions
            long startTime = System.nanoTime();
            for (int i = 0; i < collisionOps; i++) {
                avlHashTable.insert(i, "Value-" + i);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap with collisions
            startTime = System.nanoTime();
            for (int i = 0; i < collisionOps; i++) {
                standardHashMap.put(new BadHashKey(i), "Value-" + i);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            totalAVLTime / (NUM_RUNS * 1_000_000.0), totalHashMapTime / (NUM_RUNS * 1_000_000.0));
        
        if (totalAVLTime < totalHashMapTime) {
            System.out.printf("Relative Performance: AVL is %.2f times faster than standard HashMap under collisions%n",
                (double) totalHashMapTime / totalAVLTime);
        } else {
            System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap even under collisions%n",
                (double) totalAVLTime / totalHashMapTime);
        }
        
        System.out.println("\n===== Search Under Collisions Test =====");
        
        totalAVLTime = 0;
        totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            //reset and prepare tables with colliding data
            HashTableWithAVL<Integer, String> avlHashTable = 
                new HashTableWithAVL<Integer, String>(16, 0.75, new BadHashFunction());
            
            //use BadHashKey for standard HashMap
            HashMap<BadHashKey, String> standardHashMap = new HashMap<>();
            
            int collisionOps = NUM_OPERATIONS / 10;
            
            //insert data
            for (int i = 0; i < collisionOps; i++) {
                avlHashTable.insert(i, "Value-" + i);
            }
            
            List<BadHashKey> badKeys = new ArrayList<>();
            for (int i = 0; i < collisionOps; i++) {
                BadHashKey key = new BadHashKey(i);
                standardHashMap.put(key, "Value-" + i);
                badKeys.add(key);
            }
            
            //measure AVL HashMap search time with collisions
            long startTime = System.nanoTime();
            for (int i = 0; i < collisionOps; i++) {
                avlHashTable.search(i);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap search time with collisions
            startTime = System.nanoTime();
            for (BadHashKey key : badKeys) {
                standardHashMap.get(key);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap search: %.3f ms, Standard HashMap search: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        System.out.printf("Average - AVL HashMap search: %.3f ms, Standard HashMap search: %.3f ms%n", 
            totalAVLTime / (NUM_RUNS * 1_000_000.0), totalHashMapTime / (NUM_RUNS * 1_000_000.0));
        
        if (totalAVLTime < totalHashMapTime) {
            System.out.printf("Relative Search Performance: AVL is %.2f times faster than standard HashMap under collisions%n",
                (double) totalHashMapTime / totalAVLTime);
        } else {
            System.out.printf("Relative Search Performance: AVL is %.2f times slower than standard HashMap even under collisions%n",
                (double) totalAVLTime / totalHashMapTime);
        }
    }
    
    private static void memoryUsageTest() {
        System.out.println("\n===== Memory Usage Test =====");
        
        //force garbage collection to get a cleaner measurement
        System.gc();
        
        //measure initial memory usage
        long initialMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        //create and fill AVL HashMap
        HashTableWithAVL<Integer, String> avlHashTable = new HashTableWithAVL<>();
        for (int i = 0; i < NUM_OPERATIONS; i++) {
            avlHashTable.insert(i, "Value-" + i);
        }
        
        //force garbage collection again
        System.gc();
        
        //measure memory after AVL HashMap creation
        long afterAVLMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long avlMemoryUsage = afterAVLMemory - initialMemory;
        
        //clear AVL HashMap to free memory
        avlHashTable = null;
        System.gc();
        
        //reset initial memory measurement
        initialMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        //create and fill standard HashMap
        HashMap<Integer, String> standardHashMap = new HashMap<>();
        for (int i = 0; i < NUM_OPERATIONS; i++) {
            standardHashMap.put(i, "Value-" + i);
        }
        
        //force garbage collection again
        System.gc();
        
        //measure memory after standard HashMap creation
        long afterStandardMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long standardMemoryUsage = afterStandardMemory - initialMemory;
        
        System.out.printf("AVL HashMap memory usage: %.2f MB%n", avlMemoryUsage / (1024.0 * 1024.0));
        System.out.printf("Standard HashMap memory usage: %.2f MB%n", standardMemoryUsage / (1024.0 * 1024.0));
        System.out.printf("Memory Ratio: AVL HashMap uses %.2f times the memory of standard HashMap%n",
            (double) avlMemoryUsage / standardMemoryUsage);
    }
}