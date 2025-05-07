package main.hash;

public interface HashFunction<K> {
    int hash(K key);
}
