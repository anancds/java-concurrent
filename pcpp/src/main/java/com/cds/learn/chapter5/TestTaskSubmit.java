package com.cds.learn.chapter5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.*;

/**
 * Created by cds on 12/13/16 09:52.
 */
public class TestTaskSubmit {

    private static final ExecutorService executor = Executors.newWorkStealingPool();

    public static void main(String[] args) {

        testRunnableTask();
        testCallableTask();
    }

    private static void testRunnableTask() {

        Future<?> fut = executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task run!");
            }
        });

        try {
            fut.get();
        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void testRunnableLambdaTask() {

        Future<?> fut = executor.submit(() -> System.out.println("Task run!"));

        try {
            fut.get();
        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPage(String url, int maxLines) throws IOException {
        // This will close the streams after use (JLS 8 para 14.20.3):
        try (BufferedReader in
                     = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < maxLines; i++) {
                String inputLine = in.readLine();
                if (inputLine == null)
                    break;
                else
                    sb.append(inputLine).append("\n");
            }
            return sb.toString();
        }
    }

    private static void testCallableTask() {
        Future<String> fut
                = executor.submit(new Callable<String>() {
            public String call() throws IOException {
                return getPage("http://www.baidu.com", 10);
            }});
        try {
            String webpage = fut.get();
            System.out.println(webpage);
        } catch (InterruptedException exn) { System.out.println(exn); }
        catch (ExecutionException exn) { throw new RuntimeException(exn); }
    }

    private static void testCallableLambdaTask() {
        Future<String> fut
                = executor.submit(() -> {
            return getPage("http://www.wikipedia.org", 10);
        });
        try {
            String webpage = fut.get();
            System.out.println(webpage);
        } catch (InterruptedException exn) { System.out.println(exn); }
        catch (ExecutionException exn) { throw new RuntimeException(exn); }
    }

}
