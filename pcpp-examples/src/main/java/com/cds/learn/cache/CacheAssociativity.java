package com.cds.learn.cache;

/**
 * Created by chendongsheng5 on 2017/5/18.
 * 说明缓冲关联性的例子
 *
 * 大概有三种方式可以将缓存映射到主存上。
 * 1、直接映射(Direct mapped cache)：就是hash，chunk_index % cache_slots
 * 2、N路组关联(N-way set associated cache)：把cache分组，内存通过直接映射到cache的组上，然后组内是完全关联。
 * 3、完全关联(Fully associative cache)：每个内存块能够映射到任意一个缓存槽。
 *也就是说如果是1组，那么就是完全关联，如果每个槽位是一个组，那么就是直接映射，所以N路相关联是一种折中的方式。
 *
 * reference:
 * http://blog.csdn.net/hs794502825/article/details/37937949
 * http://igoro.com/archive/gallery-of-processor-cache-effects/的例子5
 */
public class CacheAssociativity {

  public static long UpdateEveryKthByte(byte[] arr, int K) {
    final int rep = 1024 * 1024; // Number of iterations – arbitrary
    int p = 0;
    for (int i = 0; i < rep; i++) {
      arr[p]++;
      p += K;
      if (p >= arr.length)
        p = 0;
    }
    return 1;

  }
}
