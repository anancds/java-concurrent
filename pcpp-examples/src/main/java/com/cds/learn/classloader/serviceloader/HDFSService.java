package com.cds.learn.classloader.serviceloader;

/**
 * Created by chendongsheng5 on 2017/6/13.
 */
public class HDFSService implements IService {
  @Override
  public String sayHello() {
    return "Hello HDFSService";
  }
  @Override
  public String getScheme() {
    return "hdfs";
  }
}
