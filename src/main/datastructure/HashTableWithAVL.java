package main.datastructure;

import main.hash.HashFunction;
import main.utils.KeyValuePair;
import java.util.List;

public class HashTableWithAVL<K extends Comparable<K>, V> {
    private AVLTree<K, V>[] buckets;
    private int size; 
    private int capacity; 
    private double loadFactorThreshold;
    private HashFunction<K> hashFunction;
    
    //default hash function
    public static class DefaultHashFunction<K> implements HashFunction<K> {
        @Override
        public int hash(K key) {
            return key.hashCode();
        }
    }
    
    public HashTableWithAVL() {
        this(16, 0.75, new DefaultHashFunction<>());
    }

    @SuppressWarnings("unchecked")
    public HashTableWithAVL(int initialCapacity, double loadFactorThreshold, HashFunction<K> hashFunction) {
        this.capacity = initialCapacity;
        this.loadFactorThreshold = loadFactorThreshold;
        this.hashFunction = hashFunction;
        this.size = 0;
        
        //init buckets where each bucket is an AVL tree
        buckets = new AVLTree[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new AVLTree<>();
        }
    }
    
    //get the bucket index for a key
    private int getBucketIndex(K key) {
        int hash = hashFunction.hash(key);
        return Math.abs(hash) % capacity;
    }
    
    //insert a key-value pair
    public void insert(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        int oldSize = buckets[bucketIndex].getSize();
        buckets[bucketIndex].insert(key, value);
        
        if (buckets[bucketIndex].getSize() > oldSize) {
            size++;
            if ((double) size / capacity > loadFactorThreshold) {
                resize();
            }
        }
    }
    
    //search for a key
    public V search(K key) {
        int bucketIndex = getBucketIndex(key);
        return buckets[bucketIndex].search(key);
    }
    
    //delete a key
    public void delete(K key) {
        int bucketIndex = getBucketIndex(key);
        int oldSize = buckets[bucketIndex].getSize();
        buckets[bucketIndex].delete(key);
        
        if (buckets[bucketIndex].getSize() < oldSize) {
            size--;
        }
    }
    
    //resize the hash table
    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;

        AVLTree<K, V>[] oldBuckets = buckets;
        
        buckets = new AVLTree[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new AVLTree<>();
        }

        size = 0;
        
        for (int i = 0; i < oldCapacity; i++) {
            rehashBucket(oldBuckets[i]);
        }
    }
    
    //helper to rehash a bucket during resize
    private void rehashBucket(AVLTree<K, V> tree) {
        List<KeyValuePair<K, V>> pairs = tree.getAllKeyValuePairs();
        for (KeyValuePair<K, V> pair : pairs) {
            insert(pair.getKey(), pair.getValue());
        }
    }
    
    //get total number of entries
    public int getSize() {
        return size;
    }
    
    //get number of buckets
    public int getCapacity() {
        return capacity;
    }
    
    //get current load factor
    public double getCurrentLoadFactor() {
        return (double) size / capacity;
    }
    
    //get max height of all AVL trees
    public int getMaxHeight() {
        int maxHeight = 0;
        for (AVLTree<K, V> bucket : buckets) {
            maxHeight = Math.max(maxHeight, bucket.getHeight());
        }
        return maxHeight;
    }
    
    //get average height of all AVL trees
    public double getAverageHeight() {
        double totalHeight = 0;
        for (AVLTree<K, V> bucket : buckets) {
            totalHeight += bucket.getHeight();
        }
        return totalHeight / capacity;
    }
    
    //get total rotation count from all AVL trees
    public int getTotalRotationCount() {
        int totalRotations = 0;
        for (AVLTree<K, V> bucket : buckets) {
            totalRotations += bucket.getRotationCount();
        }
        return totalRotations;
    }
    
    //get heights of all buckets (for visualization)
    public int[] getBucketHeights() {
        int[] heights = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            heights[i] = buckets[i].getHeight();
        }
        return heights;
    }
}
