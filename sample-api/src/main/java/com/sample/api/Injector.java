package com.sample.api;


import com.sample.api.finder.Finder;

public interface Injector<T> {
    /**
     * @param host   target
     * @param source source
     * @param finder
     */
    void inject(T host, Object source, Finder finder);
}
