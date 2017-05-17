package com.cds.learn.classloader.hotswap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chendongsheng5 on 2017/5/16.
 */
public class Main {

  /**
   * @param args the command line arguments
   *
   * 快速定位ClassLoader的jvm参数是：-XX:+TraceClassLoading -XX:+TraceClassUnloading
   */
  public static void main(String[] args)
    throws ClassNotFoundException, IOException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, InterruptedException {
    String path = "pcpp-examples\\src\\main\\resources\\LocalClass.class";
    ManageClassLoader mc = new ManageClassLoader();
    while (true) {
      Class c = mc.loadClass(path);
      Object o = c.newInstance();
      Method m = c.getMethod("getName");
      m.invoke(o);
      System.out.println(c.getClassLoader());
      Thread.sleep(5000);
    }


  }
}
