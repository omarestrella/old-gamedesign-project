package edu.ncsu.csc481.events;

public interface EventBus {	
	public static enum TYPE {
		GENERAL_LOGGING, //Typical logging of events
		REPLAY_LOGGING, //Logging to the replay file
		
		SERVER_MESSAGE, //Message from the server to clients
		CLIENT_MESSAGE, //Message from a client to the server
		
		PLAYER_CREATED, //Created a player
		PLAYER_MOVED, //Player moving
		PLAYER_DEATH; //Player died
	};
	
	/**
	 * Add an event that listens when its type is fired
	 * @param type the type of the event
	 * @param e
	 */
	public void addEventListener(TYPE type, Event e);
	
	/**
	 * Fire all events of a given type
	 * @param type the type of event being fired off
	 * @param data data to pass to the event
	 */
	public void fireEvents(TYPE type, String data);
}
