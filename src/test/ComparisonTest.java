package test;

import main.datastructure.HashTableWithAVL;
import main.hash.HashFunction;
import java.util.Random;

public class ComparisonTest {
    public static void main(String[] args) {
        compareHashFunctions();
        compareLoadFactors();
    }
    
    private static void compareHashFunctions() {
        System.out.println("===== Comparing Hash Functions =====");
        
        //default hash function 
        HashTableWithAVL<Integer, String> defaultHash = new HashTableWithAVL<>();
        
        //multiplication hash function
        HashFunction<Integer> multiplyHash = key -> {
            int hash = key * 265443576 % 2147483647; // Knuth's multiplicative method
            return hash;
        };
        HashTableWithAVL<Integer, String> multiplyHashTable = 
            new HashTableWithAVL<>(16, 0.75, multiplyHash);
        
        //FNV-1a hash function
        HashFunction<Integer> fnvHash = key -> {
            int hash = 216613626; // FNV offset basis
            String keyStr = key.toString();
            for (int i = 0; i < keyStr.length(); i++) {
                hash ^= keyStr.charAt(i);
                hash *= 16777619; // FNV prime
            }
            return hash;
        };
        HashTableWithAVL<Integer, String> fnvHashTable = 
            new HashTableWithAVL<>(16, 0.75, fnvHash);
        
        //compare by inserting 10,000 sequential integers
        Random random = new Random(42);
        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(1000000);
            
            defaultHash.insert(key, "Value-" + key);
            multiplyHashTable.insert(key, "Value-" + key);
            fnvHashTable.insert(key, "Value-" + key);
        }
        
        System.out.println("Default Hash Function:");
        printStatistics(defaultHash);
        
        System.out.println("\nMultiplication Hash Function:");
        printStatistics(multiplyHashTable);
        
        System.out.println("\nFNV-1a Hash Function:");
        printStatistics(fnvHashTable);
    }
    
    private static void compareLoadFactors() {
        System.out.println("\n===== Comparing Load Factors =====");
        
        //hash tables with different load factors
        HashTableWithAVL<Integer, String> lowLoadFactor = 
            new HashTableWithAVL<Integer, String>(16, 0.5, new HashTableWithAVL.DefaultHashFunction<Integer>());
            
        HashTableWithAVL<Integer, String> mediumLoadFactor = 
            new HashTableWithAVL<Integer, String>(16, 0.75, new HashTableWithAVL.DefaultHashFunction<Integer>());
            
        HashTableWithAVL<Integer, String> highLoadFactor = 
            new HashTableWithAVL<Integer, String>(16, 0.9, new HashTableWithAVL.DefaultHashFunction<Integer>());
        
        //insert 10,000 random key-value pairs
        Random random = new Random(42);
        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(1000000);
            
            lowLoadFactor.insert(key, "Value-" + key);
            mediumLoadFactor.insert(key, "Value-" + key);
            highLoadFactor.insert(key, "Value-" + key);
        }
        
        System.out.println("Load Factor 0.5:");
        printStatistics(lowLoadFactor);
        
        System.out.println("\nLoad Factor 0.75:");
        printStatistics(mediumLoadFactor);
        
        System.out.println("\nLoad Factor 0.9:");
        printStatistics(highLoadFactor);
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
