package DX_Ball.GameObjects;

import DX_Ball.Handler;
import javafx.geometry.Point2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/********************
 * This is the SuperClass that every object in the game extends from.
 *
 * Declare common variable every object needs, a body, id, position.
 * Also declares abstract functions that every object need to implement, in order to store
 * different objects together in a container and they all do different things (Polymorphism).
 ********************/
public abstract class GameObject {
	protected Rectangle2D.Double body;
	protected ID id;
	protected Point2D pos;

	//The stander constructor that initialize the position and creates the "body" of the object, also gives the object an ID
	protected GameObject(double x, double y, double xSize, double ySize, ID id) {
		this.pos = new Point2D(x, y);
		this.body = new Rectangle2D.Double(pos.getX(), pos.getY(), xSize, ySize);
		this.id = id;
	}

	public abstract void tick();
	public abstract void render(Graphics2D g2);
	public abstract void collide(GameObject object, Handler handler);
	public abstract boolean isDead();
	public ID getId() {
		return id;
	}
	public Rectangle2D getBody() {
		return body;
	}

	//Function to clamp a double in case it is outside the scope of the min and max value
	protected static double clamp(double pos, double min, double max) {
		if(pos < min) {
			return min;
		} else if (pos	> max) {
			return max;
		} else {
			return pos;
		}
	}
}
