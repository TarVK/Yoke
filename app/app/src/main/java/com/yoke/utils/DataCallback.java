package com.yoke.utils;

/**
 * An interface for async data callbacks
 * @param <T>  The type of data to be returned
 */
public interface DataCallback<T> {
    void retrieve(T data);
}
