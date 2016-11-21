package com.cds.learn.chapter2;

import java.io.IOException;




public class ThreadsafeLongCounter {
    public static void main(String[] args) throws IOException {
        final LongCounter lc = new LongCounter();
        Thread t = new Thread(() -> {
            //noinspection InfiniteLoopStatement
            while (true)		// Forever call increment
                lc.increment();
        });
        t.start();
        System.out.println("Press Enter to get the current value:");
        //noinspection InfiniteLoopStatement
        while (true) {
            System.in.read();         // Wait for enter key
            System.out.println(lc.get());
        }
    }
}

class LongCounter {
//    @GuardedBy("this")
    private long count = 0;
    public synchronized void increment() {
        count++;
    }
    public synchronized long get() {
        return count;
    }
}
