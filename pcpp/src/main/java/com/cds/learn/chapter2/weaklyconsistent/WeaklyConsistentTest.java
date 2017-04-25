package com.cds.learn.chapter2.weaklyconsistent;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>弱一致性测试类</p>
 *
 * fail-fast
 * fail-fast机制在遍历一个集合时，当集合结构被修改，会抛出Concurrent Modification Exception，有两种情况。
 * 1、单线程环境下，集合被创建后，遍历集合的过程中修改了结构，注意remove方法会让expectModcount和modcount相等，
 * 所以不会抛出异常。
 * 2、多线程环境下，当一个线程遍历这个集合，而另一个线程对这个集合的结构进行了修改。
 *
 *注意，迭代器的快速失败行为无法得到保证，因为一般来说，不可能对是否出现不同步并发修改做出任何硬性保证。
 * 快速失败迭代器会尽最大努力抛出 ConcurrentModificationException。因此，为提高这类迭代器的正确性而编写
 * 一个依赖于此异常的程序是错误的做法：迭代器的快速失败行为应该仅用于检测 bug。
 *
 * fail-fast机制如何检测：这个可以通过具体的代码来说明，主要强调modCount这个成员变量的作用。
 * 迭代器在遍历过程中是直接访问内部数据的，因为内部的数据在遍历的过程中无法被修改。为了保证不被修改，迭代器内部
 * 维护了一个标记mode，当集合结构改变(添加删除或者修改)，标记mode会被修改，修改的次数会被记录在modCount中,
 * 迭代器每次的hasNext()和next()方法都会检查该mode是否被改变，当检测到被修改时，抛出Concurrent Modification Exception
 *
 * 用ArrayList类中的checkForComodification函数来说明。
 *
 *
 * fail-safe机制
 * fail-safe：任何对集合的修改都会在一个复制的集合上进行修改，因此不会抛出Concurrent Modification Exception，
 * 但是这个机制有两个问题：
 * 1、需要复制集合，产生大量的无效对象，开销大。
 * 2、无法保证读取的数据是目前原始数据结构中的数据。
 *
 *                                             fail-fast                           fail-safe
 *Throw ConcurrentModification Exception         yes                                no
 *Clone object                                   no                                 yes
 *Memory Overhead                                no                                 yes
 *Example                          HashMap,Vector,ArrayList,HashSet      CopyOnWriteArrayList,ConcurrentHashMap
 *
 *
 */
@SuppressWarnings("unused")
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
//				dequeTest();
//				linkedListTest();
		copyOnWriteTest();
	}
}
