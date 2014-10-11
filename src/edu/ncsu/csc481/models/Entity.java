package edu.ncsu.csc481.models;

import java.util.LinkedList;

import processing.core.PApplet;
import edu.ncsu.csc481.models.entities.Player;
import edu.ncsu.csc481.models.entities.Position;

public abstract class Entity {
	protected PApplet parent;
	
	private Position pos;
	private int c;
	private String id;
	
	private float x;
	private float y;

	// Physicsy movement by looking at Box2D source
	private double gravity = .05;
	private double horizAcc = 2.0;
	private double vertAcc = .05;
	private double horizSpeed = 0.0;
	private double vertSpeed = 0.0;
	private double maxHorizSpeed = 10.0;
	private double maxVertSpeed = 2.5;

	private int STATE = 0;
	private int FALLING = 1;
	private int RISING = 2;

	private LinkedList<Entity> children;

	/**
	 * Generic Game Object to hold children
	 */
	public Entity(PApplet p, int x, int y, String id) {
		this.parent = p;
		this.pos = new Position(x, y);
		this.id = id;
		this.c = parent.color(255);
		
		this.x = pos.x;
		this.y = pos.y;

		children = new LinkedList<Entity>();
	}

	public Position getPosition() {
		return this.pos;
	}

	public String getId() {
		return this.id;
	}

	public LinkedList<Entity> getChildren() {
		return this.children;
	}

	public double getMaxVertSpeed() {
		return this.maxVertSpeed;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setVertSpeed(double d) {
		this.vertSpeed = d;
	}

	/**
	 * Display the object at the current position
	 */
	public void display() {
		// Generic version not implemented yet
	}

	/**
	 * Move the object left
	 */
	public void moveLeft() {		
		if (this instanceof Player) {
			Player c = (Player) this;
			c.setDirection('l');
		}

		horizSpeed -= horizAcc;

		if (horizSpeed < maxHorizSpeed) {
			horizSpeed = horizAcc;
		}

		pos.x -= horizSpeed;
		this.x = pos.x;
	}

	/**
	 * Move the object right
	 */
	public void moveRight() {
		if (this instanceof Player) {
			Player c = (Player) this;
			c.setDirection('r');
		}

		horizSpeed += horizAcc;

		if (horizSpeed < maxHorizSpeed) {
			horizSpeed = horizAcc;
		}

		pos.x += horizSpeed;
		this.x = pos.x;
	}

	/**
	 * Make the object jump
	 */
	public void jump() {
		vertSpeed += vertAcc;

		if (vertSpeed >= maxVertSpeed) {
			STATE = FALLING;
			vertSpeed = -maxVertSpeed;

			if (this instanceof Player) {
				Player c = (Player) this;
				c.setJumping(false);
			}
		}

		pos.y -= vertSpeed;
		this.y = pos.y;
	}

	/**
	 * Make the object fall
	 */
	public void fall() {
		vertSpeed += gravity;

		if (vertSpeed >= 0.0) {
			STATE = 0;
			vertSpeed = 0.0;

			/*
			 * if(this instanceof Circle) { Circle c = (Circle) this;
			 * c.setJumping(false); }
			 */
		}

		pos.y -= vertSpeed;
		this.y = pos.y;
	}
	
	public String toString() {
		return String.format("id: %s, x: %f, y: %f", id, x, y);
	}
}
