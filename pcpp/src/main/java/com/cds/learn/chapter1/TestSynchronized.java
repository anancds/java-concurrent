package com.cds.learn.chapter1;

/**
 * Created by chendongsheng5 on 2017/4/17.
 * 这个例子用来说明类锁和静态的对象锁其实是一个锁
 */
public class TestSynchronized {

	public static synchronized void test2() {
		int i = 5;
		while (i-- > 0) {
			System.out.println(Thread.currentThread().getName() + " : " + i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}
		}
	}

	public static void main(String[] args) {
		final TestSynchronized myt2 = new TestSynchronized();
		Thread test1 = new Thread(new Runnable() {
			public void run() {
				myt2.test1();
			}
		}, "test1");
		Thread test2 = new Thread(new Runnable() {
			public void run() {
				TestSynchronized.test2();
			}
		}, "test2");
		test1.start();
		test2.start();
		//         TestRunnable tr=new TestRunnable();
		//         Thread test3=new Thread(tr);
		//         test3.start();
	}

	public void test1() {
		synchronized (TestSynchronized.class) {
			int i = 5;
			while (i-- > 0) {
				System.out.println(Thread.currentThread().getName() + " : " + i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
			}
		}
	}

}
