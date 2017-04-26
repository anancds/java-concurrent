package com.cds.learn.cache;

/**
 * Created by chendongsheng5 on 2017/4/25.
 *
 * reference:
 * 1、http://coolshell.cn/articles/3236.html
 * 2、http://igoro.com/archive/gallery-of-processor-cache-effects/
 * 3、http://blog.csdn.net/jiary5201314/article/details/44676425
 *
 * 这个例子用来说明内存访问和运行，通过coreinfo来解释下本机器的cache，一共有L1、L2、L3，四核处理器，
 * L1数据和指令各一个。
 *
 * 两个循环花费相同时间的原因跟内存有关。循环执行时间长短由数组的内存访问次数决定的，而非整型数的乘法运算次数。
 * 经过下面对第二个示例的解释，你会发现硬件对这两个循环的主存访问次数是相同的。
 *
 * 注意当步长在1到16范围内，循环运行时间几乎不变。但从16开始，每次步长加倍，运行时间减半。
 * 背后的原因是今天的CPU不再是按字节访问内存，而是以64字节为单位的块(chunk)拿取，称为一个缓存行(cache line)。当你读一个特定的内存地址，整个缓存行将从主存换入缓存，并且访问同一个缓存行内的其它值的开销是很小的。

 由于16个整型数占用64字节（一个缓存行），for循环步长在1到16之间必定接触到相同数目的缓存行：即数组中所有的缓存行。当步长为32，我们只有大约每两个缓存行接触一次，当步长为64，只有每四个接触一次。

 理解缓存行对某些类型的程序优化而言可能很重要。比如，数据字节对齐可能决定一次操作接触1个还是2个缓存行。那上面的例子来说，很显然操作不对齐的数据将损失一半性能。

 示例3：L1和L2缓存大小
 */
public class Cache1 {

  private int[] arr = new int[256 * 1024];

  public static void main(String[] args) {

    Cache1 cache1 = new Cache1();
//    cache1.Loop1();
//    cache1.Loop2();
    cache1.L1L2Cache();
  }

  private void Loop1() {
    long start = System.currentTimeMillis();
// Loop 1
    for (int i = 0; i < arr.length; i++) {
      arr[i] *= 3;
    }

    System.out.println("Loop1 cost time: " + (System.currentTimeMillis() - start) + "ms");
  }

  private void Loop2() {
    long start = System.currentTimeMillis();
// Loop 2，修改步长，从1到16，再32,、64等
    for (int i = 0; i < arr.length; i += 8) {
      arr[i] *= 3;
    }

    System.out.println("Loop2 cost time: " + (System.currentTimeMillis() - start) + "ms");
  }


  /**
   * 在我的机器上，CoreInfo现实我有一个32KB的L1数据缓存，一个32KB的L1指令缓存，还有一个256KB大小L2数据缓存。
   * 还有一个8M的L3缓存
   *
   * L1缓存是处理器独享的，L2缓存是成对处理器共享的，也就是L1的指令缓存和数据缓存共用一个L2缓存。
   */
  private void L1L2Cache() {

    long start = System.currentTimeMillis();
    //改变arr大小1-16,32,64,128,256，
    // 8 * 1024 * 4就是32KB，64 * 1024* 4就是256KB， 2 * 1024 * 1024 * 4就是8M，用来说明我的电脑的L1是32KB，L2是256K,L3是8M。
    int steps = 1024 * 1024 * 1024;
// Arbitrary number of steps
    int lengthMod = arr.length - 1;
    for (int i = 0; i < steps; i++) {
      arr[(i * 16) & lengthMod]++; // (x & lengthMod) is equal to (x % arr.Length)
    }

    System.out.println("L1L2Cache cost time: " + (System.currentTimeMillis() - start) + "ms");
  }
}
