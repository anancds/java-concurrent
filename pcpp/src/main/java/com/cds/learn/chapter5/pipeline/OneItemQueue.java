package com.cds.learn.chapter5.pipeline;

/**
 * Created by cds on 12/13/16 13:42.
 */
public class OneItemQueue<T> implements BlockingQueue<T> {

    private T item;

    private boolean full = false;

    @Override
    public void put(T item) {
        synchronized (this) {
            while (full) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            full = true;
            this.item = item;
            this.notifyAll();
        }

    }

    @Override
    public T take() {

        synchronized (this) {
            while (!full) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            full = false;
            this.notifyAll();
            return item;
        }
    }

}
