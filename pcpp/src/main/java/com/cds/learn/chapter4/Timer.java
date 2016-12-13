package com.cds.learn.chapter4;

/**
 * Created by cds on 12/12/16 21:03.
 */
public class Timer {
    private long start, spent = 0;

    public Timer() {
        play();
    }

    public double check() {
        return (System.nanoTime() - start + spent) / 1e9;
    }

    public void pause() {
        spent += System.nanoTime() - start;
    }

    public void play() {
        start = System.nanoTime();
    }
}
