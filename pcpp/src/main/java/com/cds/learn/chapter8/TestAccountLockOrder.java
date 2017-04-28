package com.cds.learn.chapter8;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;

public class TestAccountLockOrder {
  public static void main(String[] args) {
    final Account1 account1 = new Account1(), account2 = new Account1();
    final Random rnd = new Random();
//    final int transfers = 2_000_000;
    final int transfers = 2;
    account1.deposit(3000); account2.deposit(2000);
    Thread clerk1 = new Thread(() -> {
      for (int i=0; i<transfers; i++)
        account1.transferD(account2, rnd.nextInt(10000));
    });
    Thread clerk2 = new Thread(() -> {
      for (int i=0; i<transfers; i++)
        account2.transferD(account1, rnd.nextInt(10000));
    });
    clerk1.start(); clerk2.start();
    // We occasionally print the account balances during the transfer:
    for (int i=0; i<40; i++) {
      try { Thread.sleep(10); } catch (InterruptedException exn) { }
      // Locking both accounts is necessary to avoid reading the
      // balance in the middle of a transfer.
      System.out.println(Account1.balanceSumD(account1, account2));
    }
    // The auditor prints the account balance sum when the clerks are finished:
    try { clerk1.join(); clerk2.join(); } catch (InterruptedException exn) { }
    System.out.println("\nFinal sum is " + Account1.balanceSumD(account1, account2));
  }
}


class Account1 {
  private static final AtomicInteger intSequence = new AtomicInteger();
  private final int serial = intSequence.getAndIncrement();


  private long balance = 0;

  public synchronized void deposit(long amount) {
    balance += amount;
  }

  public synchronized long get() {
    return balance;
  }

  // This is thread-safe and cannot deadlock; takes the locks in the
  // order determined by the Account1 object's serial number.
  public void transferD(Account1 that, final long amount) {
    Account1 ac1 = this, ac2 = that;
    System.out.println("ac1: " + ac1.serial + " ac2: " + ac2.serial);
    if (ac1.serial <= ac2.serial)
      synchronized (ac1) { synchronized (ac2) { // ac1 <= ac2
        ac1.balance = ac1.balance - amount;
        ac2.balance = ac2.balance + amount;
      } }
    else
      synchronized (ac2) { synchronized (ac1) { // ac2 < ac1
        ac1.balance = ac1.balance - amount;
        ac2.balance = ac2.balance + amount;
      } }
  }

  public static long balanceSumD(Account1 ac1, Account1 ac2) {
    if (ac1.serial <= ac2.serial)
      synchronized (ac1) { synchronized (ac2) { // ac1 <= ac2
        return ac1.balance + ac2.balance;
      } }
    else
      synchronized (ac2) { synchronized (ac1) { // ac2 < ac1
        return ac1.balance + ac2.balance;
      } }
  }

  // This is thread-safe but may deadlock; takes the locks in the
  // order determined by the Account1 object's hashCode.  May deadlock
  // in (the rare) case distinct objects get identical hashcodes; this
  // case may be handled using a third lock, as in Goetz p. 209.
  public void transferE(Account1 that, final long amount) {
    Account1 ac1 = this, ac2 = that;
    if (System.identityHashCode(ac1) <= System.identityHashCode(ac2))
      synchronized (ac1) { synchronized (ac2) { // ac1 <= ac2
        ac1.balance = ac1.balance - amount;
        ac2.balance = ac2.balance + amount;
      } }
    else
      synchronized (ac2) { synchronized (ac1) { // ac2 < ac1
        ac1.balance = ac1.balance - amount;
        ac2.balance = ac2.balance + amount;
      } }
  }

  public static long balanceSumE(Account1 ac1, Account1 ac2) {
    if (System.identityHashCode(ac1) <= System.identityHashCode(ac2))
      synchronized (ac1) { synchronized (ac2) { // ac1 <= ac2
        return ac1.balance + ac2.balance;
      } }
    else
      synchronized (ac2) { synchronized (ac1) { // ac2 < ac1
        return ac1.balance + ac2.balance;
      } }
  }

  // Use an abstraction that encapsulates the ordered lock-taking.
  // It works, but is too clever for the ThreadSafe tool, see below.
  public void transferF(final Account1 that, final long amount) {
    final Account1 ac1 = this, ac2 = that;
    lockBothAndRun(ac1, ac2, new Runnable() {
      public void run() {
        ac1.balance = ac1.balance - amount;
        ac2.balance = ac2.balance + amount;
      }
    });
  }

  public static long balanceSumF(Account1 ac1, Account1 ac2) {
    final AtomicLong result = new AtomicLong();
    lockBothAndRun(ac1, ac2, new Runnable() {
      public void run() {
        result.addAndGet(ac1.balance);
        result.addAndGet(ac2.balance);
      }
    });
    return result.longValue();
  }

  // This is elegant but the ThreadSafe tool does not understand it
  public static void lockBothAndRun(Account1 ac1, Account1 ac2, Runnable action) {
    if (ac1.serial <= ac2.serial)
      synchronized (ac1) { synchronized (ac2) { action.run(); } }
    else
      synchronized (ac2) { synchronized (ac1) { action.run(); } }
  }
}
