package com.cds.learn.chapter5.pipeline;

/**
 * Created by cds on 12/13/16 13:40.
 */
public interface BlockingQueue<T> {

    void put(T item);

    T take();
}
