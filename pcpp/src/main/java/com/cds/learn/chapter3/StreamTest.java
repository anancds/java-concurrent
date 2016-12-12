package com.cds.learn.chapter3;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by cds on 12/12/16 15:13.
 *
 */
public class StreamTest {

    public static void main(String[] args) {
        Stream<Integer> s = Stream.of(2, 3, 5);

//        System.out.println(Arrays.toString(s.filter(x -> x % 2 == 0).toArray()));

//        System.out.println(Arrays.toString(s.map(x -> 3 * x).toArray()));

//        System.out.println(Arrays.toString(s.flatMap(x -> Stream.of(x, x + 1)).toArray()));

//        System.out.println(s.findAny());

        System.out.println(s.reduce(1, (x, y) -> (x * y)).toString());
    }

}
