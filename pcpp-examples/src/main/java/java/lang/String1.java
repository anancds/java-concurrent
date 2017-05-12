package java.lang;

/**
 * Created by chendongsheng5 on 2017/5/11.
 * 想要伪造一个String，为了说明双亲委托这个机制。
 * 双亲委托的意思就是：每次加载一个类，先获取一个系统加载器AppClassLoader的实例，（ClassLoader.getSystemClassLoader()）
 * 然后向上级请求，由最上级优先去加载，如果上级觉得这些类不属于核心类，就可以下方到子级去自行加载。
 *                               BootstrapClassLoader
 *                               ExtClassLoader
 *                               AppClassLoader
 *                               自定义ClassLoader
 */
public class String1 {

  public static void main(String[] args) {
    System.out.println("Aa".hashCode());
    System.out.println("BB".hashCode());
  }

}
