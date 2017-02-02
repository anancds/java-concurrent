package com.cds.learn.chapter5.pipeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by cds on 1/28/17 11:40.
 */
public class PageGetter implements Runnable {

    private final BlockingQueue<String> input;
    private final BlockingQueue<Webpage> output;

    public PageGetter(BlockingQueue<String> input, BlockingQueue<Webpage> output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        while (true) {
            String url = input.take();
            //      System.out.println("PageGetter: " + url);
            try {
                String contents = getPage(url, 200);
                output.put(new Webpage(url, contents));
            } catch (IOException exn) { System.out.println(exn); }
        }
    }

    public static String getPage(String url, int maxLines) throws IOException {
        // This will close the streams after use (JLS 8 para 14.20.3):
        try (BufferedReader in
                     = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<maxLines; i++) {
                String inputLine = in.readLine();
                if (inputLine == null)
                    break;
                else
                    sb.append(inputLine).append("\n");
            }
            return sb.toString();
        }
    }
}
