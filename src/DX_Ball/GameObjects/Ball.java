package DX_Ball.GameObjects;

import DX_Ball.Game;
import DX_Ball.Handler;
import javafx.geometry.Point2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/********************
 * Ball class
 * Extends GameObject implements Movable
 *
 * This is the ball object that moves around on the screen automatically. If it collides
 * with a block, paddle or wall it bounces which change the direction the ball is travelling in.
 * The ball has a reference to the Paddle object in order to be able to "Stick" to the paddle
 * if it is magnetic.
 ********************/
public class Ball extends GameObject implements Movable {
	//Final size for each ball
	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;

	private Point2D dir;
	private double speed;
	private Paddle mainPaddle;
	private boolean stuckToPaddle;
	private double paddleOffset;
	private boolean dead;

	//Constructor that copy the ball that was given, and switches the xDirection
	public Ball(Ball ball, Paddle paddle) {
		super(ball.getBody().getX(), ball.getBody().getY(), WIDTH, HEIGHT, ID.Ball);
		dir = ball.getDir();
		switchXDir();
		speed = ball.speed;
		mainPaddle = paddle;
		stuckToPaddle = ball.stuckToPaddle;
		paddleOffset = ball.paddleOffset;
	}

	//Constructor that places the ball at the paddle
	public Ball(Paddle paddle) {
		super(paddle.getBody().getX() + 50, paddle.getBody().getY() - HEIGHT, WIDTH, HEIGHT, ID.Ball);
		dir = new Point2D(1, -1);
		speed = 5;
		mainPaddle = paddle;
		stuckToPaddle = true;
		paddleOffset = 50;
	}

	//This constructor is only used to create the ball for the MainMenu
	public Ball(double x, double y, Paddle paddle){
		super(x, y, WIDTH, HEIGHT, ID.Ball);
		dir = new Point2D(1, 1);
		speed = 5;
		mainPaddle = paddle;
		stuckToPaddle = false;
		paddleOffset = 50;
	}

	//Tick function that moves the ball and "kills" it when it reaches the bottom of the screen
	public void tick() {
		move();
		if(pos.getY() >= Game.HEIGHT) {
			dead = true;
		}
	}

	public void render(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.fill(new Ellipse2D.Double(body.getX(), body.getY(), body.getWidth(), body.getHeight()));
		//g2.fill(body);
	}

	//The collision function, makes the ball bounce off the object it hits
	public void collide(GameObject object, Handler handler) {
		if(object.getId() == ID.Player) {
			setDir(calculateXDir(object.getBody()), dir.getY());
			bounce(object.getBody());
			//Stick to the paddle if it s magnetic
			if(mainPaddle.isMagnetic()){
				stuckToPaddle = true;
				paddleOffset = body.getX() - mainPaddle.getBody().getX();
			}
			speed *= 1.02;
		}
		if(object.getId() == ID.Block) {
			calculateSideOfBlockAndBounce(object.getBody());
		}
		//This is called by the handler to signal that the player released the ball from paddle
		if(object.equals(this)) {
			stuckToPaddle = false;
		}
	}

	public boolean isDead() {
		return dead;
	}

	//Move function that bounces the ball off each side of the screen.
	//If it's stuck to the paddle it moves around with it instead
	public void move() {
		if(stuckToPaddle) {
			setPosition(mainPaddle.getBody().getX() + paddleOffset, mainPaddle.getBody().getY() - HEIGHT);
		} else {
			if (body.getX() <= 0 || body.getX() + body.getWidth()> Game.WIDTH - 5) {
				switchXDir();
			}
			if (body.getY() <= 0) {
				switchYDir();
			}
			pos = pos.add(dir.normalize().multiply(speed));
			setPosition(clamp(pos.getX(), 0, Game.WIDTH), clamp(pos.getY(), 0, Game.HEIGHT));
		}

	}

	public void setDir(double x, double y) {
		this.dir = new Point2D(x, y);
	}
	public Point2D getDir() {
		return dir;
	}
	private void setPosition(double x, double y) {
		pos = new Point2D(x, y);
		body.setRect(pos.getX(), pos.getY(), body.getWidth(), body.getHeight());
	}

	//Switches the x/y direction to go the opposite direction
	private void switchXDir() {
		double newDirX = dir.getX() * -1;
		dir = new Point2D(newDirX, dir.getY());
	}
	private void switchYDir() {
		double newDirY = dir.getY() * -1;
		dir =  new Point2D(dir.getX(), newDirY);
	}

	//Bounces the ball
	public void bounce(Rectangle2D objectBody) {
		if(pos.getY() < objectBody.getY()) {
			switchYDir();
			setPosition(pos.getX(), objectBody.getY() - body.getHeight());
		} else if( pos.getY() + body.getHeight() > objectBody.getY() + objectBody.getHeight()) {
			switchYDir();
			setPosition(pos.getX(), objectBody.getY() + objectBody.getHeight());
		} else if(pos.getX() < objectBody.getX()){
			switchXDir();
			setPosition(objectBody.getX() - body.getWidth(), pos.getY());
		} else if (pos.getX() + body.getWidth() > objectBody.getX() + objectBody.getWidth()) {
			switchXDir();
			setPosition(objectBody.getX() + objectBody.getWidth(), pos.getY());
		}
	}

	//Calculate the side of the block the ball hit and calls the bounceOnBlockFunction
	//This Logic assumes that only one block is hit at the same time if the ball intersects with more than one ball, the logic fails
	private void calculateSideOfBlockAndBounce(Rectangle2D blockBody){
		//Creates a new rectangle representing the overlapping area
		Rectangle2D overlappingArea = blockBody.createIntersection(body);

		//Determine which side the ball hit
		if(overlappingArea.getWidth() > overlappingArea.getHeight()){
			if(body.getCenterY() > blockBody.getCenterY()){
				//Bottom
				setPosition(body.getX(), body.getY() + overlappingArea.getHeight());
				bounceOnBlock(3);
			} else {
				//Top
				setPosition(body.getX(), body.getY() - overlappingArea.getHeight());
				bounceOnBlock(1);
			}
		} else {
			if(body.getCenterX() < blockBody.getCenterX()){
				//Left
				setPosition(body.getX() - overlappingArea.getWidth(), body.getY());
				bounceOnBlock(0);
			} else {
				//Right
				setPosition(body.getX() + overlappingArea.getWidth(), body.getY());
				bounceOnBlock(2);
			}
		}
	}

	//Changes the direction depending on which side of the block the ball hit
	private void bounceOnBlock(double blockSide){
		//BlockSide: 0 = left, 1 = top, 2 = right, 3 = bottom

		if(dir.getX() > 0){
			//Ball moving to the right
			if(dir.getY() > 0){
				//Ball moving down
				if(blockSide == 0 || blockSide == 3){
					//Ball hit left or bottom side of the block
					switchXDir();
				} else {
					//Ball hit bottom or top of the block
					switchYDir();
				}
			} else {
				//Ball moving up
				if(blockSide == 0 || blockSide == 1){
					//Ball hit left or top side of the block
					switchXDir();
				} else {
					//Ball hit bottom or top of the block
					switchYDir();
				}
			}
		} else if(dir.getX() <= 0) {
			//Ball moving to the left
			if(dir.getY() > 0){
				//Ball moving down
				if(blockSide == 2 || blockSide == 3){
					//Ball hit the right or top side of the block
					switchXDir();
				} else {
					switchYDir();
				}
			} else {
				//Ball moving up
				if(blockSide == 2 || blockSide == 1){
					//Ball hit the right or top side of the block
					switchXDir();
				} else {
					switchYDir();
				}
			}
		}
	}

	//Calculate the direction depending on where it collided with the paddle (middle = less xDirection, sides = bigger xDirection)
	private double calculateXDir(Rectangle2D paddle) {
		double ballCenter = body.getCenterX();
		double paddleCenter = paddle.getCenterX();
		double xDir = ((ballCenter - paddleCenter)/(paddle.getWidth()/4));
		return xDir;
	}
}
