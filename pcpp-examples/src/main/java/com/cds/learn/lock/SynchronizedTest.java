package com.cds.learn.lock;

/**
 * Created by cds on 1/15/17 11:20.
 */
public class SynchronizedTest {

    public synchronized void test1() {
        System.out.println(Thread.currentThread().getId() + "---test1");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test1: non static");
    }

    public synchronized void test2() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test2: non static");
    }

    public void test3() {
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("test3: non static");
    }
}
