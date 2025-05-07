package main.datastructure;

import main.hash.HashFunction;
import main.utils.KeyValuePair;
import java.util.List;

public class HashTableWithAVL<K extends Comparable<K>, V> {
    private AVLTree<K, V>[] buckets;
    private int size; // Total number of entries
    private int capacity; // Number of buckets
    private double loadFactorThreshold;
    private HashFunction<K> hashFunction;
    
    // Default hash function
    public static class DefaultHashFunction<K> implements HashFunction<K> {
        @Override
        public int hash(K key) {
            return key.hashCode();
        }
    }
    
    // Constructor with default parameters
    public HashTableWithAVL() {
        this(16, 0.75, new DefaultHashFunction<>());
    }
    
    // Constructor with custom parameters
    @SuppressWarnings("unchecked")
    public HashTableWithAVL(int initialCapacity, double loadFactorThreshold, HashFunction<K> hashFunction) {
        this.capacity = initialCapacity;
        this.loadFactorThreshold = loadFactorThreshold;
        this.hashFunction = hashFunction;
        this.size = 0;
        
        // Initialize buckets (each bucket is an AVL tree)
        buckets = new AVLTree[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new AVLTree<>();
        }
    }
    
    // Get the bucket index for a key
    private int getBucketIndex(K key) {
        int hash = hashFunction.hash(key);
        return Math.abs(hash) % capacity;
    }
    
    // Insert a key-value pair
    public void insert(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        int oldSize = buckets[bucketIndex].getSize();
        buckets[bucketIndex].insert(key, value);
        
        // If size increased, a new entry was added
        if (buckets[bucketIndex].getSize() > oldSize) {
            size++;
            
            // Check if resizing is needed
            if ((double) size / capacity > loadFactorThreshold) {
                resize();
            }
        }
    }
    
    // Search for a key
    public V search(K key) {
        int bucketIndex = getBucketIndex(key);
        return buckets[bucketIndex].search(key);
    }
    
    // Delete a key
    public void delete(K key) {
        int bucketIndex = getBucketIndex(key);
        int oldSize = buckets[bucketIndex].getSize();
        buckets[bucketIndex].delete(key);
        
        // If size decreased, an entry was removed
        if (buckets[bucketIndex].getSize() < oldSize) {
            size--;
        }
    }
    
    // Resize the hash table
    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        
        // Save old buckets
        AVLTree<K, V>[] oldBuckets = buckets;
        
        // Create new buckets
        buckets = new AVLTree[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new AVLTree<>();
        }
        
        // Reset size since we'll reinsert all entries
        size = 0;
        
        // Reinsert all entries from old buckets
        for (int i = 0; i < oldCapacity; i++) {
            rehashBucket(oldBuckets[i]);
        }
    }
    
    // Helper to rehash a bucket during resize
    private void rehashBucket(AVLTree<K, V> tree) {
        List<KeyValuePair<K, V>> pairs = tree.getAllKeyValuePairs();
        for (KeyValuePair<K, V> pair : pairs) {
            insert(pair.getKey(), pair.getValue());
        }
    }
    
    // Get total number of entries
    public int getSize() {
        return size;
    }
    
    // Get number of buckets
    public int getCapacity() {
        return capacity;
    }
    
    // Get current load factor
    public double getCurrentLoadFactor() {
        return (double) size / capacity;
    }
    
    // Get max height of all AVL trees
    public int getMaxHeight() {
        int maxHeight = 0;
        for (AVLTree<K, V> bucket : buckets) {
            maxHeight = Math.max(maxHeight, bucket.getHeight());
        }
        return maxHeight;
    }
    
    // Get average height of all AVL trees
    public double getAverageHeight() {
        double totalHeight = 0;
        for (AVLTree<K, V> bucket : buckets) {
            totalHeight += bucket.getHeight();
        }
        return totalHeight / capacity;
    }
    
    // Get total rotation count from all AVL trees
    public int getTotalRotationCount() {
        int totalRotations = 0;
        for (AVLTree<K, V> bucket : buckets) {
            totalRotations += bucket.getRotationCount();
        }
        return totalRotations;
    }
    
    // Get heights of all buckets (for visualization)
    public int[] getBucketHeights() {
        int[] heights = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            heights[i] = buckets[i].getHeight();
        }
        return heights;
    }
}
