package com.cds.learn.tools;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerTest {

    private static final Exchanger<String> exgr = new Exchanger<>();

    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        threadPool.execute(() -> {
            String A = "银行A";
            try {
                exgr.exchange(A);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPool.execute(() -> {
            String B = "银行B";
            try {
                String A = exgr.exchange(B);
                System.out.println("A:" + A + " B: " + B);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        threadPool.shutdown();
    }
}
