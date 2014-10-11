package edu.ncsu.csc481.models;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PConstants;
import edu.ncsu.csc481.models.entities.Player;
import edu.ncsu.csc481.models.entities.Position;
import edu.ncsu.csc481.models.entities.Platform;

public class Scene {
	protected PApplet parent;
	
	private HashMap<String, Entity> objects;
	private HashMap<String, Position> boundaries;

	private int MOVEMENT = 100;

	public Scene(PApplet p) {
		parent = p;
		objects = new HashMap<String, Entity>();
		boundaries = new HashMap<String, Position>();

		boundaries.put("left", new Position(-1000, 0));
		boundaries.put("right", new Position(1000, 0));
		boundaries.put("top", new Position(0, -1000));
		boundaries.put("bottom", new Position(0, 0));
	}

	public void addObject(Entity obj) {
		objects.put(obj.getId(), obj);
	}

	public HashMap<String, Entity> getObjects() {
		return objects;
	}

	public HashMap<String, Position> getBoundaries() {
		return boundaries;
	}

	/**
	 * Move all objects in the world to the left
	 */
	public void moveLeft(Entity obj) {
		for (Entity o : objects.values()) {
			o.getPosition().x -= MOVEMENT;
		}
	}

	/**
	 * Move all objects in the world to the right
	 */
	public void moveRight(Entity obj) {
		for (Entity o : objects.values()) {
			o.getPosition().x += MOVEMENT;
		}
	}

	/**
	 * Check whether or not the space in a direction contains an object relative
	 * to a certain object Right now uses a sort of bounding box approach
	 */
	public boolean isSpaceEmpty(int direction, Entity obj) {
		Player c = null;

		if (obj instanceof Player) {
			c = (Player) obj;
		}

		if (direction == PConstants.LEFT) {
			for (Entity o : objects.values()) {
				// Test everything but the player circle
				if (obj != o) {
					if (o instanceof Platform) {
						Platform r = (Platform) o;

						// Check the object to the left
						if (r.getPosition().x + r.getWidth() / 2 < c
								.getPosition().x - c.getRadius()) {
							// Make sure the object isnt clear above
							if (r.getPosition().y + r.getHeight() / 2 < c
									.getPosition().y + c.getRadius()) {
								// Subtract 1 to fix the issue when it clips
								// over the next object
								if (r.getPosition().x + r.getWidth() / 2 >= c
										.getPosition().x - c.getRadius() - 1) {
									return false;
								}
							}
						}

					}
				}
			}

			return true;
		}

		if (direction == PConstants.RIGHT) {
			for (Entity o : objects.values()) {
				// Test everything but the player circle
				if (obj != o) {
					if (o instanceof Platform) {
						Platform r = (Platform) o;

						// Check objects to the right
						if (r.getPosition().x - r.getWidth() / 2 > c
								.getPosition().x + c.getRadius()) {
							// Make sure the object isnt clear above
							if (r.getPosition().y + r.getHeight() / 2 < c
									.getPosition().y + c.getRadius()) {
								// Add 1 to fix the issue when it clips over the
								// next object
								if (r.getPosition().x - r.getWidth() / 2 <= c
										.getPosition().x + c.getRadius() + 1) {
									return false;
								}
							}
						}

					}
				}
			}

			return true;
		}

		if (direction == PConstants.UP) {
			for (Entity o : objects.values()) {
				// Test everything by the player circle
				if (obj != o) {
					if (o instanceof Platform) {
						Platform r = (Platform) o;

						if (r.getPosition().y + r.getHeight() / 2 >= c
								.getPosition().y - c.getRadius() - 1) {
							if (r.getPosition().x + r.getWidth() / 2
									+ c.getRadius() * 2 < c.getPosition().x
									+ c.getRadius()
									&& r.getPosition().x - r.getWidth() / 2
											- c.getRadius() * 2 > c
											.getPosition().x - c.getRadius()) {
								return false;
							}
						}
					}
				}

			}

			return true;
		}

		if (direction == PConstants.DOWN) {
			for (Entity o : objects.values()) {
				// Test everything by the player circle
				if (obj != o) {
					if (o instanceof Platform) {
						Platform r = (Platform) o;

						if (r.getPosition().y - r.getHeight() / 2 <= c
								.getPosition().y + c.getRadius() + 1) {
							if (r.getPosition().x + r.getWidth() / 2
									+ c.getRadius() * 2 > c.getPosition().x
									+ c.getRadius()
									&& r.getPosition().x - r.getWidth() / 2
											- c.getRadius() * 2 < c
											.getPosition().x - c.getRadius()) {
								return false;
							}
						}
					}
				}

			}

			return true;
		}

		return true;
	}
}