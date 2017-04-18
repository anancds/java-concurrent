package com.cds.learn.chapter1;

/**
 * 这个例子用来解释synchronized的字节码
 */
public class ClassBytes {
	public synchronized void test1(){

	}

	/**
	 * 用来解释Thread2里的现象
	 */
	public void test2(){
		synchronized (this){

		}
	}

	public static void main(String[] args) {
		ClassBytes a = new ClassBytes();
		ClassBytes b = new ClassBytes();

	}
}
