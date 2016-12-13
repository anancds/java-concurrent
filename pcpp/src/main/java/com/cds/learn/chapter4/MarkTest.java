package com.cds.learn.chapter4;

/**
 * Created by cds on 12/12/16 21:05.
 */
public class MarkTest {

    private static double multiply(int i) {
        double x = 1.1 * (double) (i & 0xFF);
        return x * x * x * x * x * x * x * x * x * x;
    }


    public static void Mark0() {
        Timer t = new Timer();
        double dummy = multiply(10);
        double time = t.check() * 1e9;
        System.out.printf("%6.1f ns%n", time);
    }

    public static void main(String[] args) {

        Mark0();
    }
}
