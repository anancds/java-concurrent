package com.cds.learn.chapter6;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chendongsheng5 on 2017/4/27.
 */
public class WrapConcurrentHashMap<K,V> implements OurMap<K,V> {
  final ConcurrentHashMap<K,V> underlying = new ConcurrentHashMap<>();

  public boolean containsKey(K k) {
    return underlying.containsKey(k);
  }

  public V get(K k) {
    return underlying.get(k);
  }

  public V put(K k, V v) {
    return underlying.put(k, v);
  }

  public V putIfAbsent(K k, V v) {
    return underlying.putIfAbsent(k, v);
  }

  public V remove(K k) {
    return underlying.remove(k);
  }

  public int size() {
    return underlying.size();
  }

  public void forEach(Consumer<K,V> consumer) {
    underlying.forEach((k,v) -> consumer.accept(k,v));
  }

  public void reallocateBuckets() { }
}
