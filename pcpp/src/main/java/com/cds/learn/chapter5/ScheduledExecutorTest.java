package com.cds.learn.chapter5;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by cds on 12/13/16 11:52.
 */
public class ScheduledExecutorTest implements Runnable{

    private String jobName = "";

    public ScheduledExecutorTest(String jobName) {
        super();
        this.jobName = jobName;
    }

    @Override
    public void run() {
        System.out.println("execute " + jobName);

    }

    public static void main(String[] args) {

        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

        long initialDelay = 1;

        long period = 1;

        service.scheduleAtFixedRate(new ScheduledExecutorTest("job1"), initialDelay, period, TimeUnit.SECONDS);

        long initialDelay2 = 1;

        long delay2 = 2;

        service.scheduleWithFixedDelay(new ScheduledExecutorTest("job2"), initialDelay2, delay2, TimeUnit.SECONDS);


    }
}
