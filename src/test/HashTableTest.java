package test;

import main.datastructure.HashTableWithAVL;

public class HashTableTest {
    public static void main(String[] args) {
        //basic test
        testBasicOperations();
    }
    
    private static void testBasicOperations() {
        System.out.println("===== Basic Operations Test =====");
        
        HashTableWithAVL<String, Integer> hashTable = new HashTableWithAVL<>();
        
        //insert some key-value pairs
        hashTable.insert("apple", 1);
        hashTable.insert("banana", 2);
        hashTable.insert("cherry", 3);
        hashTable.insert("date", 4);
        hashTable.insert("elderberry", 5);
        
        //search for keys
        System.out.println("apple: " + hashTable.search("apple")); // Should be 1
        System.out.println("banana: " + hashTable.search("banana")); // Should be 2
        System.out.println("fig: " + hashTable.search("fig")); // Should be null
        
        //delete a key
        hashTable.delete("banana");
        System.out.println("After deleting banana: " + hashTable.search("banana")); // Should be null
        
        //print statistics
        printStatistics(hashTable);
    }
    
    private static void printStatistics(HashTableWithAVL<?, ?> hashTable) {
        System.out.println("Entries: " + hashTable.getSize());
        System.out.println("Table size: " + hashTable.getCapacity());
        System.out.println("Current load factor: " + hashTable.getCurrentLoadFactor());
        System.out.println("Max AVL tree height: " + hashTable.getMaxHeight());
        System.out.println("Average AVL tree height: " + hashTable.getAverageHeight());
        System.out.println("Total rotations: " + hashTable.getTotalRotationCount());
    }
}