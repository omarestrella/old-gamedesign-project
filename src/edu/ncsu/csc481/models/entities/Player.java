package edu.ncsu.csc481.models.entities;

import edu.ncsu.csc481.models.Entity;
import processing.core.PApplet;
import processing.core.PConstants;

public class Player extends Entity {
	private int radius;

	private boolean jumping, moving;

	private char direction = 's'; // Still by default

	public Player(PApplet p, int x, int y, int radius, String id) {
		super(p, x, y, id);

		this.jumping = false;
		this.moving = false;
		this.radius = radius;
	}

	public int getRadius() {
		return this.radius;
	}

	public boolean isJumping() {
		return this.jumping;
	}

	public boolean isMoving() {
		return this.moving;
	}

	public char getDirection() {
		return this.direction;
	}

	public void setMoving(boolean m) {
		this.moving = m;

		if (!moving) {
			direction = 's';
		}
	}

	public void setJumping(boolean j) {
		this.jumping = j;
	}

	public void setDirection(char d) {
		this.direction = d;
	}

	public boolean isDead() {
		return this.getPosition().y > parent.height;
	}

	/**
	 * Display the circle at the current this.getPosition()ition
	 */
	@Override
	public void display() {
		parent.ellipseMode(PConstants.CENTER);
		parent.ellipse(this.getPosition().x, this.getPosition().y, radius * 2,
				radius * 2);
	}
}
