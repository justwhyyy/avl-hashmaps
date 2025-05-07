package test;

import main.datastructure.HashTableWithAVL;
import main.hash.HashFunction;

public class AdversarialInputTest {
    public static void main(String[] args) {
        testWithAdversarialInput();
    }
    
    private static void testWithAdversarialInput() {
        System.out.println("===== Testing with Adversarial Input =====");
        
        // Custom hash function that always returns the same hash code
        HashFunction<Integer> badHashFunction = key -> 42;
        
        HashTableWithAVL<Integer, String> hashTable = 
            new HashTableWithAVL<>(16, 0.75, badHashFunction);
        
        // Insert 1,000 sequential integers (all will hash to the same bucket)
        for (int i = 0; i < 1000; i++) {
            hashTable.insert(i, "Value-" + i);
            
            // Every 100 insertions, print statistics
            if ((i + 1) % 100 == 0) {
                printStatistics(hashTable, i + 1);
            }
        }
        
        // Verify all values can be retrieved
        boolean allFound = true;
        for (int i = 0; i < 1000; i++) {
            String value = hashTable.search(i);
            if (!("Value-" + i).equals(value)) {
                allFound = false;
                System.out.println("Value not found for key: " + i);
                break;
            }
        }
        
        System.out.println("All values retrieved correctly: " + allFound);
        System.out.println("Total rotations: " + hashTable.getTotalRotationCount());
    }
    
    private static void printStatistics(HashTableWithAVL<?, ?> hashTable, int entries) {
        System.out.println("Entries: " + entries);
        System.out.println("Table size: " + hashTable.getCapacity());
        System.out.println("Current load factor: " + hashTable.getCurrentLoadFactor());
        System.out.println("Max AVL tree height: " + hashTable.getMaxHeight());
        System.out.println("Average AVL tree height: " + hashTable.getAverageHeight());
    }
}
