public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left, RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

    public RedBlackTree() {
        root = null;
    }

    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        RBTreeNode<T> newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;

        newRoot.isBlack = node.isBlack;
        node.isBlack = false;

        return newRoot;
    }

    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        RBTreeNode<T> newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;

        newRoot.isBlack = node.isBlack;
        node.isBlack = false;

        return newRoot;
    }

    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    public void insert(T item) {
        root = insertHelper(root, item);
        root.isBlack = true;
    }

    private RBTreeNode<T> insertHelper(RBTreeNode<T> node, T item) {
        if (node == null) {
            return new RBTreeNode<>(false, item);
        }

        int cmp = item.compareTo(node.item);
        if (cmp < 0) {
            node.left = insertHelper(node.left, item);
        } else if (cmp > 0) {
            node.right = insertHelper(node.right, item);
        } else {
            return node;
        }

        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }

        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }

        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }
}