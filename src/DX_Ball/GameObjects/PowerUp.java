package DX_Ball.GameObjects;

import DX_Ball.Game;
import DX_Ball.Handler;
import javafx.geometry.Point2D;

import java.awt.*;

/********************
 * PowerUp class
 * Extends GameObject Implements Movable
 *
 * This object has a chance to be created when a Block dies.
 * Depending on the ID it has different behaviors, which is handled by the handler when the
 * PowerUp collides with the paddle.
 ********************/

public class PowerUp extends GameObject implements Movable{
	//Final size for every powerUp
	private static final int WIDTH = 20;
	private static final int HEIGHT = 20;

	private Color color;
	private boolean dead;
	private Point2D dir;
	private double speed;

	public PowerUp(double x, double y, ID id, Color color) {
		super(x, y, WIDTH, HEIGHT, id);
		this.color = color;
		dead = false;
		//Changes the xDirection depending on which side of the screen it is created on
		if(x <= Game.WIDTH/2) {
			dir = new Point2D(0.5, 1);
		} else {
			dir = new Point2D(-0.5, 1);
		}
		speed = 2;
	}

	//Tick function that moves the object and "kills" it when it reaches the bottom of the screen
	public void tick() {
		move();
		if(pos.getY() > Game.HEIGHT) {
			dead = true;
		}
	}
	public void render(Graphics2D g2) {
		g2.setColor(color);
		g2.fill(body);
	}

	//If the object collides with the paddle it dies
	public void collide(GameObject object, Handler handler) {
		dead = true;
	}
	public boolean isDead() {
		return dead;
	}

	public void move() {
		pos = pos.add(dir.normalize().multiply(speed));
		body.setRect(pos.getX(), pos.getY(), body.getWidth(), body.getHeight());
	}
	public void setDir(double x, double y) {
		this.dir = new Point2D(x, y);
	}
	public Point2D getDir() {
		return dir;
	}
}
