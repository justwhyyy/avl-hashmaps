package test;

import main.datastructure.HashTableWithAVL;
import java.util.Random;

public class RandomInputTest {
    public static void main(String[] args) {
        testWithRandomInput();
    }
    
    private static void testWithRandomInput() {
        System.out.println("===== Testing with Random Input =====");
        
        HashTableWithAVL<Integer, String> hashTable = new HashTableWithAVL<>();
        Random random = new Random(42); //fixed seed for reproducibility
        
        //insert 10,000 random key-value pairs
        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(1000000);
            hashTable.insert(key, "Value-" + key);
            
            //every 1000 insertions, print statistics
            if ((i + 1) % 1000 == 0) {
                printStatistics(hashTable, i + 1);
            }
        }
        
        //search for 1000 random keys and measure hit rate
        int hits = 0;
        for (int i = 0; i < 1000; i++) {
            int key = random.nextInt(1000000);
            if (hashTable.search(key) != null) {
                hits++;
            }
        }
        
        System.out.println("Search hit rate: " + (double) hits / 1000);
    }
    
    private static void printStatistics(HashTableWithAVL<?, ?> hashTable, int entries) {
        System.out.println("Entries: " + entries);
        System.out.println("Table size: " + hashTable.getCapacity());
        System.out.println("Current load factor: " + hashTable.getCurrentLoadFactor());
        System.out.println("Max AVL tree height: " + hashTable.getMaxHeight());
        System.out.println("Average AVL tree height: " + hashTable.getAverageHeight());
        System.out.println("Total rotations: " + hashTable.getTotalRotationCount());
    }
}

