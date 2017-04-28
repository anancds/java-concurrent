package com.cds.learn.chapter6;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by chendongsheng5 on 2017/4/27.
 */
public class StripedWriteMap<K,V> implements OurMap<K,V> {
  // Synchronization policy: writing to
  //   buckets[hash] is guarded by locks[hash % lockCount]
  //   sizes[stripe] is guarded by locks[stripe]
  // Visibility of writes to reads is ensured by writes writing to
  // the stripe's size component (even if size does not change) and
  // reads reading from the stripe's size component.
  private volatile ItemNode<K,V>[] buckets;
  private final int lockCount;
  private final Object[] locks;
  private final AtomicIntegerArray sizes;

  public StripedWriteMap(int bucketCount, int lockCount) {
    if (bucketCount % lockCount != 0)
      throw new RuntimeException("bucket count must be a multiple of stripe count");
    this.lockCount = lockCount;
    this.buckets = makeBuckets(bucketCount);
    this.locks = new Object[lockCount];
    this.sizes = new AtomicIntegerArray(lockCount);
    for (int stripe=0; stripe<lockCount; stripe++)
      this.locks[stripe] = new Object();
  }

  @SuppressWarnings("unchecked")
  private static <K,V> ItemNode<K,V>[] makeBuckets(int size) {
    // Java's @$#@?!! type system requires "unsafe" cast here:
    return (ItemNode<K,V>[])new ItemNode[size];
  }

  // Protect against poor hash functions and make non-negative
  private static <K> int getHash(K k) {
    final int kh = k.hashCode();
    return (kh ^ (kh >>> 16)) & 0x7FFFFFFF;
  }

  // Return true if key k is in map, else false
  public boolean containsKey(K k) {
    final ItemNode<K,V>[] bs = buckets;
    final int h = getHash(k), stripe = h % lockCount, hash = h % bs.length;
    // The sizes access is necessary for visibility of bs elements
    return sizes.get(stripe) != 0 && ItemNode.search(bs[hash], k, null);
  }

  // Return value v associated with key k, or null
  public V get(K k) {
    // TO DO: IMPLEMENT
    return null;
  }

  public int size() {
    // TO DO: IMPLEMENT
    return 0;
  }

  // Put v at key k, or update if already present.  The logic here has
  // become more contorted because we must not hold the stripe lock
  // when calling reallocateBuckets, otherwise there will be deadlock
  // when two threads working on different stripes try to reallocate
  // at the same time.
  public V put(K k, V v) {
    final int h = getHash(k), stripe = h % lockCount;
    final Holder<V> old = new Holder<V>();
    ItemNode<K,V>[] bs;
    int afterSize;
    synchronized (locks[stripe]) {
      bs = buckets;
      final int hash = h % bs.length;
      final ItemNode<K,V> node = bs[hash],
        newNode = ItemNode.delete(node, k, old);
      bs[hash] = new ItemNode<>(k, v, newNode);
      // Write for visibility; increment if k was not already in map
      afterSize = sizes.addAndGet(stripe, newNode == node ? 1 : 0);
    }
    if (afterSize * lockCount > bs.length)
      reallocateBuckets(bs);
    return old.get();
  }

  // Put v at key k only if absent.
  public V putIfAbsent(K k, V v) {
    // TO DO: IMPLEMENT
    return null;
  }

  // Remove and return the value at key k if any, else return null
  public V remove(K k) {
    // TO DO: IMPLEMENT
    return null;
  }

  // Iterate over the hashmap's entries one stripe at a time.
  public void forEach(Consumer<K,V> consumer) {
    // TO DO: IMPLEMENT
  }

  // Now that reallocation happens internally, do not do it externally
  public void reallocateBuckets() { }

  // First lock all stripes.  Then double bucket table size, rehash,
  // and redistribute entries.  Since the number of stripes does not
  // change, and since buckets.length is a multiple of lockCount, a
  // key that belongs to stripe s because (getHash(k) % N) %
  // lockCount == s will continue to belong to stripe s.  Hence the
  // sizes array need not be recomputed.

  // In any case, do not reallocate if the buckets field was updated
  // since the need for reallocation was discovered; this means that
  // another thread has already reallocated.  This happens very often
  // with 16 threads and a largish buckets table, size > 10,000.

  public void reallocateBuckets(final ItemNode<K,V>[] oldBuckets) {
    lockAllAndThen(() -> {
      final ItemNode<K,V>[] bs = buckets;
      if (oldBuckets == bs) {
        // System.out.printf("Reallocating from %d buckets%n", bs.length);
        final ItemNode<K,V>[] newBuckets = makeBuckets(2 * bs.length);
        for (int hash=0; hash<bs.length; hash++) {
          ItemNode<K,V> node = bs[hash];
          while (node != null) {
            final int newHash = getHash(node.k) % newBuckets.length;
            newBuckets[newHash]
              = new ItemNode<>(node.k, node.v, newBuckets[newHash]);
            node = node.next;
          }
        }
        buckets = newBuckets; // Visibility: buckets field is volatile
      }
    });
  }

  // Lock all stripes, perform action, then unlock all stripes
  private void lockAllAndThen(Runnable action) {
    lockAllAndThen(0, action);
  }

  private void lockAllAndThen(int nextStripe, Runnable action) {
    if (nextStripe >= lockCount)
      action.run();
    else
      synchronized (locks[nextStripe]) {
        lockAllAndThen(nextStripe + 1, action);
      }
  }

  static class ItemNode<K,V> {
    private final K k;
    private final V v;
    private final ItemNode<K,V> next;

    public ItemNode(K k, V v, ItemNode<K,V> next) {
      this.k = k;
      this.v = v;
      this.next = next;
    }

    // These work on immutable data only, no synchronization needed.

    public static <K,V> boolean search(ItemNode<K,V> node, K k, Holder<V> old) {
      while (node != null)
        if (k.equals(node.k)) {
          if (old != null)
            old.set(node.v);
          return true;
        } else
          node = node.next;
      return false;
    }

    public static <K,V> ItemNode<K,V> delete(ItemNode<K,V> node, K k, Holder<V> old) {
      if (node == null)
        return null;
      else if (k.equals(node.k)) {
        old.set(node.v);
        return node.next;
      } else {
        final ItemNode<K,V> newNode = delete(node.next, k, old);
        if (newNode == node.next)
          return node;
        else
          return new ItemNode<>(node.k, node.v, newNode);
      }
    }
  }

  // Object to hold a "by reference" parameter.  For use only on a
  // single thread, so no need for "volatile" or synchronization.

  static class Holder<V> {
    private V value;
    public V get() {
      return value;
    }
    public void set(V value) {
      this.value = value;
    }
  }
}
