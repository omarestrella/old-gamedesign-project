package edu.ncsu.csc481.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class Server implements Runnable {
	private ServerSocket serverSocket;
	private Thread serverThread;
	private LinkedList<HashMap<String, Object>> clients;
	
	private int port;
	
	public Server(int port) {
		clients = new LinkedList<HashMap<String, Object>>();
		this.port = port;
		
		System.out.println("Server started");
	}
	
	/**
	 * Return the server socket
	 * @return the server socket
	 */
	public ServerSocket getSocket() {
		return serverSocket;
	}
	
	/**
	 * Run method for Runnable
	 */
	public void run() {
		try {
			while(true) {
				if(serverSocket == null) {
					serverSocket = new ServerSocket(port);
				}
				
				addClient(serverSocket.accept());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a client connection to the server by spawning a new thread
	 * @param socket the client socket
	 */
	public void addClient(Socket socket) {
		System.out.println("Client connected at: " + (new Date()).toString());
		
		ServerThread thread = new ServerThread(this, socket);
		
		try {
			//We can only create an object input/output stream once, so we should just
			//store it into the linked list along with the client
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("socket", socket);
			map.put("outputStream", new DataOutputStream(socket.getOutputStream()));
			map.put("inputStream", new DataInputStream(socket.getInputStream()));
			
			clients.add(map);
			thread.openConnection();
			thread.start();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Start running the server in a new thread
	 */
	public void startServer() {
		serverThread = new Thread(this);
		serverThread.start();
	}
	
	/**
	 * Stop the server and all threads
	 * @throws IOException
	 */
	public void stopServer() throws IOException {
		for(HashMap<String, Object> client : clients) {
			((Socket) client.get("socket")).close();
		}
		
		serverThread = null; //same as above
		serverSocket.close();
	}
	
	/**
	 * Send a string to all clients
	 * @param str the string to send to clients
	 * @throws IOException 
	 */
	public void sendMessage(String str) throws IOException {
		for(HashMap<String, Object> client : clients) {
			try {
				((DataOutputStream) client.get("outputStream")).writeUTF(str);
			} catch(IOException e) {
				//Fail silently for now
				//e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		//Start the server thread and have it listen on 8888
		Server server = new Server(8888);
		Thread serverThread = new Thread(server);
		serverThread.start();
			
		//Testing code
		/*try {
			Client client1 = new Client("127.0.0.1", 8888);
			client1.openConnection();
			client1.sendMessage("Blah from 1");
			Client client2 = new Client("127.0.0.1", 8888);
			client2.openConnection();
			client2.sendMessage("Blah from 2");
			client1.sendMessage("Bleh from 1");
			client2.sendMessage("Bleh from 2");
			server.sendMessage("Message from server!");
		} catch(IOException e) {
			e.printStackTrace();
		}*/
	}
}
