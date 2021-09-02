package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>  {
    private class Node {
        private K key;
        private V value;

        private Node left;
        private Node right;

        private Node( K k, V v) {
            key = k;
            value= v;
        }
    }
    private Node root;
    private int size;




    @Override
    public void clear() {
        root= null;
        size=0;

    }

    @Override
    public boolean containsKey(K key) {
        return containsHelper(key, root);
    }

    private boolean containsHelper(K key, Node p) {
        if (key ==  null) throw new IllegalArgumentException("the argument of get() is null!");

        if (root == null){
            return false;
        }

        int cmp = key.compareTo(p.key);

        if (cmp ==0) {
            return true;
        }
        else if (cmp < 0) {
            return containsHelper(key,p.left);
        }

        else if (cmp > 0) {
            return containsHelper(key,p.right );
        }
        else {
            return false;
        }


    }

    @Override
    public int size() {
        return this.size;
    }

    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }

        int cmp = key.compareTo(p.key);

        if (cmp < 0) {
            return getHelper(key,p.left);
        }

        if (cmp > 0) {
            return getHelper(key,p.right );
        }
        return p.value;
    }


    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);

    }

    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size ++;
            return new Node(key, value);
        }

        else if (p.key == key) {
            p.value=value;
        }

        int cmp = key.compareTo(root.key);
        if (cmp < 0) {
            p.left=putHelper(key, value,p.left );
        }

        else if (cmp > 0) {
            p.right= putHelper(key, value,p.right );
        }
        return p;
    }

    @Override
    public Set keySet() {
        throw new UnsupportedOperationException("Invalid operation for sorted list.");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Invalid operation for sorted list.");    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Invalid operation for sorted list.");
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException("Invalid operation for sorted list.");
    }
}
