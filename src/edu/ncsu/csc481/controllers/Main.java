package edu.ncsu.csc481.controllers;

import java.io.IOException;
import java.util.Collection;

import processing.core.PApplet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.ncsu.csc481.controllers.Configuration.Data;
import edu.ncsu.csc481.controllers.Configuration.DataContainer;
import edu.ncsu.csc481.events.Event;
import edu.ncsu.csc481.events.EventBus;
import edu.ncsu.csc481.events.SimpleEventBus;
import edu.ncsu.csc481.models.Entity;
import edu.ncsu.csc481.models.Scene;
import edu.ncsu.csc481.models.entities.Platform;
import edu.ncsu.csc481.models.entities.Player;
import edu.ncsu.csc481.models.entities.Position;
import edu.ncsu.csc481.server.Client;

public class Main extends PApplet {
	private static final long serialVersionUID = 5595911279477938733L;
	
	private PApplet self = this;
	
	private Scene scene;
	private Player player;
	private EventBus bus;
	
	private Collection<Entity> currentObjects;

	private Client client;
	
	private Gson gson;
	
	public void reset() {
		scene = new Scene(this);
		
		try {
			configure();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		player = (Player) scene.getObjects().get("player"); //Use the ID set in the objects config
	}
	
	public void setup() {
		//Initialize all necessary components
		bus = SimpleEventBus.getInstance();
		scene = new Scene(this);
		
		//Use this to serialize objects into JSON
		gson = new GsonBuilder().setExclusionStrategies(new Configuration.Excluder(PApplet.class)).create();
		
		//Initialize the general state of the game
		size(320, 240);
		stroke(1);
		smooth();
		frameRate(60);
		background(200);
		registerEvents();
		
		//Configure the game
		try {
			configure();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		//Use the ID set in the objects config to get the player
		//Then change it to a unique hashed id
		player = (Player) scene.getObjects().get("player");
		player.setId(String.valueOf(player.hashCode()));
		
		currentObjects = scene.getObjects().values();
		
		//Now open the connection to the server
		client = new Client("127.0.0.1", 8888, player.getId());
		
		try {
			client.openConnection();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//Send player created message after client was started
		bus.fireEvents(EventBus.TYPE.PLAYER_CREATED, gson.toJson(player));
	}
	
	public void draw() {
		background(200);
		frameRate(60);
		
		//Draw the frame rate in the top left corner
		//text(frameRate, 20, 20);
			
		//Display all objects
		for(Entity e : scene.getObjects().values()) {
			e.display();
			
			if(e.getChildren().size() != 0) {
				for(Entity o : e.getChildren()) {
					o.display();
				}
			}
		}
	  
		keyPress(); //Smooth movement
	  
		//Handle jumping and falling
		if(player.isJumping()) {
			if(scene.isSpaceEmpty(UP, player)) {
				player.jump();
			}
			
			bus.fireEvents(EventBus.TYPE.PLAYER_MOVED, gson.toJson(player));
		} else {
			if(scene.isSpaceEmpty(DOWN, player)) {
				player.setVertSpeed(-player.getMaxVertSpeed());
				player.fall();
				
				bus.fireEvents(EventBus.TYPE.PLAYER_MOVED, gson.toJson(player));
			} else {
				player.setVertSpeed(0.0);
			}
		}
	  
		//Handle scrolling
		if(player.getPosition().x + player.getRadius() >= width) {
			scene.moveLeft(player);
		} else if (player.getPosition().x - player.getRadius() <= 0) {
			scene.moveRight(player);
		}
		
		if(player.isDead()) {
			reset();
		}
	}

	public void keyPress() {
		if(player.isMoving()) {
			if(keyCode == LEFT) {
				if(scene.isSpaceEmpty(LEFT, player)) {
					player.moveLeft();
					bus.fireEvents(EventBus.TYPE.PLAYER_MOVED, gson.toJson(player));
				}
			} else if(keyCode == RIGHT) {
				if(scene.isSpaceEmpty(RIGHT, player)) {
					player.moveRight();
					bus.fireEvents(EventBus.TYPE.PLAYER_MOVED, gson.toJson(player));
				}
			} else if(key == ' ') {
				player.setJumping(true);
				player.jump();
				bus.fireEvents(EventBus.TYPE.PLAYER_MOVED, gson.toJson(player));
			}
			
			bus.fireEvents(EventBus.TYPE.PLAYER_MOVED, gson.toJson(player));
		}
	}

	public void keyPressed() {
		player.setMoving(true);
		
		bus.fireEvents(EventBus.TYPE.PLAYER_MOVED, gson.toJson(player));
	}

	public void keyReleased() {
		player.setMoving(false);
	}	
	
	public void configure() throws Exception {
		DataContainer data;
		Configuration conf = new Configuration("config/objects.json");
		
		conf.readConfiguration();
		data = conf.getData();
		
		if(data != null) {
			for(Data d : data.getData()) {
				if(d.getType().equals("player")) {
					Player c = new Player(this, d.getPosition().get(0), d.getPosition().get(1), 
							d.getDimensions().get(0), d.getId());
					scene.addObject(c);
				} else if(d.getType().equals("platform")) {
					Platform p = new Platform(this, d.getPosition().get(0), d.getPosition().get(1),
							d.getDimensions().get(0), d.getDimensions().get(1), d.getId());
					scene.addObject(p);
				} else {
					throw new Exception("Encountered an invalid object type");
				}
			}
		}
	}
	
	public void registerEvents() {		
		//Send a message to the server from the client
		bus.addEventListener(EventBus.TYPE.PLAYER_MOVED, new Event() {
			public void run(String data) {
				try {
					client.sendMessage(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		//Send message on creation
		bus.addEventListener(EventBus.TYPE.PLAYER_CREATED, new Event() {
			public void run(String data) {
				try {
					client.sendMessage(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		//Read a message from the server
		bus.addEventListener(EventBus.TYPE.SERVER_MESSAGE, new Event() {
			public void run(String data) {
				Configuration.JsonClass c = gson.fromJson(data, Configuration.JsonClass.class);
				Player p = new Player(self, (int) c.getX(), (int) c.getY(), c.getRadius(), c.getId());
				
				if(!c.getId().equals(player.getId())) {
					if(scene.getObjects().get(c.getId()) != null) {
						scene.getObjects().get(p.getId()).getPosition().x = c.getX();
						scene.getObjects().get(p.getId()).getPosition().y = c.getY();
					} else {
						scene.addObject(p);
					}
				}
			}
		});
	}
	
	public void stop() {
		client.closeConnection();
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] {"edu.ncsu.csc481.controllers.Main"});
	}
}
