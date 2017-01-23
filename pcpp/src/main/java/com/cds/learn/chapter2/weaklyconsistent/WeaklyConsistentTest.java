package com.cds.learn.chapter2.weaklyconsistent;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>弱一致性测试类</p>
 *
 * @author chendongsheng5 2017/1/23 14:17
 * @version V1.0
 * @modificationHistory =========================逻辑或功能性重大变更记录
 * @modify by user: chendongsheng5 2017/1/23 14:17
 * @modify by reason:{方法名}:{原因}
 */
public class WeaklyConsistentTest {

	private static void dequeTest() {

		//weakly consistent
		List<String> input = Arrays.asList("a", "b", "c", "d", "e");
		List<String> output = new ArrayList<>();

		Deque<String> deque = new ConcurrentLinkedDeque<>(input);
		for (String s : deque) {
			output.add(s);
			if (s.equals("c")) {
				deque.addFirst("XXX");
				deque.removeLast();
			}
		}

		System.out.println(output);
	}

	private static void linkedListTest() {
		//fail-fast
		List<String> input = new LinkedList<>(Arrays.asList("a", "b", "c", "d", "e"));
		List<String> output = new ArrayList<>();

		for (String s : input) {
			output.add(s);
			input.remove(4);
		}

		System.out.println(output);
	}

	private static void copyOnWriteTest() {

		// fail-safe
		List<String> input = new CopyOnWriteArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
		List<String> output = new ArrayList<>();

		for (String s : input) {
			output.add(s);
			input.remove(4);
		}

		System.out.println(output);
	}

	public static void main(String[] args) {
		//		dequeTest();
		//		linkedListTest();
		copyOnWriteTest();
	}
}
