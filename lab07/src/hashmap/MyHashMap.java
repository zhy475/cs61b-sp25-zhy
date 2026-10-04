package hashmap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A hash table-backed Map implementation.
 *
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private int initialCapacity;
    private double loadFactor;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    @SuppressWarnings("unchecked")
    public MyHashMap(int initialCapacity, double loadFactor) {
        if (initialCapacity < 1 || loadFactor <= 0.0) {
            throw new IllegalArgumentException("Capacity must be >= 1 and load factor must be > 0");
        }
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.size = 0;

        this.buckets = (Collection<Node>[]) new Collection[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        return Math.floorMod(hashCode, buckets.length);
    }

    private Node getNode(K key) {
        int index = getBucketIndex(key);
        Collection<Node> bucket = buckets[index];
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        Node node = getNode(key);
        if (node != null) {
            node.value = value;
            return;
        }

        if ((double) (size + 1) / buckets.length > loadFactor) {
            resize(buckets.length * 2);
        }

        int index = getBucketIndex(key);
        buckets[index].add(new Node(key, value));
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Collection<Node>[] oldBuckets = buckets;
        buckets = (Collection<Node>[]) new Collection[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            buckets[i] = createBucket();
        }

        size = 0;
        for (Collection<Node> bucket : oldBuckets) {
            for (Node node : bucket) {
                put(node.key, node.value);
            }
        }
    }

    @Override
    public V get(K key) {
        Node node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                keys.add(node.key);
            }
        }
        return keys;
    }

    @Override
    public V remove(K key) {
        int index = getBucketIndex(key);
        Collection<Node> bucket = buckets[index];
        Iterator<Node> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.key.equals(key)) {
                iterator.remove();
                size--;
                return node.value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}