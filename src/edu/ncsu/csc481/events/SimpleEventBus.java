package edu.ncsu.csc481.events;

import java.util.HashMap;
import java.util.LinkedList;

public class SimpleEventBus implements EventBus {
	private static EventBus instance = null;
	private HashMap<TYPE, LinkedList<Event>> listeners;
	
	private SimpleEventBus() {
		listeners = new HashMap<TYPE, LinkedList<Event>>();
	}
	
	/**
	 * Singleton pattern for the event bus
	 * @return the instance of the event bus
	 */
	public static EventBus getInstance() {
		if(instance == null) {
			instance = new SimpleEventBus();
		}
		
		return instance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.ncsu.csc481.events.EventBus#fireEvents(edu.ncsu.csc481.events.EventBus.TYPE, java.lang.String)
	 */
	public void fireEvents(TYPE type, String data) throws IllegalArgumentException {
		if(listeners.containsKey(type)) {
			LinkedList<Event> list = listeners.get(type);
			
			for(Event e : list) {
				e.run(data);
			}
		} else {
			throw new IllegalArgumentException("No events are registered with that type");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.ncsu.csc481.events.EventBus#addEventListener(edu.ncsu.csc481.events.EventBus.TYPE, edu.ncsu.csc481.events.Event)
	 */
	public void addEventListener(TYPE type, Event e) {
		if(listeners.containsKey(type)) {
			listeners.get(type).add(e);
		} else {
			LinkedList<Event> list = new LinkedList<Event>();
			list.add(e);
			
			listeners.put(type, list);
		}
	}
}
