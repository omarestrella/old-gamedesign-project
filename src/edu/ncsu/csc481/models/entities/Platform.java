package edu.ncsu.csc481.models.entities;

import edu.ncsu.csc481.models.Entity;
import processing.core.PApplet;
import processing.core.PConstants;

public class Platform extends Entity {
	private int width, height;
	
	public Platform(PApplet p, int x, int y, int w, int h, String id) {
		super(p, x, y, id);

		this.width = w;
		this.height = h;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	/**
	 * Display the rectangle at the current this.getPosition()ition
	 */
	@Override
	public void display() {
		parent.rectMode(PConstants.CENTER);
		parent.rect(this.getPosition().x, this.getPosition().y, this.width,
				this.height);
	}
}