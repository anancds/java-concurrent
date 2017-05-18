package com.cds.learn.chapter9;

import static org.multiverse.api.StmUtils.atomic;
import static org.multiverse.api.StmUtils.newTxnBoolean;
import static org.multiverse.api.StmUtils.retry;

import org.multiverse.api.references.TxnBoolean;

public class TestStmPhilosophersA {

  public static void main(String[] args) {
    final TxnBoolean[] forks =
      {newTxnBoolean(), newTxnBoolean(), newTxnBoolean(), newTxnBoolean(), newTxnBoolean()};
    for (int place = 0; place < forks.length; place++) {
      Thread phil = new Thread(new Philosopher(forks, place));
      phil.start();
    }
  }
}

class Philosopher implements Runnable {

  private final TxnBoolean[] forks;
  private final int place;

  public Philosopher(TxnBoolean[] forks, int place) {
    this.forks = forks;
    this.place = place;
  }

  public void run() {
    while (true) {
      // Take the two forks to the left and the right
      final int left = place, right = (place + 1) % forks.length;
      atomic(() -> {
        // System.out.printf("[%d]", place);
        if (!forks[left].get() && !forks[right].get()) {
          forks[left].set(true);
          forks[right].set(true);
        } else {
          retry();
        }
      });
      System.out.printf("%d ", place);  // Eat
      atomic(() -> {
        forks[left].set(false);
        forks[right].set(false);
      });
      try {
        Thread.sleep(10);
      }         // Think
      catch (InterruptedException exn) {
      }
    }
  }
}
