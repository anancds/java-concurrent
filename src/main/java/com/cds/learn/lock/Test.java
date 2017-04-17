package com.cds.learn.lock;

/**
 * Created by cds on 1/15/17 11:25.
 * 可冲入锁的例子
 */

public class Test implements Runnable {

	public static void main(String[] args) {
		Test ss = new Test();
		new Thread(ss).start();
		new Thread(ss).start();
		new Thread(ss).start();
		//        Test ss = new Test();
		//        Test ss1 = new Test();
		//        Test ss2 = new Test();
		//        new Thread(ss).start();
		//        new Thread(ss1).start();
		//        new Thread(ss2).start();
	}

	public synchronized void get() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getId());
		set();
	}

	public synchronized void set() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getId());
	}

	@Override
	public void run() {
		get();
	}
}
