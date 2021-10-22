package de.framedev.essentialsmini.utils;

public class Cooldown {

	private final int seconds;
	private long secondsLeft;
	private long milliSeconds;
	private final long actualTime;

	public Cooldown(int seconds, long actualTime) {
		this.seconds = seconds;
		this.actualTime = actualTime;
	}
	
	public Cooldown(int seconds) {
		this.seconds = seconds;
		this.actualTime = System.currentTimeMillis();
	}

	public long getSecondsLeft() {
		return secondsLeft;
	}

	public int getSeconds() {
		return seconds;
	}

	public long getMilliSeconds() {
		return milliSeconds;
	}

	public boolean check() {
		secondsLeft = ((actualTime / 1000) + seconds) - (System.currentTimeMillis() / 1000);
		milliSeconds = actualTime + (seconds * 1000L) - System.currentTimeMillis();
		if (secondsLeft > 0) {
			return false;
		}
		return true;
	}

}
