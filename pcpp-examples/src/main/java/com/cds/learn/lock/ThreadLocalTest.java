package com.cds.learn.lock;

/**
 * Created by chendongsheng5 on 2017/4/26.
 * 这个例子用来说明ThreadLoacl的用法。
 *
 * 有一个个人的理解，不一定对，锁就是时间换空间，因为java的并发模式就是共享可变的，ThreadLocal可以理解为
 * 空间换时间。
 *
 *
 * ThreadLoacl不是用于解决共享变量问题，也不是为了协调线程同步，而是为了方便每个线程处理自己的状态而引入的一个机制。
 * 根据官方文档可以总结出三点：
 * 1、每个线程都有自己的局部变量。
 * 2、每个线程都有独立于变量的初始化副本。
 * 3、每个线程有自己的一个ThreadLocal，它是变量的一个“拷贝”，修改它不影响其他线程。
 */
public class ThreadLocalTest {

  private static final ThreadLocal<Integer> local = ThreadLocal.withInitial(() -> 0);

  public static void main(String[] args) throws InterruptedException {
    Thread[] threads = new Thread[5];
    for (int j = 0; j < 5; j++) {
      threads[j] = new Thread(() -> {
        //获取当前线程的本地变量，然后累加5次
        int num = local.get();
        for (int i = 0; i < 5; i++) {
          num++;
        }
        //重新设置累加后的本地变量
        local.set(num);
        System.out.println(Thread.currentThread().getName() + " : " + local.get());

      }, "Thread-" + j);
    }

    for (Thread thread : threads) {
      thread.start();
    }
  }
}
