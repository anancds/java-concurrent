package com.cds.learn.chapter2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p></p>
 *
 * @author chendongsheng5 2017/1/18 11:16
 * @version V1.0
 * @modificationHistory =========================逻辑或功能性重大变更记录
 * @modify by user: chendongsheng5 2017/1/18 11:16
 * @modify by reason:{方法名}:{原因}
 */
public class TestVehicleTracker {

	private static void testMonitorVehicleTracker() {
		MutablePoint point = new MutablePoint();
		point.x = 3;
		point.y = 4;
		Map<String, MutablePoint> map = new HashMap<>();
		map.put("#1", point);

		MonitorVehicleTracker monitorVehicleTracker = new MonitorVehicleTracker(map);
		System.out.println(monitorVehicleTracker.getLocations());
		System.out.println(monitorVehicleTracker.getLocation("#1"));

		monitorVehicleTracker.setLocation("#1", 4, 5);
		System.out.println(monitorVehicleTracker.getLocations());
		System.out.println(monitorVehicleTracker.getLocation("#1"));
	}

	public static void main(String[] args) {

		testMonitorVehicleTracker();

	}
}

class MutablePoint {
	public int x, y;

	public MutablePoint() {
		x = 0;
		y = 0;
	}

	public MutablePoint(MutablePoint p) {
		this.x = p.x;
		this.y = p.y;
	}

	@Override public String toString() {
		return "MutablePoint{" + "x=" + x + ", y=" + y + '}';
	}
}

/**
 * Monitor-based vehicle tracker implementation; version V1.
 *
 * @author Brian Goetz and Tim Peierls
 */

class MonitorVehicleTracker {
	private final Map<String, MutablePoint> locations;

	public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
		this.locations = deepCopy(locations);
	}

	private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
		Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();
		for (String id : m.keySet())
			result.put(id, new MutablePoint(m.get(id)));
		return Collections.unmodifiableMap(result);
	}

	public synchronized Map<String, MutablePoint> getLocations() {
		return deepCopy(locations);
	}

	public synchronized MutablePoint getLocation(String id) {
		MutablePoint loc = locations.get(id);
		return loc == null ? null : new MutablePoint(loc);
	}

	public synchronized void setLocation(String id, int x, int y) {
		MutablePoint loc = locations.get(id);
		loc.x = x;
		loc.y = y;
	}
}

/**
 * Immutable Point class used by DelegatingVehicleTracker
 *
 * @author Brian Goetz and Tim Peierls
 */
class Point {
	public final int x, y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

/**
 * Delegating thread safety to a ConcurrentHashMap, exposing it as an
 * unmodifiable Map, and using immutable points; version V2A and V2B.
 *
 * @author Brian Goetz and Tim Peierls
 */

class DelegatingVehicleTracker {
	private final ConcurrentMap<String, Point> locations;
	private final Map<String, Point> unmodifiableMap;

	public DelegatingVehicleTracker(Map<String, Point> points) {
		locations = new ConcurrentHashMap<String, Point>(points);
		unmodifiableMap = Collections.unmodifiableMap(locations);
	}

	public Point getLocation(String id) {
		return locations.get(id);
	}

	public void setLocation(String id, int x, int y) {
		if (locations.replace(id, new Point(x, y)) == null)
			throw new IllegalArgumentException("invalid vehicle name: " + id);
	}

	// First version of getLocations (Listing 4.7) (version V2A)
	public Map<String, Point> getLocations() {
		return unmodifiableMap;
	}

	// Second version of getLocations (Listing 4.8) (version V2B)
	public Map<String, Point> getLocationsSnapshot() {
		return Collections.unmodifiableMap(new HashMap<String, Point>(locations));
	}
}

/**
 * @author Brian Goetz and Tim Peierls
 */
class SafePoint {
	private int x, y;

	private SafePoint(int[] a) {
		this(a[0], a[1]);
	}

	public SafePoint(SafePoint p) {
		this(p.get());
	}

	public SafePoint(int x, int y) {
		this.set(x, y);
	}

	public synchronized int[] get() {
		return new int[] { x, y };
	}

	public synchronized void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

/**
 * Vehicle tracker that safely publishes underlying state; version V3
 *
 * @author Brian Goetz and Tim Peierls
 */
class PublishingVehicleTracker {
	private final Map<String, SafePoint> locations;
	private final Map<String, SafePoint> unmodifiableMap;

	public PublishingVehicleTracker(Map<String, SafePoint> locations) {
		this.locations = new ConcurrentHashMap<String, SafePoint>(locations);
		this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
	}

	public Map<String, SafePoint> getLocations() {
		return unmodifiableMap;
	}

	public SafePoint getLocation(String id) {
		return locations.get(id);
	}

	public void setLocation(String id, int x, int y) {
		locations.get(id).set(x, y);
	}
}