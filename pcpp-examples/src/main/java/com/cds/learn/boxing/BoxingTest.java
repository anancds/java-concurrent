package com.cds.learn.boxing;

import java.util.ArrayList;

/**
 * Created by chendongsheng5 on 2017/4/26.
 * reference：
 * http://www.importnew.com/18346.html
 *
 * 自动装箱和拆箱感觉是java设计上的一个缺陷，把数据类型和对象分开了，数据类型就是一些基本的类型，就是primitive type。
 *
 * 自动装箱的弊端就是会造成大量的多余的对象，影响程序性能。
 */
public class BoxingTest {

  private static void test1() {
    ArrayList<Integer> intList = new ArrayList<Integer>();
    intList.add(1); //autoboxing - primitive to object
    intList.add(2); //autoboxing

    ThreadLocal<Integer> intLocal = new ThreadLocal<Integer>();
    intLocal.set(4); //autoboxing

    int number = intList.get(0); // unboxing
    int local = intLocal.get(); // unboxing in Java
  }

  /**
   * 在装箱的时候自动调用的是Integer的valueOf(int)方法。而在拆箱的时候自动调用的是Integer的intValue方法。
   * 看下字节码就能看出来
   */
  private static void test2() {
    //before java5
    Integer iObject = Integer.valueOf(3);
    int iPrimitive = iObject.intValue();

//after java5
    Integer iObject1 = 3; //autobxing - primitive to wrapper conversion
    int iPrimitive1 = iObject1; //unboxing - object to primitive conversion
  }

  /**
   * 这是一个比较容易出错的地方，”==“可以用于原始值进行比较，也可以用于对象进行比较，当用于对象与对象之间比较时，
   * 比较的不是对象代表的值，而是检查两个对象是否是同一对象，这个比较过程中没有自动装箱发生。
   * 进行对象值比较不应该使用”==“，而应该使用对象对应的equals方法。看一个能说明问题的例子。
   */
  private static void test3() {
    // Example 1: == comparison pure primitive – no autoboxing
    int i1 = 1;
    int i2 = 1;
    System.out.println("i1==i2 : " + (i1 == i2)); // true

    // Example 2: equality operator mixing object and primitive
    Integer num1 = 1; // autoboxing
    int num2 = 1;
    System.out.println("num1 == num2 : " + (num1 == num2)); // true

    // Example 3: special case - arises due to autoboxing in Java
    Integer obj1 = 1; // autoboxing will call Integer.valueOf()
    Integer obj2 = 1; // same call to Integer.valueOf() will return same
    // cached Object

    System.out.println("obj1 == obj2 : " + (obj1 == obj2)); // true

    // Example 4: equality operator - pure object comparison
    Integer one = new Integer(1); // no autoboxing
    Integer anotherOne = new Integer(1);
    System.out.println("one == anotherOne : " + (one == anotherOne)); // false
  }

  public static void main(String[] args) {
    test3();
  }
}
