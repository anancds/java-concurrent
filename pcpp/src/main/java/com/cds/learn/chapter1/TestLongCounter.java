package com.cds.learn.chapter1;

import java.io.IOException;

public class TestLongCounter {
    public static void main(String[] args) throws IOException {
        final LongCounter lc = new LongCounter();

        Thread t = new Thread(() -> {
            while (true)		// Forever call increment
                lc.increment();
        });
        t.start();
        System.out.println("Press Enter to get the current value:");
        while (true) {
//            System.in.read();         // Wait for enter key
            System.out.println(lc.get());
        }
    }
}

class LongCounter {
    private long count = 0;
    public synchronized void increment() {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      count = count + 1;
    }
    public synchronized long get() {
        return count;
    }
}
