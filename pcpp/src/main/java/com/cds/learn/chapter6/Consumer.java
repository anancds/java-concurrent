package com.cds.learn.chapter6;

/**
 * Created by chendongsheng5 on 2017/4/27.
 */
interface Consumer<K,V> {
  void accept(K k, V v);
}
