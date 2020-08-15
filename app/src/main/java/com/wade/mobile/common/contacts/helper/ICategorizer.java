package com.wade.mobile.common.contacts.helper;

public interface ICategorizer<T, V> {
    T getType(V v);
}