package DX_Ball.GameObjects;

import DX_Ball.Handler;
import javafx.geometry.Point2D;

import java.awt.*;

/********************
 * Bullet class
 *
 * This object is created when the player shoots. This object dies if it collides with
 * a block and also damages the block. If this object reach the top of the screen it also dies.
 ********************/
public class Bullet extends GameObject implements Movable {
	//Final size for each ball
	private static final int WIDTH = 5;
	private static final int HEIGHT = 10;

	private Color color;
	private Point2D dir;
	private int speed;
	private boolean dead;

	public Bullet(double x, double y) {
		super(x, y, WIDTH, HEIGHT, ID.Bullet);
		color = new Color(0xFF5464);
		dir = new Point2D(0, -1);
		speed = 4;
		dead = false;
	}

	//Tick function that moves the bullet and "kills" it when it reaches the top of the screen
	public void tick() {
		move();
		if (pos.getY() < 0) {
			dead = true;
		}
	}

	public void render(Graphics2D g2) {
		g2.setColor(color);
		g2.fill(body);
	}

	//Collision function that kills the bullet and calls the collide function for the block it collides with
	public void collide(GameObject object, Handler handler) {
		if (object.getId() == ID.Block) {
			dead = true;
			object.collide(this, handler);
		}
	}

	public boolean isDead() {
		return dead;
	}
	public void move() {
		pos = pos.add(dir.normalize().multiply(speed));
		body.setRect(pos.getX(), pos.getY(), body.getWidth(), body.getHeight());
	}
	public void setDir(double x, double y) {
		dir = new Point2D(x, y);
	}
	public Point2D getDir() {
		return dir;
	}
}
