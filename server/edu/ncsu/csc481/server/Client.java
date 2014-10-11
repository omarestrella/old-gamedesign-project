package edu.ncsu.csc481.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import edu.ncsu.csc481.events.EventBus;
import edu.ncsu.csc481.events.SimpleEventBus;

public class Client extends Thread {
	private Socket clientSocket;
	
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	private EventBus bus;
	
	private String id;
	
	public Client(String ip, int port, String id) {
		super();
		
		bus = SimpleEventBus.getInstance();
		this.id = id;
		
		try {
			clientSocket = new Socket(ip, port);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the ID set to represent the client
	 */
	public String getClientId() {
		return id;
	}
	
	/**
	 * Override the run method and wait on messages
	 */
	public void run() {
		while(true) {
			try {
				String message = inputStream.readUTF();
				bus.fireEvents(EventBus.TYPE.SERVER_MESSAGE, message);
			} catch (IOException e) {
				//Fail silently for now
				//e.printStackTrace();
			}
		}
	}
	
	public void openConnection() throws IOException {
		outputStream = new DataOutputStream(clientSocket.getOutputStream());
		inputStream = new DataInputStream(clientSocket.getInputStream());
		
		start();
	}
	
	public void closeConnection() {
		try {
			outputStream.close();
			inputStream.close();
			clientSocket.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send a string to the server
	 * @param str the object to send
	 * @throws IOException
	 */
	public void sendMessage(String str) throws IOException {
		outputStream.writeUTF(str);
	}
}
