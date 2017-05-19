package com.cds.learn.spinlock;

/**
 * Created by chendongsheng5 on 2017/5/19.
 * reference:
 * http://blog.csdn.net/iter_zc/article/details/40373881
 */
public class Main {

  private static TimeCost timeCost = new TimeCost(new TASLock());

  //private static TimeCost timeCost = new TimeCost(new TTASLock());

  public static void method() {
    timeCost.lock();
    //int a = 10;
    timeCost.unlock();
  }

  public static void main(String[] args) {
    for (int i = 0; i < 100; i++) {
      Thread t = new Thread(new Runnable() {

        @Override
        public void run() {
          method();
        }

      });
      t.start();
    }
  }

}
