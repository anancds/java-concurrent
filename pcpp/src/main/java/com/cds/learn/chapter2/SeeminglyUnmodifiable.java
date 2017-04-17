package com.cds.learn.chapter2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个例子用来解释Collections.unmodifiableMap返回的是一个不可修改的Map
 * 不可修改的意思是Map中的对象地址不可修改，里面的对象若支持修改的话，其实也是可以修改的。
 */
public class SeeminglyUnmodifiable {
	private Map<String, Point1> startingLocations = new HashMap<>(3);

	public SeeminglyUnmodifiable() {
		//创建对象就是创建一个地址，这个地址不可改变，原理跟final差不多。
		startingLocations.put("LeftRook", new Point1(1, 1));
		startingLocations.put("LeftKnight", new Point1(1, 2));
		startingLocations.put("LeftCamel", new Point1(1, 3));
	}

	public static void main(String[] args) {
		SeeminglyUnmodifiable pieceLocations = new SeeminglyUnmodifiable();
		Map<String, Point1> locations = pieceLocations.getStartingLocations();

		Point1 camelLoc = locations.get("LeftCamel");
		System.out.println("The LeftCamel's start is at [ " + camelLoc.getX() + ", " + camelLoc.getY() + " ]");

		try {
			locations.put("LeftCamel", new Point1(0, 0));
		} catch (java.lang.UnsupportedOperationException e) {
			System.out.println("Try 1 - Could not update the map!");
		}

		camelLoc.setLocation(0, 0);

		Point1 newCamelLoc = pieceLocations.getStartingLocations().get("LeftCamel");
		System.out.println(
				"Try 2 - Map updated! The LeftCamel's start is now at [ " + newCamelLoc.getX() + ", " + newCamelLoc
						.getY() + " ]");
	}

	public Map<String, Point1> getStartingLocations() {
		return Collections.unmodifiableMap(startingLocations);
		//		return startingLocations;
	}
}

class Point1 {
	public float x;
	public float y;

	public Point1(float x, float y) {
		setLocation(x, y);
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
