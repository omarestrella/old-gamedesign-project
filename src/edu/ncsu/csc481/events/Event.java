package edu.ncsu.csc481.events;

public interface Event {
	/**
	 * Run an event
	 * @param data the data passed to the event
	 */
	public void run(String data);
}
