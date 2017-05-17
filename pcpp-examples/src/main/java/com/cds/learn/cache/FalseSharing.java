package com.cds.learn.cache;

import sun.misc.Contended;

/**
 * Created by chendongsheng5 on 2017/5/17.
 * FalseSharing：http://ifeve.com/falsesharing/
 */
public final class FalseSharing implements Runnable {

  public final static int NUM_THREADS = 4; // change
  public final static long ITERATIONS = 500L * 1000L * 1000L;
  private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

  static {
    for (int i = 0; i < longs.length; i++) {
      longs[i] = new VolatileLong();
    }
  }

  private final int arrayIndex;

  public FalseSharing(final int arrayIndex) {
    this.arrayIndex = arrayIndex;
  }

  public static void main(final String[] args) throws Exception {
    final long start = System.currentTimeMillis();
    runTest();
    System.out.println("duration = " + (System.currentTimeMillis() - start) + "ms.");
  }

  private static void runTest() throws InterruptedException {
    Thread[] threads = new Thread[NUM_THREADS];

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new FalseSharing(i));
    }

    for (Thread t : threads) {
      t.start();
    }

    for (Thread t : threads) {
      t.join();
    }
  }

  public void run() {
    long i = ITERATIONS + 1;
    while (0 != --i) {
      longs[arrayIndex].value = i;
    }
  }

  /**
   * cache line一般是64字节，long型数据是8字节,对象头是8字节。
   * 所有对象都有两个字长的对象头。第一个字是由24位哈希码和8位标志位（如锁的状态或作为锁对象）组成的Mark Word。
   * 第二个字是对象所属类的引用。如果是数组对象还需要一个额外的字来存储数组的长度。
   * 每个对象的起始地址都对齐于8字节以提高性能。因此当封装对象的时候为了高效率，
   * 对象字段声明的顺序会被重排序成下列基于字节大小的顺序：
   * doubles (8) 和 longs (8)
   * ints (4) 和 floats (4)
   * shorts (2) 和 chars (2)
   * booleans (1) 和 bytes (1)
   * references (4/8)
   * <子类字段重复上述顺序>
   */
//  public final static class VolatileLong {
//
//    //这里填充7个long型数据
//    public volatile long value = 0L;
//    public long p1, p2, p3, p4, p5, p6; // comment out
//  }

  /**
   * 这个是java8原始支持缓冲行填充，并且jvm参数需要加上：-XX:-RestrictContended
   * 但是为了说明，例子还是用上面的好。
   */
  @Contended
  public final static class VolatileLong {
    public volatile long value = 0L;
  }
}
