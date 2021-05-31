/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts �ndern, @Copyright by FrameDev 
 */
package de.framedev.essentialsmini.utils;

/**
 * @author Darryl
 *
 */
public enum DateUnit {
	
	SEC("Second(s)", 1),
	MIN("Minute(s)", 60),
	HOUR("Hour(s)",60*60),
	DAY("Day(s)", 24*60*60),
	WEEK("Week(s)", 7*24*60*60),
	MON("Month(s)",30*24*60*60),
	YEAR("Year(s)",365*24*60*60);
	
	private final String output;
	private final long toSec;

	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @return the toSec
	 */
	public long getToSec() {
		return toSec;
	}
	/**
	 * 
	 */
	private DateUnit(String output,long toSec) {
		this.output = output;
		this.toSec = toSec;
	}

	/**
	 * Convert toSeconds to Milliseconds
	 * @return returns the selected DateUnit to MilliSeconds
	 */
	public long toMillis() {
		return toSec * 1000;
	}

}
