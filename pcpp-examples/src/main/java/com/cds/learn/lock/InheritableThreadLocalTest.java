package com.cds.learn.lock;

/**
 * Created by chendongsheng5 on 2017/6/7.
 * reference:
 * http://blog.csdn.net/ni357103403/article/details/51970748
 */
public class InheritableThreadLocalTest {

  //  public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
  private static final ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();

  public static void main(String args[]) {
    threadLocal.set(123);

    Thread thread = new MyThread();
    thread.start();

    System.out.println("main = " + threadLocal.get());
  }

  static class MyThread extends Thread {

    @Override
    public void run() {
      System.out.println("MyThread = " + threadLocal.get());
    }
  }
}
