package com.cds.learn.cache;

/**
 * Created by chendongsheng5 on 2017/4/26.
 * 这个例子用来说明指令级别并发,例子有点问题，貌似无法说明，目前还没有想明白怎么回事。
 */
public class Cache2 {

  private static void instruction() {
    int steps = 256 * 1024 * 1024;
    int[] a = new int[2];

    long start1 = System.currentTimeMillis();

// Loop 1
    for (int i = 0; i < steps; i++) {
      a[0]++;
      a[0]++;
    }

    long start2 = System.currentTimeMillis();

    System.out.println("Loop1 cost time: " + (start2 - start1) + "ms");
// Loop 2
    for (int i = 0; i < steps; i++) {
      a[0]++;
      a[1]++;
    }

    long end = System.currentTimeMillis();

    System.out.println("Loop2 cost time: " + (end - start2) + "ms");
  }

  public static void main(String[] args) {

    instruction();
  }
}
