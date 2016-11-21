package com.cds.learn.chapter2.defensiveCopy;

import java.util.Date;

public class Person {
    private String name;
    private Date birth;

    public Person(String name, Date birth) {
        this.name = name;
        this.birth = birth;
    }

    public Date getBirth() {
        return new Date(birth.getTime()); //保护性拷贝Defensive copying
//        return birth;
    }

    public String getName() {
        return name;
    }


    public static void main(String[] args) {

        Person p = new Person("Benson",new Date(1990, 4, 13));
        System.out.println(p.getName());
        System.out.println(p.getBirth().getYear());

        Date hole = p.getBirth();
        hole.setYear(2013);

        System.out.println(p.getName());
        System.out.println(p.getBirth().getYear());
    }
}
