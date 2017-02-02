package com.cds.learn.chapter5.pipeline;

/**
 * Created by cds on 1/28/17 11:38.
 */
public class UrlProducer implements Runnable {

    private final BlockingQueue<String> output;
    private static final String[] urls =
            {"http://www.itu.dk", "http://www.di.ku.dk", "http://www.miele.de",
                    "http://www.microsoft.com", "http://www.amazon.com", "http://www.dr.dk",
                    "http://www.vg.no", "http://www.tv2.dk", "http://www.google.com",
                    "http://www.ing.dk", "http://www.dtu.dk", "http://www.bbc.co.uk"
            };

    public UrlProducer(BlockingQueue<String> output) {
        this.output = output;
    }

    @Override
    public void run() {

        for (int i = 0; i < urls.length; i++) {
            output.put(urls[i]);
        }

    }
}
