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
	
	private String output;
	private long toSec;
	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}
	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}
	/**
	 * @return the toSec
	 */
	public long getToSec() {
		return toSec;
	}
	/**
	 * @param toSec the toSec to set
	 */
	public void setToSec(long toSec) {
		this.toSec = toSec;
	}
	/**
	 * 
	 */
	private DateUnit(String output,long toSec) {
		this.output = output;
		this.toSec = toSec;
	}

}
