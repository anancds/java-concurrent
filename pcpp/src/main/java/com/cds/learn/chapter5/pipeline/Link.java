package com.cds.learn.chapter5.pipeline;

/**
 * Created by cds on 1/28/17 11:48.
 */
public class Link {

    public final String from, to;

    public Link(String from, String to) {
        this.from = from;
        this.to = to;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (!from.equals(link.from)) return false;
        return to.equals(link.to);

    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}

