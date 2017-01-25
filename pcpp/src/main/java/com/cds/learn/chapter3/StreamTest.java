package com.cds.learn.chapter3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by cds on 12/12/16 15:13.
 */
public class StreamTest {

	private static void streamOperation() {
		Stream<Integer> s = Stream.of(2, 3, 5);

		//		System.out.println(Arrays.toString(s.filter(x -> x % 2 == 0).toArray()));

		System.out.println(Arrays.toString(s.map(x -> 3 * x).toArray()));

		//		System.out.println(Arrays.toString(s.flatMap(x -> Stream.of(x, x + 1)).toArray()));

		//		System.out.println(s.findAny());

		//		System.out.println(s.reduce(1, (x, y) -> (x * y)).toString());
	}

	private static void parallelTest() {
		System.out.println(IntStream.range(0, 100).filter(x -> x % 2 == 0).count());
		System.out.println(IntStream.range(0, 100).parallel().filter(x -> x % 2 == 0).count());
	}

	private static void createStream1() {
		IntStream is = IntStream.of(2, 3, 5, 7, 11, 13);
		System.out.println("is: " + Arrays.toString(is.toArray()));

		//String[]
		String[] a = { "Hoover", "Roosevelt" };
		Stream<String> presidents = Arrays.stream(a);
		System.out.println("presidents: " + Arrays.toString(presidents.toArray()));

		//collection
		Collection<String> coll = new ArrayList<>();
		coll.add("china");
		Stream countries = coll.stream();
		System.out.println("countries: " + Arrays.toString(countries.toArray()));

		//map
		Map<String, Integer> phoneNumbers = new HashMap<>();
		phoneNumbers.put("a", 123);
		Stream<Map.Entry<String, Integer>> phones = phoneNumbers.entrySet().stream();
		System.out.println(Arrays.toString(phones.toArray()));
	}

	private static void createStream2() {
		System.out.println(Arrays.toString(IntStream.range(0, 10).toArray()));

		//random
		Random random = new Random();
		System.out.println(Arrays.toString(random.ints(10).toArray()));

		//BufferReader
		try {
			URL url = StreamTest.class.getClassLoader().getResource("test.txt");
			assert url != null;
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(url.toURI().getPath())));
			System.out.println(Arrays.toString(bufferedReader.lines().toArray()));
		} catch (FileNotFoundException | URISyntaxException e) {
			e.printStackTrace();
		}

		//BitSet
		BitSet bitSet = new BitSet();
		System.out.println(Arrays.toString(bitSet.stream().toArray()));

	}

	// TODO: 过年后再看看
	private static void createStream3() {

		IntStream nats1 = IntStream.iterate(0, x -> x + 1);
		System.out.println(Arrays.toString(nats1.toArray()));

		IntStream nats2 = IntStream.generate(new IntSupplier() {
			private int next = 0;

			public int getAsInt() {
				return next++;
			}
		});

		final int[] next = { 0 };
		IntStream nats3 = IntStream.generate(() -> next[0]++);
	}

	public static void main(String[] args) {
		//		streamOperation();
		//		parallelTest();
		//		createStream1();
		//		createStream2();
		createStream3();
	}

}
