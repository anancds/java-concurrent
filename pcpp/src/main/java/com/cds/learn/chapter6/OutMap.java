package com.cds.learn.chapter6;

import java.util.function.Consumer;

/**
 * Created by cds on 12/13/16 21:54.
 */
public interface OutMap<K,V>{

    boolean containsKey(K k);
    V get(K k);
    V put(K k, V v);
    V putIfAbsent(K k, V v);
    V remove(K k);
    int size();
//    void forEach(Consumer<K,V> consumer);
    void reallocateBuckets();
}
