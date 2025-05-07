package main.datastructure;

import main.utils.KeyValuePair;
import java.util.ArrayList;
import java.util.List;

public class AVLTree<K extends Comparable<K>, V> {
    private Node root;
    private int size;
    private int rotationCount; // Enhancement: track rotations
    
    private class Node {
        K key;
        V value;
        Node left, right;
        int height;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Leaf nodes have height 1
            this.left = null;
            this.right = null;
        }
    }
    
    public AVLTree() {
        this.root = null;
        this.size = 0;
        this.rotationCount = 0;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getHeight() {
        return height(root);
    }
    
    public int getRotationCount() {
        return rotationCount;
    }
    
    // Get height of a node (null nodes have height 0)
    private int height(Node node) {
        if (node == null) return 0;
        return node.height;
    }
    
    // Update height of a node based on its children
    private void updateHeight(Node node) {
        if (node == null) return;
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }
    
    // Get balance factor of a node (difference between left and right subtree heights)
    private int getBalanceFactor(Node node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }
    
    // Right rotation
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        // Perform rotation
        x.right = y;
        y.left = T2;
        
        // Update heights
        updateHeight(y);
        updateHeight(x);
        
        rotationCount++; // Count rotation
        return x;
    }
    
    // Left rotation
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        // Perform rotation
        y.left = x;
        x.right = T2;
        
        // Update heights
        updateHeight(x);
        updateHeight(y);
        
        rotationCount++; // Count rotation
        return y;
    }
    
    // Insert a key-value pair
    public void insert(K key, V value) {
        root = insertRec(root, key, value);
    }
    
    private Node insertRec(Node node, K key, V value) {
        // Standard BST insert
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insertRec(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, key, value);
        } else {
            // Key already exists, update value
            node.value = value;
            return node;
        }
        
        // Update height of current node
        updateHeight(node);
        
        // Get balance factor to check if this node became unbalanced
        int balance = getBalanceFactor(node);
        
        // Left Left Case
        if (balance > 1 && key.compareTo(node.left.key) < 0)
            return rightRotate(node);
        
        // Right Right Case
        if (balance < -1 && key.compareTo(node.right.key) > 0)
            return leftRotate(node);
        
        // Left Right Case
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        
        return node;
    }
    
    // Search for a key
    public V search(K key) {
        Node result = searchRec(root, key);
        return result == null ? null : result.value;
    }
    
    private Node searchRec(Node node, K key) {
        if (node == null) return null;
        
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;
        if (cmp < 0) return searchRec(node.left, key);
        return searchRec(node.right, key);
    }
    
    // Find node with minimum key value
    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }
    
    // Delete a key
    public void delete(K key) {
        Node node = searchRec(root, key);
        if (node != null) { // Only decrease size if node exists
            size--;
            root = deleteRec(root, key);
        }
    }
    
    private Node deleteRec(Node root, K key) {
        if (root == null) return root;
        
        int cmp = key.compareTo(root.key);
        
        // Find the node to be deleted
        if (cmp < 0) {
            root.left = deleteRec(root.left, key);
        } else if (cmp > 0) {
            root.right = deleteRec(root.right, key);
        } else {
            // Node found
            
            // Node with only one child or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            
            // Node with two children, get inorder successor (smallest in right subtree)
            Node temp = minValueNode(root.right);
            
            // Copy the inorder successor's data to this node
            root.key = temp.key;
            root.value = temp.value;
            
            // Delete the inorder successor
            root.right = deleteRec(root.right, temp.key);
        }
        
        // If the tree had only one node, return
        if (root == null) return null;
        
        // Update height
        updateHeight(root);
        
        // Check balance factor and balance if needed
        int balance = getBalanceFactor(root);
        
        // Left Left Case
        if (balance > 1 && getBalanceFactor(root.left) >= 0)
            return rightRotate(root);
        
        // Left Right Case
        if (balance > 1 && getBalanceFactor(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        
        // Right Right Case
        if (balance < -1 && getBalanceFactor(root.right) <= 0)
            return leftRotate(root);
        
        // Right Left Case
        if (balance < -1 && getBalanceFactor(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        
        return root;
    }
    
    // Get all key-value pairs (for rehashing)
    public List<KeyValuePair<K, V>> getAllKeyValuePairs() {
        List<KeyValuePair<K, V>> pairs = new ArrayList<>();
        collectKeyValuePairs(root, pairs);
        return pairs;
    }
    
    private void collectKeyValuePairs(Node node, List<KeyValuePair<K, V>> pairs) {
        if (node == null) return;
        
        collectKeyValuePairs(node.left, pairs);
        pairs.add(new KeyValuePair<>(node.key, node.value));
        collectKeyValuePairs(node.right, pairs);
    }
}