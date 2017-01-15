package com.cds.learn.lock;

/**
 * Created by cds on 1/15/17 11:49.
 */
public class SynchronizedTest1 {

    Object object = new Object();

    public void test1() {
        synchronized (this) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test1: non static, other object");
        }
    }

    public void test2() {
        synchronized (this) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test2: non static, other object");
        }
    }

    public void test3() {

        System.out.println("test3: non static, other object");
    }
}
