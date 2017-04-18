package com.cds.learn.chapter1;

public class VolatileTest {

    public volatile static int count = 0;

    public static void inc() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 100; i++) {
            count++;
        }
    }

    public static void main(String[] args) {
        new Thread(VolatileTest::inc).start();

        VolatileTest.inc();
        System.out.println("the count is: " + count);
    }

}
