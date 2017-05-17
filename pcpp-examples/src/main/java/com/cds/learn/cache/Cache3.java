package com.cds.learn.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chendongsheng5 on 2017/5/17.
 * 这个例子用来说明伪共享(false sharing),还没有成功说明，不知道问题在哪？
 *
 * 在多核机器上，缓存遇到了另一个问题——一致性。不同的处理器拥有完全或部分分离的缓存。
 * 当一个处理器改变了属于它自己缓存中的一个值，其它处理器就再也无法使用它自己原来的值，
 * 因为其对应的内存位置将被刷新(invalidate)到所有缓存。而且由于缓存操作是以缓存行而不是字节为粒度，
 * 所有缓存中整个缓存行将被刷新！
 *
 * 先传入1,2,3,4，再改成16,32,48,64
 * 第一个例子中的四个值很可能在同一个缓存行里，每次一个处理器增加计数，这四个计数所在的缓存行将被刷新，
 * 而其它处理器在下一次访问它们各自的计数,将失去命中(miss)一个缓存。
 * 这种多线程行为有效地禁止了缓存功能，削弱了程序性能。
 */
public class Cache3 {

  private static int[] s_counter = new int[1024];

  private static int UpdateCounter(int position) {
    for (int j = 0; j < 1000000000; j++) {
      s_counter[position] = s_counter[position] + 3;
    }
    return position;
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    ExecutorService executorService = Executors.newFixedThreadPool(4);
    AtomicInteger integer = new AtomicInteger(0);
//    AtomicInteger integer = new AtomicInteger(16);
    List<Future<Integer>> futures = new ArrayList<>();
    int result = 0;

    long start1 = System.currentTimeMillis();
    for (int i = 0; i < 4; i++) {
      futures.add(executorService.submit(() -> UpdateCounter(integer.getAndAdd(1))));
    }
    for (Future<Integer> future : futures) {
      result += future.get();
    }
    executorService.shutdown();
    long start2 = System.currentTimeMillis();

    System.out.println("cost time: " + (start2 - start1) + "ms");
    System.out.println("result: " + result);
  }
}
