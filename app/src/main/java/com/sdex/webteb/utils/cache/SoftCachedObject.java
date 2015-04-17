package com.sdex.webteb.utils.cache;

public class SoftCachedObject<T> {
    T object;

    public SoftCachedObject(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
