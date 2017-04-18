package com.cds.learn.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cds on 1/15/17 11:41.
 */

public class Test1 implements Runnable
{
    ReentrantLock lock = new ReentrantLock();

    public void get()
    {
        lock.lock();
        System.out.println(Thread.currentThread().getId());
        set();
        lock.unlock();
    }

    public void set()
    {
        lock.lock();
        System.out.println(Thread.currentThread().getId());
        lock.unlock();
    }

    @Override
    public void run()
    {
        get();
    }

    public static void main(String[] args)
    {
        Test ss = new Test();
        new Thread(ss).start();
        new Thread(ss).start();
        new Thread(ss).start();
    }
}
