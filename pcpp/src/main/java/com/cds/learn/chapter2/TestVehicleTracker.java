package com.cds.learn.chapter2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>Vehicle tracker 测试类</p>
 */
@SuppressWarnings("unused")
public class TestVehicleTracker {

  private static void testMonitorVehicleTracker() {
    MutablePoint point = new MutablePoint();
    point.x = 3;
    point.y = 4;
    Map<String, MutablePoint> map = new HashMap<>();
    map.put("#1", point);

    MonitorVehicleTracker monitorVehicleTracker = new MonitorVehicleTracker(map);
    System.out.println(monitorVehicleTracker.getLocations());
    System.out.println(monitorVehicleTracker.getLocation("#1"));

    new Thread(() -> {
      for (int i = 0; i < 20; i++) {
        monitorVehicleTracker.setLocation("#1", i, i + 1);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

    new Thread(() -> System.out.println(monitorVehicleTracker.getLocations())).start();

    new Thread(() -> {
      MutablePoint point1 = monitorVehicleTracker.getLocation("#1");
      while (true) {

        System.out.println(point1.x + ", " + point1.y);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  private static void testDelegatingVehicleTracker() {
    Point point = new Point(3, 4);
    Map<String, Point> map = new HashMap<>();
    map.put("#1", point);

    DelegatingVehicleTracker delegatingVehicleTracker = new DelegatingVehicleTracker(map);
    System.out.println(delegatingVehicleTracker.getLocations());
    System.out.println(delegatingVehicleTracker.getLocation("#1"));

    new Thread(() -> {
      for (int i = 0; i < 20; i++) {
        delegatingVehicleTracker.setLocation("#1", i, i + 1);
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

    new Thread(() -> {
      Point point1 = delegatingVehicleTracker.getLocation("#1");

      for (int i = 0; i < 20; i++) {
        System.out.println(point1.x + ", " + point1.y);
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

    new Thread(() -> {
      Map<String, Point> map1 = delegatingVehicleTracker.getLocations();
      for (int i = 0; i < 20; i++) {
        System.out.println(map1);
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public static void main(String[] args) {

    //		testMonitorVehicleTracker();

    //		testDelegatingVehicleTracker();
  }
}

/**
 * 有这么几个理由来说吗这个类是线程不安全的。
 * 1、设计到this指针暴露问题，如果不加final的话，可能在字段赋值之前就暴露给外部。这里的本质其实是指令重排序。加final就不让指令重排序。举个例子吧
 * Point p = new Point(3, 4);
 * 那么理想的执行顺序是：
 * tmp = allocate Point;
 * tmp.x = 3;
 * tmp.y = 4;
 * p = tmp; // 此时this的引用暴露在外
 * 如果x和y有final修饰，那么执行顺序就会保证跟上述顺序行为一致。
 * 但是没有final修饰的话，运行时各种部分可以让执行乱序，例如：
 * tmp = allocate Point;
 * tmp.x = 3;
 * p = tmp;
 * tmp.y = 4;
 * 这里设计到编译器的指令重排序。再举个重排序的例子，比如从内存中读数据到缓存中，读取到a到一个地址，同一个地址做别的事，然后a做计算。
 * 这样this就在完全初始化完成前先被暴露出去了。这样就有可能会被认为是线程不安全。 <- 当然还是得看具体的使用场景才可以讨论到底算不算线程不安全。
 * <p>
 * 2、字段x和y是public且mutable，那么外部随意修改，并且对别的线程不可见，不是安全发布。
 * <p>
 * 3、类似p.x=p.x+1这样的依赖于当前值的修改操作不是原子的，会导致丢失修改，所以即使在属性x,y之前加上volatile关键字也不是线程安全的
 */
class MutablePoint {

  public int x, y;

  public MutablePoint() {
    x = 0;
    y = 0;
  }

  public MutablePoint(MutablePoint p) {
    this.x = p.x;
    this.y = p.y;
  }

  @Override
  public String toString() {
    return "MutablePoint{" + "x=" + x + ", y=" + y + '}';
  }
}

/**
 * Monitor-based vehicle tracker implementation; version V1.
 *
 * @author Brian Goetz and Tim Peierls
 */

/**
 * 这个版本的本质是在返回客户代码之前复制可变的数据来维持线程安全。那么存在的问题就有：
 * 1、数据量大时不断拷贝肯定影响性能。
 * 2、实时性不够，也就是返回只是数据的快照而已，但是保证了最终一致性，在locations上是一致的。原因就是每次getLocation和getLocations都要复制数据。
 * 比如getLocations返回的是一个UnmodifiableMap，即使更新了location上的数据，但是要获取实时数据只能重新getLocations下。
 */
@SuppressWarnings("unused")
class MonitorVehicleTracker {

  private final Map<String, MutablePoint> locations;

  public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
    this.locations = deepCopy(locations);
  }

  private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
    Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();
    for (String id : m.keySet()) {
      result.put(id, new MutablePoint(m.get(id)));
    }
    return Collections.unmodifiableMap(result);
  }

  public synchronized Map<String, MutablePoint> getLocations() {
    return deepCopy(locations);
  }

  public synchronized MutablePoint getLocation(String id) {
    MutablePoint loc = locations.get(id);
    //				return loc == null ? null : loc;
    return loc == null ? null : new MutablePoint(loc);
  }

  public synchronized void setLocation(String id, int x, int y) {
    MutablePoint loc = locations.get(id);
    loc.x = x;
    loc.y = y;
  }
}

/**
 * Immutable Point class used by DelegatingVehicleTracker
 *
 * @author Brian Goetz and Tim Peierls
 */
class Point {

  public final int x, y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Point{" + "x=" + x + ", y=" + y + '}';
  }
}

/**
 * Delegating thread safety to a ConcurrentHashMap, exposing it as an
 * unmodifiable Map, and using immutable points; version V2A and V2B.
 *
 * @author Brian Goetz and Tim Peierls
 */

/**
 * 把线程安全委托给一个线程安全的类，即ConcurrentMap。
 * 在V1版本中返回的是车辆位置的快照，这里返回的是一个不可修改但实时性很好的车辆位置视图。
 * 举例来说：如果线程A调用getLocations获取unmodifiableMap，这时线程B修改车辆位置信息，但是在A线程中的unmodifiableMap会实时反应这些变化。
 * 也就是说实时更新数据。如果改成defensive copy，那么会提高性能，当然会牺牲实时性。
 *
 * 当然也有缺点：就是可能会导致不一致的车辆位置视图，这个还不太理解。
 */
@SuppressWarnings("unused")
class DelegatingVehicleTracker {

  private final ConcurrentMap<String, Point> locations;
  private final Map<String, Point> unmodifiableMap;

  public DelegatingVehicleTracker(Map<String, Point> points) {
    locations = new ConcurrentHashMap<>(points);
    unmodifiableMap = Collections.unmodifiableMap(locations);
  }

  public Point getLocation(String id) {
    return locations.get(id);
  }

  public void setLocation(String id, int x, int y) {
    if (locations.replace(id, new Point(x, y)) == null) {
      throw new IllegalArgumentException("invalid vehicle name: " + id);
    }
  }

  // First version of getLocations (Listing 4.7) (version V2A)
  public Map<String, Point> getLocations() {
    return unmodifiableMap;
  }

  // Second version of getLocations (Listing 4.8) (version V2B)
  public Map<String, Point> getLocationsSnapshot() {
    return Collections.unmodifiableMap(new HashMap<>(locations));
  }
}

/**
 * @author Brian Goetz and Tim Peierls
 */
@SuppressWarnings("unused")
class SafePoint {

  private int x, y;

  private SafePoint(int[] a) {
    this(a[0], a[1]);
  }

  public SafePoint(SafePoint p) {
    this(p.get());
  }

  public SafePoint(int x, int y) {
    this.set(x, y);
  }

  public synchronized int[] get() {
    return new int[]{x, y};
  }

  public synchronized void set(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

/**
 * Vehicle tracker that safely publishes underlying state; version V3
 *
 * @author Brian Goetz and Tim Peierls
 */

/**
 * 将线程安全委托给ConcurrentHashMap
 * 有点如下：
 * 1、实时性高。
 * 2、getLocations返回一个不可修改的Map，线程安全。
 * 缺点如下：
 * 调用者不能增加或者删除车辆，只能修改车辆位置信息，也就是通过set方法修改萨芬point的值。
 */
@SuppressWarnings("unused")
class PublishingVehicleTracker {

  private final Map<String, SafePoint> locations;
  private final Map<String, SafePoint> unmodifiableMap;

  public PublishingVehicleTracker(Map<String, SafePoint> locations) {
    this.locations = new ConcurrentHashMap<>(locations);
    this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
  }

  public Map<String, SafePoint> getLocations() {
    return unmodifiableMap;
  }

  public SafePoint getLocation(String id) {
    return locations.get(id);
  }

  public void setLocation(String id, int x, int y) {
    locations.get(id).set(x, y);
  }
}