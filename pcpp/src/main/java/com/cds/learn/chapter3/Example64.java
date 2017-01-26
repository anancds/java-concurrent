package com.cds.learn.chapter3;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p></p>
 *
 * @author chendongsheng5 2017/1/24 10:07
 * @version V1.0
 * @modificationHistory =========================逻辑或功能性重大变更记录
 * @modify by user: chendongsheng5 2017/1/24 10:07
 * @modify by reason:{方法名}:{原因}
 */
public class Example64 {

	public Example64(Integer integer) {

	}

	private static void test1() {
		//		Function<String, Integer>  fsi1 = s -> Integer.parseInt(s);

		//String -> int
		Function<String, Integer> fsi1 = Integer::parseInt;
		System.out.println(fsi1.apply("0090023"));

		//String * int -> String
		BiFunction<String, Integer, String> fsis1 = ((s, i) -> s.substring(i, Math.min(i + 3, s.length())));
		System.out.println(fsis1.apply("abcdef", 0));

		//unit -> String
		Supplier<String> now = () -> new Date().toString();
		System.out.println(now.get());

		//String -> unit
		Consumer<String> show1 = s -> System.out.println(">>>" + s + "<<<");
		show1.accept("abc");

		BiFunction<String,Integer,Character> charat = String::charAt;
		System.out.println(charat.apply("abcdefg", 1));

		Function<String, Integer> parseInt = Integer::parseInt;
		System.out.println(parseInt.apply("0123"));

		//Class and array constructs
		Function<Integer, Example64> makeC = Example64::new;
		Function<Integer, Double[]> make1DArray = Double[]::new;

	}

	public static void main(String[] args) {
		test1();
	}
}
