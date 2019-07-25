package com.example.mylibrary;

public interface DataCallback<T> {
    void onDataFetched(T data);
}
