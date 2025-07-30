# AVL HashMaps

HashMap implementation using AVL trees for collision resolution instead of Java's Red-Black trees.

## Implementation

- **AVL Tree buckets**: Self-balancing BST with height difference ≤ 1
- **Dynamic resizing**: Maintains load factor < 0.75
- **Multiple hash functions**: Pluggable hashing strategies

## Performance Results

### Random Input (10k entries)
- AVL HashMap: 2.3x slower insertion, 2.2x slower deletion
- Max tree height: 3, 370 rotations performed

### Collision-Heavy Input (adversarial)
- **AVL HashMap: 3.15x faster collision handling**
- **AVL HashMap: 3.79x faster search under collisions**
- Max tree height: 10 (1k entries), 990 rotations

## Key Findings

- Standard HashMap faster for random data
- AVL HashMap significantly faster under hash collisions
- Maintains O(log n) worst-case vs O(n) degradation
- Resistant to hash flooding attacks
