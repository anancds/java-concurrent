package com.cds.learn.threadpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by chendongsheng5 on 2017/5/17.
 * 说明fork join用法的例子。就是一个分治算法，相当于一个单机版本的map reduce。
 *
 * reference:
 * http://ifeve.com/a-java-fork-join-framework-3/
 * http://ifeve.com/a-java-fork-join-framework-3-2/
 *
 * RecursiveAction，用于没有返回结果的任务
 * RecursiveTask，用于有返回值的任务
 */
public class ForkJoinTest extends RecursiveTask<Integer> {

  private static final int threshold = 2;
  private static final long serialVersionUID = -3611254198265061729L;
  private int start;
  private int end;

  private ForkJoinTest(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public static void main(String[] args) {
    ForkJoinPool forkjoinPool = new ForkJoinPool();

    //生成一个计算任务，计算1+2+3+4
    ForkJoinTest task = new ForkJoinTest(1, 100);

    //执行一个任务
    Future<Integer> result = forkjoinPool.submit(task);

    try {
      System.out.println(result.get());
    } catch (Exception e) {
      //noinspection ThrowablePrintedToSystemOut
      System.out.println(e);
    }
  }

  @Override
  protected Integer compute() {
    int sum = 0;

    //如果任务足够小就计算任务
    boolean canCompute = (end - start) <= threshold;
    if (canCompute) {
      for (int i = start; i <= end; i++) {
        sum += i;
      }
    } else {
      // 如果任务大于阈值，就分裂成两个子任务计算
      int middle = (start + end) / 2;
      ForkJoinTest leftTask = new ForkJoinTest(start, middle);
      ForkJoinTest rightTask = new ForkJoinTest(middle + 1, end);

      // 执行子任务
      leftTask.fork();
      rightTask.fork();

      //等待任务执行结束合并其结果
      int leftResult = leftTask.join();
      int rightResult = rightTask.join();

      //合并子任务
      sum = leftResult + rightResult;

    }

    return sum;
  }

}
