package com.cds.learn.stream;

import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by chendongsheng5 on 2017/5/18.
 * 说明spliterator的用法。
 *
 * reference：
 * https://www.ibm.com/developerworks/cn/java/j-java-streams-3-brian-goetz/
 * https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html?cm_mc_uid=88964479127914847062121&cm_mc_sid_50200000=1495072843
 * http://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.
 *
 */
public class MyArraySpliterator implements Spliterator<Long> {

  private final long[] numbers;
  private int currentPosition, endPosition;

  public MyArraySpliterator(long[] numbers) {
    this(numbers, 0, numbers.length);
  }

  public MyArraySpliterator(long[] numbers, int start, int end) {
    this.numbers = numbers;
    currentPosition = start;
    endPosition = end;
  }

  @Override
  public boolean tryAdvance(Consumer<? super Long> action) {
    if (currentPosition < endPosition) {
      action.accept(numbers[currentPosition++]);
      return true;
    }
    return false;
  }

  @Override
  public long estimateSize() {
    return endPosition - currentPosition;
  }

  /**
   * SIZED：流的大小已知
   * DISTINCT:依据用于对象流的 Object.equals() 或用于原语流的 ==，流的元素将有所不同。
   * SORTED:流的元素按自然顺序排序。
   * ORDERED:流有一个有意义的遇到顺序
   */
  @Override
  public int characteristics() {
    return ORDERED | NONNULL | SIZED | SUBSIZED;
  }

  @Override
  public void forEachRemaining(Consumer<? super Long> action) {
    int pos=currentPosition, end=endPosition;
    currentPosition=end;
    for(;pos<end; pos++) action.accept(numbers[pos]);
  }

  @Override
  public Spliterator<Long> trySplit() {
    if (estimateSize() <= 1000) {
      return null;
    }
    int middle = (endPosition + currentPosition) >>> 1;
    MyArraySpliterator prefix
      = new MyArraySpliterator(numbers, currentPosition, middle);
    currentPosition = middle;
    return prefix;
  }

  public static void main(String[] args) {
    long[] twoThousandNumbers = LongStream.rangeClosed(1, 10_000).toArray();

    Spliterator<Long> spliterator = new MyArraySpliterator(twoThousandNumbers);
    Stream<Long> stream = StreamSupport.stream(spliterator, false);

    System.out.println( sumValues(stream) );
  }

  private static long sumValues(Stream<Long> stream){
    Optional<Long> optional = stream.reduce( ( t, u) ->  t + u );

    return optional.get() != null ? optional.get() : Long.valueOf(0);
  }
}
