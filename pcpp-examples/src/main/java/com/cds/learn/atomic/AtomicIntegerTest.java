package com.cds.learn.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
    private static AtomicInteger ai = new AtomicInteger(1);

    public static void main(String[] args) {
        System.out.println(ai.get());
        System.out.println(ai.getAndIncrement());
        System.out.println(ai.get());
        System.out.println(ai.getAndAdd(10));
        System.out.println(ai.get());

        System.out.println(ai.compareAndSet(12, 22));
        System.out.println(ai.get());
        System.out.println(ai.compareAndSet(21, 23));
        System.out.println(ai.get());


        System.out.println(ai.getAndSet(23));
        System.out.println(ai.get());


        System.out.println(ai.weakCompareAndSet(22, 24));
        System.out.println(ai.get());
    }
}
