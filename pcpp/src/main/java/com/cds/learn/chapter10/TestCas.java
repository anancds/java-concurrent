package com.cds.learn.chapter10;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chendongsheng5 on 2017/5/18.
 * jvm参数：-XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly
 * java参数：a
 */
public class TestCas {
  public static void main(String[] args) {
    AtomicInteger ai = new AtomicInteger();

    for (int i=0; i<100000; i++) {
      ai.compareAndSet(65, ("foo" + args[0]).length());
    }
    System.out.println(ai.get());
  }
}
