package me.offeex.bloomware.api.event.events;

import me.offeex.bloomware.api.event.Event;

public class EventKeyRelease extends Event {
	private final int key;
	private final int scanCode;

	public EventKeyRelease(int key, int scanCode) {
		this.key = key;
		this.scanCode = scanCode;
	}

	public int getKey() {
		return key;
	}

	public int getScanCode() {
		return scanCode;
	}
}
