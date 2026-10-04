import java.util.Set;
import java.util.Iterator;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        private K key;
        private V value;
        private Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = putHelper(root, key, value);
    }

    private Node putHelper(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = compare(key, node.key);

        if (cmp < 0) {
            node.left = putHelper(node.left, key, value);
        } else if (cmp > 0) {
            node.right = putHelper(node.right, key, value);
        } else {
            node.value = value;
        }

        return node;
    }

    @Override
    public V get(K key) {
        return getHelper(root, key);
    }

    private V getHelper(Node node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = compare(key, node.key);

        if (cmp < 0) {
            return getHelper(node.left, key);
        } else if (cmp > 0) {
            return getHelper(node.right, key);
        } else {
            return node.value;
        }
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyHelper(root, key);
    }

    private boolean containsKeyHelper(Node node, K key) {
        if (node == null) {
            return false;
        }

        int cmp = compare(key, node.key);

        if (cmp < 0) {
            return containsKeyHelper(node.left, key);
        } else if (cmp > 0) {
            return containsKeyHelper(node.right, key);
        } else {
            return true;
        }
    }

    private int compare(K key1, K key2) {
        if (key1 == null && key2 == null) {
            return 0;
        } else if (key1 == null) {
            return -1;
        } else if (key2 == null) {
            return 1;
        } else {
            return key1.compareTo(key2);
        }
    }

    public void printInOrder() {
        printInOrderHelper(root);
    }

    private void printInOrderHelper(Node node) {
        if (node == null) return;
        printInOrderHelper(node.left);
        System.out.println(node.key + ": " + node.value);
        printInOrderHelper(node.right);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}