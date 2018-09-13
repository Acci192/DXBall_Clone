package DX_Ball;


import DX_Ball.GameObjects.*;
import DX_Ball.State.MainMenu;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;


/********************
 * Handler
 *
 * The Handler class keep track of every gameObject in the game. Calls the tick and render
 * method for each Object and check if there has been a collision between different objects.
 *
 * It also keep track of the current level and the amount of life the player have left.
 ********************/

public class Handler {
	private Game mainGame;

	private Paddle paddle;
	private LinkedList<GameObject> movingObjects;
	private LinkedList<Block> blocks;
	private int currentLevel;
	private LinkedList<GameObject> objectsToAdd;

	private int ballCount;
	private int life = 3;

	//Initialize the containers and start a new game
	public Handler(Game maingame) {
		this.mainGame = maingame;
		movingObjects = new LinkedList<>();
		blocks = new LinkedList<>();
		objectsToAdd = new LinkedList<>();
		startNewGame();
	}

	//Function that loads the level from the layout that was given from the MapGenerator
	private void loadLevel(int level) {
		Block[][] levelLayout = new MapGenerator(level).getBlockLayout();
		for (int column = 0; column < 10; column++) {
			for (int row = 0; row < 10; row++) {
				if (!(levelLayout[row][column] == null)) {
					addBlock(levelLayout[row][column]);
				}
			}
		}
	}
	//Creates a new paddle and ball, and makes sure that every container is empty. Also set the currentLevel to 1 and load that level
	public void startNewGame() {
		paddle = ObjectFactory.makePaddle();
		movingObjects.clear();
		objectsToAdd.clear();
		blocks.clear();
		addObject(new Ball(paddle));
		ballCount = 1;
		life = 3;
		currentLevel = 1;
		loadLevel(currentLevel);
	}
	//Creates a new paddle and ball, and makes sure that every container is empty. It also loads the next level
	private void startNewLevel() {
		paddle = ObjectFactory.makePaddle();
		movingObjects.clear();
		objectsToAdd.clear();
		blocks.clear();
		addObject(new Ball(paddle));
		ballCount = 1;
		currentLevel++;
		loadLevel(currentLevel);
	}
	private void loadSecretLevel() {
		paddle = ObjectFactory.makePaddle();
		movingObjects.clear();
		objectsToAdd.clear();
		blocks.clear();
		addObject(new Ball(paddle));
		ballCount = 1;
		currentLevel = 99;
		loadLevel(currentLevel);
	}

	//Tick function that updates every object in the game
	public void tick() {
		paddle.tick();
		//Updates the objects in the game and check for collisions
		for (GameObject obj : movingObjects) {
			ballActions(obj);
			powerUpActions(obj);
			bulletAction(obj);
			obj.tick();
		}

		//Adds new objects if there are any that is waiting to be added
		//This is done this way so it doesn't interfere with any other updates/rendering
		if(!objectsToAdd.isEmpty()){
			for(GameObject obj : objectsToAdd) {
				movingObjects.add(obj);
			}
			//Clears the container to make sure each object only get added once
			objectsToAdd.clear();
		}

		//Remove every dead objects
		killMovingObjects();
		killBlocks();

		//If block container is empty, the next level is loaded
		if (blocks.isEmpty() && currentLevel <= 11) {
			startNewLevel();
		} else if (blocks.isEmpty() && currentLevel == 99) {
			mainGame.setCurrentState(new MainMenu(mainGame));
		}

		//Loose one life if all the balls died and reset the paddle/ball
		if(ballCount <= 0) {
			life--;
			movingObjects.clear();
			paddle = new Paddle(Game.WIDTH / 2, (Game.HEIGHT / 20) * 18);
			addObject(new Ball(paddle));
			ballCount = 1;
			//Go to the MainMenu if the player ran out of lifes
			if(life <= 0) {
				mainGame.setCurrentState(new MainMenu(mainGame));
			}
		}
	}
	public void render(Graphics2D g2) {
		paddle.render(g2);
		for (GameObject obj : movingObjects) {
			obj.render(g2);
		}
		for (Block block : blocks) {
			block.render(g2);
		}
		for (int i = 0; i < life; i++) {
			g2.setColor(Color.WHITE);
			g2.fillRect(Game.WIDTH - 40 - i * 20, Game.HEIGHT - 50, 15, 5);
		}
	}
	public Paddle getPaddle() {
		return paddle;
	}

	//Function to add a Block to the block container
	private void addBlock(Block block) {
		blocks.add(block);
	}

	//Adds new object to a special container that adds the objects when it safe to add them
	public void addObject(GameObject object) {
		objectsToAdd.add(object);
	}

	//Updates the balls and check if they collide with any objects
	private void ballActions(GameObject obj) {
		if (obj.getId() == ID.Ball) {
			if(obj.getBody().intersects(getPaddle().getBody())) {
				obj.collide(paddle, this);
			}
			for (Block block : blocks) {
				if (obj.getBody().intersects(block.getBody())) {
					obj.collide(block, this);
					block.collide(obj, this);
				}
			}
			if(obj.getBody().getX() > Game.WIDTH - 7) {
				loadSecretLevel();
			}
		}
	}

	//Updates the PowerUps and check if they collide with the paddle. Then calls certain functions depending on the ID of the powerUp
	private void powerUpActions(GameObject obj) {
		if (obj instanceof PowerUp) {
			if (obj.getBody().intersects(getPaddle().getBody())) {
				switch (obj.getId()) {
					case PU_IncreasePaddle:
						paddle.increasePaddle();
						break;
					case PU_DecreasePaddle:
						paddle.decreasePaddle();
						break;
					case PU_SplitBall:
						splitBalls();
						break;
					case PU_Guns:
						paddle.addGuns();
						break;
					case PU_Magnetic:
						paddle.beMagnetic();
						break;
				}
				obj.collide(paddle, this);
			}
		}
	}

	//Updates the bullets and check if they collide with any block
	private void bulletAction(GameObject obj) {
		if(obj.getId() == ID.Bullet){
			for(Block block : blocks) {
				if(obj.getBody().intersects(block.getBody())){
					obj.collide(block, this);
				}
			}
		}
	}

	//Remove all the dead objects in the MovingObjects container
	private void killMovingObjects() {
		for (int i = 0; i < movingObjects.size(); ) {
			if (movingObjects.get(i).isDead()) {
				if(movingObjects.get(i).getId() == ID.Ball) {
					ballCount--;
				}
				movingObjects.remove(i);
			} else {
				i++;
			}
		}
	}

	//Remove all the dead blocks. Each block has a chance to spawn a random PowerUp
	private void killBlocks() {
		for (int i = 0; i < blocks.size(); ) {
			if (blocks.get(i).isDead()) {
				Block deadBlock = blocks.get(i);
				Random randomGenerator = new Random();
				if(randomGenerator.nextInt(12) == 0) {
					addObject(ObjectFactory.makePowerUp(randomGenerator.nextInt(5), deadBlock.getBody().getX(), deadBlock.getBody().getY()));
				}
				blocks.remove(deadBlock);
			} else {
				i++;
			}
		}
	}

	//Function that is called when there has been an explosion and checks if any blocks collide with the explosion
	public void checkForExplosion() {
		for (int i = 0; i < blocks.size(); i++) {
			if (blocks.get(i).getId() == ID.Explosive) {
				Block explosiveBlock = blocks.get(i);
				for (Block block : blocks) {
					if (block.getId() == ID.Block && explosiveBlock.getBody().intersects(block.getBody())) {
						block.collide(explosiveBlock, this);
					}
				}
			}
		}
	}
	private void splitBalls() {
		int sizeOfContainer = movingObjects.size();
		for (int i = 0; i < sizeOfContainer; i++) {
			if (movingObjects.get(i).getId() == ID.Ball) {
				Ball newBall = new Ball((Ball) movingObjects.get(i), paddle);
				addObject(newBall);
				ballCount++;
			}
		}
	}

	//Release any block that is stuck to the paddle
	public void releaseBalls() {
		for(GameObject obj : movingObjects){
			if(obj.getId() == ID.Ball) {
				obj.collide(obj, this);
			}
		}
	}
}
