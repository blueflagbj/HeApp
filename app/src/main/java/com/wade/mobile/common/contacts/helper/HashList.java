package com.wade.mobile.common.contacts.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class HashList<T, V> {
    private ICategorizer<T, V> categorizer;
    private HashMap<T, List<V>> map = new HashMap<>();
    private List<T> typeList = new ArrayList();

    public HashList(ICategorizer<T, V> categorizer2) {
        this.categorizer = categorizer2;
    }

    public T getType(V value) {
        return this.categorizer.getType(value);
    }

    public T getType(int typeIndex) {
        return this.typeList.get(typeIndex);
    }

    public boolean containsTyps(T type) {
        return this.typeList.contains(type);
    }

    public List<V> removeType(T type) {
        if (!this.typeList.contains(type) || !this.typeList.remove(type)) {
            return null;
        }
        return this.map.remove(type);
    }

    public void sortType(Comparator<T> comparator) {
        Collections.sort(this.typeList, comparator);
    }

    public List<V> getValues(int typeIndex) {
        T type = getType(typeIndex);
        if (type != null) {
            return this.map.get(type);
        }
        return null;
    }

    public List<T> getTypes() {
        return this.typeList;
    }

    public V getValue(int typeIndex, int valueIndex) {
        return getValues(typeIndex).get(valueIndex);
    }

    public int typeSize() {
        return this.typeList.size();
    }

    public int size() {
        int size = 0;
        for (T t : this.typeList) {
            size += this.map.get(t).size();
        }
        return size;
    }

    public void clear() {
        this.map.clear();
        this.typeList.clear();
    }

    public boolean containsValue(V value) {
        T type = getType(value);
        if (!this.typeList.contains(type) || !this.map.get(type).contains(value)) {
            return false;
        }
        return true;
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean add(V value) {
        return add(getType(value), value);
    }

    public boolean add(T type, V value) {
        if (!containsTyps(type)) {
            List<V> list = new ArrayList<>();
            if (!this.typeList.add(type)) {
                return false;
            }
            this.map.put(type, list);
        }
        return this.map.get(type).add(value);
    }

    public int indexOfType(T type) {
        return this.typeList.indexOf(type);
    }
}