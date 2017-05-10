package com.cds.learn.classloader;

/**
 * Created by chendongsheng5 on 2017/5/10.
 */
public class LocalClassCopy {

  public void print() {
    System.out.println("Hi Here is LocalClassCopy");
  }

  private LocalClassCopy instance;

  public void setInstance(Object instance) {
    this.instance = (LocalClassCopy) instance;
  }
}


