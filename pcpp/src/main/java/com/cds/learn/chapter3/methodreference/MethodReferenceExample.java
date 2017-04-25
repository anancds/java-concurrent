package com.cds.learn.chapter3.methodreference;

/**
 * Created by chendongsheng5 on 2017/4/25.
 */
public class MethodReferenceExample {
  void close() {
    System.out.println("Close.");
  }

  public static void main(String[] args) throws Exception {
    MethodReferenceExample referenceObj = new MethodReferenceExample();
    try (AutoCloseable ac = referenceObj::close) {
    }
  }
}
