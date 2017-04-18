package com.cds.learn.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayTest {

    private static int[] value = new int[] {1, 2};
    private static AtomicIntegerArray ai = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        System.out.println(ai.get(0));
        System.out.println(ai.get(1));

        System.out.println(ai.getAndSet(0, 2));
        System.out.println(ai.get(0));
    }
}
