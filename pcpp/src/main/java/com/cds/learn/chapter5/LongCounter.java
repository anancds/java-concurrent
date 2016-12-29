package com.cds.learn.chapter5;

/**
 * Created by cds on 12/13/16 13:10.
 */
public class LongCounter {
    private long count = 0;
    public synchronized void increment() {
        count = count + 1;
    }
    public synchronized long get() {
        return count;
    }
}
