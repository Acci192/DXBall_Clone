package DX_Ball.GameObjects;

import DX_Ball.Game;
import DX_Ball.Handler;

import java.awt.*;

/********************
 * Block class
 * Super class for different types of blocks
 *
 * Block class is the base class for every type of block.
 * It implements the tick method from GameObject and leaves it empty since there are no blocks that moves.
 * Also implements the size for each type of block and a collide function that is used for each block
 * except ExplosiveBlock
 ********************/
public abstract class Block extends GameObject{
	//Final size and border color for every block
	public static final int WIDTH = Game.WIDTH / 10;
	public static final int HEIGHT = Game.HEIGHT / 20;
	protected static final Color BORDER_COLOR = Color.WHITE;

	protected int health;

	public Block(double x, double y, int health) {
		super(x ,y, WIDTH ,HEIGHT, ID.Block);
		this.health = health;
	}

	public void tick() {}

	//Signals that the block died when health reaches 0
	public boolean isDead() {
		return health <= 0;
	}

	public void collide(GameObject object, Handler handler) {
		//Kills the block if it get hit by an explosive (No matter how much health it currently have)
		if(object.getId() == ID.Explosive) {
			health = 0;
		} else {
			health--;
		}
	}
}
