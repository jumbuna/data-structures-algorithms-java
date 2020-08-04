package com.jumbuna.ds;

import java.util.Map;

public class HashMap<K, V> {
    private class MapNode {
        MapNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
        K key;
        V value;
        boolean tombStone = false;
    }

    private int capacity;
    private int threshold;
    final private double loadFactor = .67;
    private int nodeCount;
    private Object[] table = null;

    public HashMap() {
        capacity = 32;
        threshold = (int) (capacity * loadFactor);
        nodeCount = 0;
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * loadFactor);
        Object[] newTable = new Object[capacity];
        for (int i = 0; i < capacity/2; i++) {
            MapNode node = (MapNode) table[i];
            if(node == null) continue;
            if(node.tombStone) {
                table[i] = null;
            }else {
                int hash = hashFunction(node.key);
                for(int j = 1; newTable[hash] != null; j++) {
                    hash = (probeFunction(j) + hash) % capacity;
                }
                newTable[hash] = node;
            }
        }
        table = newTable;
    }

    private int hashFunction(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private int probeFunction(int x) {
        //linear probe
        return x;
    }

    public void insert(K key, V value) {
        if(table == null) {
            table = new Object[capacity];
        }
        int i = indexOf(key);
        if(i != -1) {
            ((MapNode)table[i]).value = value;
            return;
        }
        int hash = hashFunction(key);
        for(int j = 1; table[hash] != null; j++) {
            hash = (hash + probeFunction(j)) % capacity;
        }
        table[hash] = new MapNode(key, value);
        if(nodeCount++ >= threshold) {
            resize();
        }
    }

    public V get(K key) {
        int index = indexOf(key);
        if(index != -1) {
            return ((MapNode) table[index]).value;
        }
        return null;
    }

    private int indexOf(K key) {
        int hash = hashFunction(key);
        for(int i = 1; table[hash] != null; i++) {
            if(((MapNode)table[hash]).key.equals(key) && !((MapNode)table[hash]).tombStone) {
                return hash;
            }
            hash = (probeFunction(i) + hash) % capacity;
        }
        return -1;
    }

    public void remove(K key) {
        int i = indexOf(key);
        if(i != -1) {
            ((MapNode)table[i]).tombStone = true;
            --nodeCount;
        }
    }

    public int size() {
        return nodeCount;
    }

    public void clear() {
        capacity = 16;
        threshold = (int) (capacity * loadFactor);
        table = new Object[capacity];
        nodeCount = 0;
    }

    public Vector<K> keys() {
        Vector<K> keys = new Vector<>(nodeCount*2);
        for (int i = 0; i < capacity; i++) {
            if(table[i] != null) {
                if(!((MapNode) table[i]).tombStone) {
                    keys.insert(((MapNode) table[i]).key);
                }
            }
        }
        return keys;
    }
}
