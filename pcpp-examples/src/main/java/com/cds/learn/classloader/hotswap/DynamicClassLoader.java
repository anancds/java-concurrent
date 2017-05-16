package com.cds.learn.classloader.hotswap;

/**
 * Created by chendongsheng5 on 2017/5/16.
 */
public class DynamicClassLoader extends ClassLoader {


  public Class<?> findClass(byte[] b) throws ClassNotFoundException {

    return defineClass(null, b, 0, b.length);
  }
}
