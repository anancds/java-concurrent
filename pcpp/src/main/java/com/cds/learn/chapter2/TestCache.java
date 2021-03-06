package com.cds.learn.chapter2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

// Interface that represents a function from A to V
interface Computable<A, V> {

  V compute(A arg) throws InterruptedException;
}


public class TestCache {

  public static void main(String[] args) throws InterruptedException {
    Computable<Long, long[]> factorizer = new Factorizer(),
        cachingFactorizer = new Memoizer1<>(factorizer);
    // cachingFactorizer = factorizer;

    long p = 71827636563813227L;

    print(factorizer.compute(p));

    long[] factors = cachingFactorizer.compute(p);
    print(factors);

    print(cachingFactorizer.compute(p));
    print(cachingFactorizer.compute(p));
    print(cachingFactorizer.compute(p));
    print(cachingFactorizer.compute(p));
    print(cachingFactorizer.compute(p));
    print(cachingFactorizer.compute(p));
    print(cachingFactorizer.compute(p));
  }

  private static void print(long[] arr) {
    for (long x : arr) {
      System.out.print(" " + x);
    }
    System.out.println();
  }
}

// Prime factorization is a function from Long to long[]
class Factorizer implements Computable<Long, long[]> {

  // For statistics only, count number of calls to factorizer:
  private final AtomicLong count = new AtomicLong(0);

  public long getCount() {
    return count.longValue();
  }

  public long[] compute(Long wrappedP) {
    count.getAndIncrement();
    long p = wrappedP;
    ArrayList<Long> factors = new ArrayList<>();
    long k = 2;
    while (p >= k * k) {
      if (p % k == 0) {
        factors.add(k);
        p /= k;
      } else {
        k++;
      }
    }
    // Now k * k > p and no number in 2..k divides p
    factors.add(p);
    long[] result = new long[factors.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = factors.get(i);
    }
    return result;
  }
}


/**
 * Initial cache attempt using HashMap and synchronization;
 * suffers from lack of parallelism due to coarse locking.
 * From Goetz p. 103
 *
 * @author Brian Goetz and Tim Peierls
 */
class Memoizer1<A, V> implements Computable<A, V> {

  private final Map<A, V> cache;
  private final Computable<A, V> c;

  public Memoizer1(Computable<A, V> c) {
    this.c = c;
    cache = new HashMap<>();
  }

  public synchronized V compute(A arg) throws InterruptedException {
    V result = cache.get(arg);
    if (result == null) {
      result = c.compute(arg);
      cache.put(arg, result);
    }
    return result;
  }
}


/**
 * Memoizer2
 * Replacing HashMap with ConcurrentHashMap for better parallelism.
 * From Goetz p. 105
 *
 * @author Brian Goetz and Tim Peierls
 */
class Memoizer2<A, V> implements Computable<A, V> {

  private final Map<A, V> cache = new ConcurrentHashMap<>();
  private final Computable<A, V> c;

  public Memoizer2(Computable<A, V> c) {
    this.c = c;
  }

  public V compute(A arg) throws InterruptedException {
    V result = cache.get(arg);
    if (result == null) {
      result = c.compute(arg);
      cache.put(arg, result);
    }
    return result;
  }
}


/**
 * Memoizer3
 * Create a Future and register in cache immediately.
 * Calls: ft.run() -> eval.call() -> c.compute(arg)
 * From Goetz p. 106
 *
 * @author Brian Goetz and Tim Peierls
 */
class Memoizer3<A, V> implements Computable<A, V> {

  private final Map<A, Future<V>> cache
      = new ConcurrentHashMap<>();
  private final Computable<A, V> c;

  public Memoizer3(Computable<A, V> c) {
    this.c = c;
  }

  public static RuntimeException launderThrowable(Throwable t) {
    if (t instanceof RuntimeException) {
      return (RuntimeException) t;
    } else if (t instanceof Error) {
      throw (Error) t;
    } else {
      throw new IllegalStateException("Not unchecked", t);
    }
  }

  public V compute(final A arg) throws InterruptedException {
    Future<V> f = cache.get(arg);
    if (f == null) {
      Callable<V> eval = new Callable<V>() {
        public V call() throws InterruptedException {
          return c.compute(arg);
        }
      };
      FutureTask<V> ft = new FutureTask<V>(eval);
      cache.put(arg, ft);
      f = ft;
      ft.run();
    }
    try {
      return f.get();
    } catch (ExecutionException e) {
      throw launderThrowable(e.getCause());
    }
  }
}


/**
 * Memoizer4, hybrid of variant of Goetz's Memoizer3 and Memoizer.  If
 * arg not in cache, create Future, then atomically putIfAbsent in
 * cache, then run on calling thread.
 */

class Memoizer4<A, V> implements Computable<A, V> {

  private final Map<A, Future<V>> cache
      = new ConcurrentHashMap<>();
  private final Computable<A, V> c;

  public Memoizer4(Computable<A, V> c) {
    this.c = c;
  }

  public static RuntimeException launderThrowable(Throwable t) {
    if (t instanceof RuntimeException) {
      return (RuntimeException) t;
    } else if (t instanceof Error) {
      throw (Error) t;
    } else {
      throw new IllegalStateException("Not unchecked", t);
    }
  }

  public V compute(final A arg) throws InterruptedException {
    Future<V> f = cache.get(arg);

    //这个null是把所有非同时竞争的线程排除掉。
    if (f == null) {
      Callable<V> eval = new Callable<V>() {
        public V call() throws InterruptedException {
          return c.compute(arg);
        }
      };
      FutureTask<V> ft = new FutureTask<>(eval);
      f = cache.putIfAbsent(arg, ft);
      if (f == null) {
        f = ft;
        ft.run();
      }
    }
    try {
      return f.get();
    } catch (ExecutionException e) {
      throw launderThrowable(e.getCause());
    }
  }
}

/**
 * Memoizer5, modern variant of Memoizer4 using the new Java 8
 * computeIfAbsent.  Atomically test whether arg is in cache and if
 * not create Future and put it there, then run on calling thread.
 */

class Memoizer5<A, V> implements Computable<A, V> {

  private final Map<A, Future<V>> cache
      = new ConcurrentHashMap<>();
  private final Computable<A, V> c;

  public Memoizer5(Computable<A, V> c) {
    this.c = c;
  }

  public static RuntimeException launderThrowable(Throwable t) {
    if (t instanceof RuntimeException) {
      return (RuntimeException) t;
    } else if (t instanceof Error) {
      throw (Error) t;
    } else {
      throw new IllegalStateException("Not unchecked", t);
    }
  }

  public V compute(final A arg) throws InterruptedException {
    // AtomicReference is used as a simple assignable holder; no atomicity needed，对frt原子操作
    final AtomicReference<FutureTask<V>> ftr = new AtomicReference<>();
    Future<V> f = cache.computeIfAbsent(arg, (A argv) -> {
      Callable<V> eval = new Callable<V>() {
        public V call() throws InterruptedException {
          return c.compute(argv);
        }
      };
      ftr.set(new FutureTask<>(eval));
      return ftr.get();
    });
    // Important to run() the future outside the computeIfAbsent():
    if (ftr.get() != null) {
      ftr.get().run();
    }
    try {
      return f.get();
    } catch (ExecutionException e) {
      throw launderThrowable(e.getCause());
    }
  }
}


/**
 * Final implementation of Memoizer using cheap get() followed by
 * atomic putIfAbsent.
 * From Goetz p. 108
 *
 * @author Brian Goetz and Tim Peierls
 */
class Memoizer<A, V> implements Computable<A, V> {

  private final ConcurrentMap<A, Future<V>> cache
      = new ConcurrentHashMap<>();
  private final Computable<A, V> c;

  public Memoizer(Computable<A, V> c) {
    this.c = c;
  }

  /**
   * Coerce a checked Throwable to an unchecked RuntimeException.
   *
   * sestoft@itu.dk 2014-09-07: This method converts a Throwable
   * (which is a checked exception) into a RuntimeException (which is
   * an unchecked exception) or an IllegalStateException (which is a
   * subclass of RuntimeException and hence unchecked).  It is unclear
   * why RuntimeException and Error are treated differently; both are
   * unchecked.  A simpler (but grosser) approach is to simply throw a
   * new RuntimeException(t), thus wrapping the Throwable, but that
   * may lead to a RuntimeException containing a RuntimeException
   * which is a little strange.  The original
   * java.util.concurrent.ExecutionException that wrapped the
   * Throwable is itself checked and therefore needs to be caught and
   * turned into something less obnoxious.
   *
   */

  public static RuntimeException launderThrowable(Throwable t) {
    if (t instanceof RuntimeException) {
      return (RuntimeException) t;
    } else if (t instanceof Error) {
      throw (Error) t;
    } else {
      throw new IllegalStateException("Not unchecked", t);
    }
  }

  public V compute(final A arg) throws InterruptedException {
    while (true) {
      Future<V> f = cache.get(arg);
      if (f == null) {
        Callable<V> eval = new Callable<V>() {
          public V call() throws InterruptedException {
            return c.compute(arg);
          }
        };
        FutureTask<V> ft = new FutureTask<>(eval);
        f = cache.putIfAbsent(arg, ft);
        if (f == null) {
          f = ft;
          ft.run();
        }
      }
      try {
        return f.get();
      } catch (CancellationException e) {
        cache.remove(arg, f);
      } catch (ExecutionException e) {
        throw launderThrowable(e.getCause());
      }
    }
  }
}
