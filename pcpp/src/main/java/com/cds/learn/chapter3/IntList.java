package com.cds.learn.chapter3;

import java.util.BitSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <p></p>
 *
 * @author chendongsheng5 2017/1/25 16:34
 * @version V1.0
 * @modificationHistory =========================逻辑或功能性重大变更记录
 * @modify by user: chendongsheng5 2017/1/25 16:34
 * @modify by reason:{方法名}:{原因}
 */
public class IntList {

	public final int item;
	public final IntList next;

	public IntList(int item, IntList next) {
		this.item = item;
		this.next = next;
	}

	public static IntStream stream(IntList xs) {
		IntStream.Builder sb = IntStream.builder();
		while (xs != null) {
			sb.accept(xs.item);
			xs = xs.next;
		}
		return sb.build();
	}

	public static Stream<IntList> perms(BitSet todo, IntList tail) {
//		if (todo.isEmpty())
			return Stream.of(tail);
		//		else
		//			return todo.stream().boxed()
		//					.flatMap(r -> perms(minus(todo, r), new IntList(r, tail)));
	}

	public static Stream<IntList> perms(int n) {
		BitSet todo = new BitSet(n);
		todo.flip(0, n);
		return perms(todo, null);
	}
}
