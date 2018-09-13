package DX_Ball.State;

import DX_Ball.Game;
import DX_Ball.Handler;
import DX_Ball.MenuButton;

import java.awt.*;
import java.util.LinkedList;

/********************
 * GameState
 * Implements State
 *
 * This GameState which render and keep track of everything that happening in the game
 * Uses the Handler to do so.
 ********************/
public class GameState implements State {
	private Handler handler;
	//This constructor is used when it transition from Pause to GameState. It continues the game from where it was paused.
	public GameState(Handler handler) {
		this.handler = handler;
	}
	//This constructor is used when it transition from MainMenu to GameState. It start the game from level 1.
	public GameState(State currentState, Handler handler) {
		this.handler = handler;
		handler.startNewGame();
	}


	public void tick() {
		handler.tick();
	}

	//Renders the the black background and then lets the handler render all the objects
	public void render(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		handler.render(g2);
	}

	@Override
	public LinkedList<MenuButton> getButtons() {
		return null;
	}
}
