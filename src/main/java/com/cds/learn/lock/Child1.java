package com.cds.learn.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cds on 1/15/17 11:11.
 */
public class Child1 extends Father1 {

    public static void main(String[] args) {
        Child child = new Child();
        child.doSomething();
    }

    public void doSomething() {
        lock.lock();
        try {
            System.out.println("child.doSomething()");
            doAnotherThing(); // 调用自己类中其他的synchronized
        } finally {
            lock.unlock();
        }

    }

    private void doAnotherThing() {
        lock.lock();
        try {
            super.doSomething(); // 调用父类的synchronized
            System.out.println("child.doAnotherThing()");
        } finally {
            lock.unlock();
        }
    }
}

class Father1 {
    Lock lock = new ReentrantLock();

    public void doSomething() {
        lock.lock();
        try {
            System.out.println("father.doSomething()");

        } finally {
            lock.unlock();
        }
    }

}
