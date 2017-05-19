package com.cds.learn.spinlock;

/**
 * Created by chendongsheng5 on 2017/5/19.
 */
public interface Lock {
  public void lock();

  public void unlock();
}
