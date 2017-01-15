package com.cds.learn.lock;

/**
 * Created by cds on 1/15/17 11:07.
 */
public class Child extends Father {
    public static void main(String[] args) {
        Child child = new Child();
        child.doSomething();
    }

    public synchronized void doSomething() {
        System.out.println("child.doSomething()");
        doAnotherThing(); // 调用自己类中其他的synchronized

    }

    private synchronized void doAnotherThing() {
        super.doSomething(); // 调用父类的synchronized
        System.out.println("child.doAnotherThing()");
    }

}

class Father {
    public synchronized void doSomething() {
        System.out.println("father.doSomething()");
    }
}
