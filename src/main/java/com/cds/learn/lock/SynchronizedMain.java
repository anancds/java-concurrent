package com.cds.learn.lock;

import java.util.TreeMap;

/**
 * Created by cds on 1/15/17 11:17.
 */
public class SynchronizedMain {

    public static void main(String[] args) {

//        SynchronizedTest test = new SynchronizedTest();
        SynchronizedTest1 test = new SynchronizedTest1();
        SynchronizedTest1 test1 = new SynchronizedTest1();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                test.test1();
//                test.test3();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    test.test3();
//                }
                test1.test1();
            }
        }).start();

    }
}
