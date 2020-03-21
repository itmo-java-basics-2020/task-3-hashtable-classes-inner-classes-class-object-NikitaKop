package ru.itmo.java;


public class HashTable {
    private final static int INITIAL_CAPACITY = 1024;
    private final static float LOAD_FACTOR = 0.5f;
    private final static float CLEAN_FACTOR = 0.6f;
    private final static int RESIZE_MULTIPLY = 2;

    private int size = 0;
    private int RealSize = 0;
    private int InitialCapacity;
    private float LoadFactor;
    private Entry[] Dict;
    private boolean[] Deleted;

    public HashTable() {
        this(INITIAL_CAPACITY, LOAD_FACTOR);
    }

    public HashTable(int InitialCapacity) {
        this(InitialCapacity, LOAD_FACTOR);
    }

    public HashTable(int InitialCapacity, float lF) {
        this.InitialCapacity = InitialCapacity;
        this.LoadFactor = lF;
        this.Dict = new Entry[InitialCapacity];
        this.Deleted = new boolean[InitialCapacity];
    }

    private int resHash(Object key) {
        int hash = key.hashCode() % Dict.length;
        if (hash < 0) {
            hash += Dict.length;
        }
        return hash;
    }

    public Object put(Object key, Object value) {
        int hash = resHash(key);
        while (Dict[hash] != null) {
            if (key.equals(Dict[hash].getKey())) {
                Object exValue = Dict[hash].getValue();
                Dict[hash].setValue(value);
                return exValue;
            }
            hash++;
            if (hash == Dict.length) {
                hash = 0;
            }
        }
        Deleted[hash] = false;
        Dict[hash] = new Entry(key, value);
        if (!Deleted[hash]) {
            RealSize++;
        }
        size++;
        if (size > Dict.length * LoadFactor) {
            Resize(RESIZE_MULTIPLY);
        }
        if (CLEAN_FACTOR > LoadFactor && RealSize > Dict.length * CLEAN_FACTOR) {
            Resize(1);
        }
        return null;
    }

    public Object get(Object key) {
        int hash = Searching(key);
        if (hash == -1) {
            return null;
        }
        return Dict[hash].getValue();
    }

    public Object remove(Object key) {
        int hash = Searching(key);
        if (hash == -1) {
            return null;
        }
        Object exValue = Dict[hash].getValue();
        Dict[hash] = new Entry();
        Deleted[hash] = true;
        size--;
        return exValue;
    }

    public int size() {
        return size;
    }

    private void Resize(int ResizeMultiply) {
        Entry[] exDict = Dict;
        Dict = new Entry[exDict.length * ResizeMultiply];
        size = 0;
        RealSize = 0;
        Deleted = new boolean[Dict.length];
        for (Entry pair : exDict) {
            if (pair != null && pair.getKey() != null && pair.getValue() != null) {
                put(pair.getKey(), pair.getValue());
            }
        }
    }

    private int Searching(Object key) {
        int hash = resHash(key);
        while (Dict[hash] != null || Deleted[hash]) {
            if (Deleted[hash]) {
                hash++;
                if (hash == Dict.length) {
                    hash = 0;
                }
                continue;
            }
            if (key.equals(Dict[hash].getKey())) {
                return hash;
            }
            hash++;
            if (hash == Dict.length) {
                hash = 0;
            }
        }
        return -1;
    }

    private class Entry {

        private Object key;
        private Object value;

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Entry() {
            this.key = null;
            this.value = null;
        }
        public Entry(Object k, Object v) {
            this.key = k;
            this.value = v;
        }
    }

}
