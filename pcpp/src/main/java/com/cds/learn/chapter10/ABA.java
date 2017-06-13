package com.cds.learn.chapter10;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 用AtomicStampedReference和AtomicMarkableReference解决ABA问题,具体原理如下：
 * 1. 创建一个Pair类来记录对象引用和时间戳信息,采用int作为时间戳，实际使用的时候时间戳信息要做成自增的，
 * 否则时间戳如果重复，还会出现ABA的问题。这个Pair对象是不可变对象，所有的属性都是final的，
 * of方法每次返回一个新的不可变对象。
 *
 * 2. 使用一个volatile类型的引用指向当前的Pair对象，一旦volatile引用发生变化，变化对所有线程可见。
 *
 * 3. set方法时，当要设置的对象和当前Pair对象不一样时，新建一个不可变的Pair对象。
 *
 * 4. compareAndSet方法中，只有期望对象的引用和版本号和目标对象的引用和版本好都一样时，
 * 才会新建一个Pair对象，然后用新建的Pair对象和原理的Pair对象做CAS操作。
 *
 * 5. 实际的CAS操作比较的是当前的pair对象和新建的pair对象，pair对象封装了引用和时间戳信息。
 */
public class ABA {

  private static AtomicInteger atomicInt = new AtomicInteger(100);
  private static AtomicStampedReference<Integer> atomicStampedRef =
    new AtomicStampedReference<>(100, 0);

  public static void main(String[] args) throws InterruptedException {
    Thread intT1 = new Thread(() -> {
      atomicInt.compareAndSet(100, 101);
      atomicInt.compareAndSet(101, 100);
    });

    Thread intT2 = new Thread(() -> {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      boolean c3 = atomicInt.compareAndSet(100, 101);
      System.out.println(c3);        //true
    });

    intT1.start();
    intT2.start();
    intT1.join();
    intT2.join();

    Thread refT1 = new Thread(() -> {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      atomicStampedRef.compareAndSet(100, 101,
        atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
      atomicStampedRef.compareAndSet(101, 100,
        atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
    });

    Thread refT2 = new Thread(() -> {
      int stamp = atomicStampedRef.getStamp();
      System.out.println("before sleep : stamp = " + stamp);    // stamp = 0
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("after sleep : stamp = " + atomicStampedRef.getStamp());//stamp = 2
      boolean c3 = atomicStampedRef.compareAndSet(100, 101, stamp, stamp + 1);
      System.out.println(c3);        //false
    });

    refT1.start();
    refT2.start();
  }

}
