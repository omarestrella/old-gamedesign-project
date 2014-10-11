package edu.ncsu.csc481.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
	private Server server;
	private Socket clientSocket;
	
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	public ServerThread(Server server, Socket clientSocket) {
		super();

		this.server = server;
		this.clientSocket = clientSocket;
	}
	
	/**
	 * Get the socket for a client
	 * @return the socket for a client
	 */
	public Socket getSocket() {
		return clientSocket;
	}
	
	/**
	 * Override the run method and wait on messages
	 */
	public void run() {
		while(true) {
			try {
				//System.out.println((String) inputStream.readUTF());
				server.sendMessage(inputStream.readUTF());
			} catch(IOException e) {
				//Fail silently when we cant read anything
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * Instantiate a connection to the server by getting the streams
	 * @throws IOException
	 */
	public void openConnection() throws IOException {
		outputStream = new DataOutputStream(clientSocket.getOutputStream());
		inputStream = new DataInputStream(clientSocket.getInputStream());
	}
}
