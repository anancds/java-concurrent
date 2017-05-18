package com.cds.learn.chapter9;

import static org.multiverse.api.StmUtils.atomic;
import static org.multiverse.api.StmUtils.newTxnBoolean;

import org.multiverse.api.references.TxnBoolean;

public class TestStmPhilosophersC {

  public static void main(String[] args) {
    final TxnBoolean[] forks =
      {newTxnBoolean(), newTxnBoolean(), newTxnBoolean(), newTxnBoolean(), newTxnBoolean()};
    for (int place = 0; place < forks.length; place++) {
      Thread phil = new Thread(new Philosopher2(forks, place));
      phil.start();
    }
  }
}

class Philosopher2 implements Runnable {

  private final TxnBoolean[] forks;
  private final int place;

  public Philosopher2(TxnBoolean[] forks, int place) {
    this.forks = forks;
    this.place = place;
  }

  public void run() {
    while (true) {
      // Take the two forks to the left and the right
      final int left = place, right = (place + 1) % forks.length;
      atomic(() -> {
        //        System.out.printf("[%d ", place);
        forks[left].await(false);
        forks[left].set(true);
        forks[right].await(false);
        forks[right].set(true);
        System.out.printf("%d ", place);  // Eat
        forks[left].set(false);
        forks[right].set(false);
      });
      // System.out.printf(" %d]", place);
      try {
        Thread.sleep(10);
      }         // Think
      catch (InterruptedException exn) {
      }
    }
  }
}
