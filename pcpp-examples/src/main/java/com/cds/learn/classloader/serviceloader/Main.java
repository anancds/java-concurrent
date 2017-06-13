package com.cds.learn.classloader.serviceloader;

import java.util.ServiceLoader;

/**
 * Created by chendongsheng5 on 2017/6/13.
 */
public class Main {

  public static void main(String[] args) {
    ServiceLoader<IService> serviceLoader = ServiceLoader.load(IService.class);
    for (IService service : serviceLoader) {
      System.out.println(service.getScheme() + "=" + service.sayHello());
    }
  }
}
