package com.jeasonfire.galaxies.util;

import java.util.HashMap;

public class Timer {
	private static HashMap<String, Long> timers = new HashMap<String, Long>();

	private Timer() {
	}

	public static void startTimer(String name) {
		timers.put(name, System.currentTimeMillis());
	}

	public static void removeTimer(String name) {
		timers.remove(name);
	}

	public static boolean timerExists(String name) {
		return timers.containsKey(name);
	}

	public static long getTime(String name) {
		return System.currentTimeMillis()
				- (timers.containsKey(name) ? timers.get(name) : 0);
	}
}
