package com.cds.learn.classloader.serviceloader;

/**
 * Created by chendongsheng5 on 2017/6/13.
 */
public class LocalService  implements IService {
  @Override
  public String sayHello() {
    return "Hello LocalService";
  }
  @Override
  public String getScheme() {
    return "local";
  }
}
