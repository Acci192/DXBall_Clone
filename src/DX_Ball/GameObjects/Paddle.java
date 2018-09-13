package DX_Ball.GameObjects;

import DX_Ball.Game;
import DX_Ball.Handler;
import javafx.geometry.Point2D;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/********************
 * Paddle Class
 * Extends GameObject implements Movable
 *
 * The paddle is the object that the user steer, to prevent the ball to reach the bottom of the screen
 * The Paddle has different "states" it can be in. Magnetic and hasWeapon.
 * If it is magnetic the ball will stick to the paddle whenever they collide. If the paddle has weapons
 * the player are able to shoot by pressing space *
 ********************/
public class Paddle extends GameObject implements Movable{
	//Sets the Default size of the paddle and also set the Max and Min size of the paddle
	private static final double DEFAULT_WIDTH = 100;
	private static final double MAX_WIDTH = DEFAULT_WIDTH * 3;
	private static final double MIN_WIDTH = DEFAULT_WIDTH * 0.5;
	private static final double HEIGHT = 10;

	private Point2D dir;
	private int speed;

	//Booleans that keep track of powerUps
	private boolean haveGuns;
	private boolean magnetic;
	private boolean reloading;

	public Paddle(double x, double y) {
		super(x, y, DEFAULT_WIDTH, HEIGHT, ID.Player);
		dir = new Point2D(0, 0);
		speed = 7;
		haveGuns = false;
		reloading = false;
		magnetic = false;
	}

	public void tick() {
		move();
	}

	//Render the paddle differently depending on which powerUp is active
	public void render(Graphics2D g2) {
		if(magnetic && haveGuns) {
			g2.setColor(new Color(0xCE72BE));
		} else if(magnetic) {
			g2.setColor(new Color(0x3256CE));
		} else if(haveGuns) {
			g2.setColor(Color.RED);
		} else {
			g2.setColor(Color.WHITE);
		}
		g2.fill(body);
	}
	public void collide(GameObject object, Handler handler) {}
	public boolean isDead() {
		return false;
	}
	public void move() {
		pos = pos.add(dir.normalize().multiply(speed));
		setPosition(clamp(pos.getX(), 0, Game.WIDTH - body.getWidth() - 6), body.getY());
	}
	public void setDir(double x, double y) {
		dir = new Point2D(x, y);
	}
	public Point2D getDir() {
		return dir;
	}
	private void setPosition(double x, double y) {
		pos = new Point2D(x, y);
		body.setRect(pos.getX(), pos.getY(), body.getWidth(), body.getHeight());
	}

	//Function that increases the size of the paddle up to the MAX_WIDTH
	public void increasePaddle() {
		body.width += DEFAULT_WIDTH * 0.5;
		if(body.width > MAX_WIDTH) {
			body.width = MAX_WIDTH;
		}
	}
	//Function that decreases  the size of the paddle down to the MIN_WIDTH
	public void decreasePaddle() {
		body.width -= DEFAULT_WIDTH * 0.5;
		if(body.width < MIN_WIDTH) {
			body.width = MIN_WIDTH;
		}
	}
	//Add powerUps to the paddle that allow the user to shoot/control the ball when it hits the paddle
	public void addGuns() {
		haveGuns = true;
	}
	public void beMagnetic() {
		magnetic = true;
	}

	//Check if the paddle is allowed to shoot
	public boolean canShoot() {
		return haveGuns && !reloading;
	}

	//Shoot function that creates two bullets on each side of the paddle
	public void shoot(Handler handler) {
		handler.addObject(new Bullet(pos.getX(), pos.getY()));
		handler.addObject(new Bullet(pos.getX() + body.getWidth() - 5, pos.getY()));
		reloading = true;
		Timer reloadTimer = new Timer();
		reloadTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				reloading = false;
			}
		}, 500);
	}

	//Signals that the paddle is magnetic, this make the ball stick to the paddle when they collide
	public boolean isMagnetic() {
		return magnetic;
	}

	//Only used on the MainScreen to make the paddle follow the ball
	public void followBall(Ball ball) {
		setPosition(clamp(ball.getBody().getX() - DEFAULT_WIDTH/2, 0, Game.WIDTH - DEFAULT_WIDTH), body.getY());

	}
}
