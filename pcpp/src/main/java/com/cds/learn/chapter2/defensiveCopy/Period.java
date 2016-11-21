package com.cds.learn.chapter2.defensiveCopy;

import java.util.Date;

public final class Period {
    private final Date start;
    private final Date end;

    private Period(Date start, Date end) {
//        if (start.compareTo(end) > 0) {
//            throw new IllegalArgumentException(start + " after " + end);
//        }
//        this.start = start;
//        this.end = end;
        this.start = new Date(start.getTime());


        this.end = new Date(end.getTime());
        if(this.start.compareTo(this.end) > 0){
            throw new IllegalArgumentException(this.start + " after " + this.end);
        }
    }

    public Date start() {
        return start;
    }

    private Date end() {
        return end;
    }
    //remainder omitted

    public static void main(String[] args) {
        Date start = new Date();
        Date end = new Date();
        Period period = new Period(start, end);
        end.setYear(2);
        System.out.println(period.end());
    }
}
