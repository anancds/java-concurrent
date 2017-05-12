package com.cds.learn.classloader;

/**
 * Created by chendongsheng5 on 2017/5/11.
 * MusicPlayer1 是由 AppClassLoader 进行的加载
 *
 * 演示流程：
 * 1、先运行在main函数
 * 2、然后打包，放到C:\Program Files\Java\jre1.8.0_101\lib\ext目录下
 * 3、把MusicPlayer类修改成MusicPlayer1。
 *
 */
public class MusicPlayer1 {

  private static void loadClass() throws ClassNotFoundException {
    Class<?> clazz = Class.forName("com.cds.learn.classloader.MusicPlayer1");
    ClassLoader classLoader = clazz.getClassLoader();
    System.out.printf("ClassLoader is %s", classLoader.getClass().getSimpleName());
    System.out.println();
  }

  /**
   * 验证 AppClassLoader 的双亲真的是 ExtClassLoader 和 BootstrapClassLoader 吗？
   * BootstrapClassLoader 比较特殊，它是由 JVM 内部实现的,所以这里没有打印出来。
   */
  private static void printParent() throws ClassNotFoundException {
    Class<?> clazz = Class.forName("com.cds.learn.classloader.MusicPlayer1");
    ClassLoader classLoader = clazz.getClassLoader();
    System.out.printf("currentClassLoader is %s\n", classLoader.getClass().getSimpleName());

    while (classLoader.getParent() != null) {
      classLoader = classLoader.getParent();
      System.out.printf("Parent is %s\n", classLoader.getClass().getSimpleName());
    }
  }

  public static void main(String[] args) throws ClassNotFoundException {
    MusicPlayer1.loadClass();
    MusicPlayer1.printParent();
  }

  @SuppressWarnings("unused")
  public void print() {
    System.out.printf("Hi I'm MusicPlayer1");
  }
}
