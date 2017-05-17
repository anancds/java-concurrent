package com.cds.learn.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chendongsheng5 on 2017/5/17.
 * soft reference > weak reference > Phantom Reference的例子
 * 虚引用形同虚设，它所引用的对象随时可能被垃圾回收器回收，具有弱引用的对象拥有稍微长一点的生命周期，
 * 当垃圾回收器执行回收操作时，有可能被垃圾回收器回收，具有软引用的对象拥有更长的生命周期，
 * 但在Java虚拟机认为内存不足的情况下，也是会被垃圾回收器回收的
 *
 * reference：http://stackoverflow.com/questions/3329691/understanding-javas-reference-classes-softreference-weakreference-and-phanto
 */
public class ReferenceTest {

  private static ReferenceQueue<Grocery> rq = new ReferenceQueue<>();

  public static void checkQueue() {
    Reference<? extends Grocery> inq = rq.poll();
    if (inq != null) {
      System.out.println("In queue : " + inq.get());
    }
  }

  public static void main(String[] args) {
    final int size = 10;
    Set<SoftReference<Grocery>> sa = new HashSet<>();

    for (int i = 0; i < size; i++) {
      SoftReference<Grocery> ref =
        new SoftReference<>(new Grocery("Soft " + i), rq);
      System.out.println("Just created: " + ref.get());
      sa.add(ref);
    }

    System.gc();
    checkQueue();

    Set<WeakReference<Grocery>> wa = new HashSet<>();

    for (int i = 0; i < size; i++) {
      WeakReference<Grocery> ref =
        new WeakReference<>(new Grocery("Weak " + i), rq);
      System.out.println("Just created: " + ref.get());
      wa.add(ref);
    }

    System.gc();
    checkQueue();

    Set<PhantomReference<Grocery>> pa = new HashSet<>();

    for (int i = 0; i < size; i++) {
      PhantomReference<Grocery> ref =
        new PhantomReference<>(new Grocery("Phantom " + i), rq);
      System.out.println("Just created: " + ref.get());
      pa.add(ref);
    }

    System.gc();
    checkQueue();
  }
}

class Grocery {

  private static final int SIZE = 10000;
  private double[] d = new double[SIZE];
  private String id;

  public Grocery(String id) {
    this.id = id;
  }

  public String toString() {
    return id;
  }

  public void finalize() {
    System.out.println("Finalizing ... " + id);
  }
}
