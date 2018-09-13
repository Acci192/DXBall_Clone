package DX_Ball;

import DX_Ball.State.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
	private Handler handler;
	private Game mainGame;

	public KeyInput(Handler handler, Game mainGame) {
		this.handler = handler;
		this.mainGame = mainGame;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(mainGame.getCurrentState() instanceof GameState) {
			//Transition to pauseScreen
			if(key == KeyEvent.VK_ESCAPE)mainGame.setCurrentState(new Pause(mainGame, handler));

			//User inputs to play
			if (handler.getPaddle().getBody().getX() > 0 && key == KeyEvent.VK_LEFT) handler.getPaddle().setDir(-1, 0);
			if (handler.getPaddle().getBody().getX() < Game.WIDTH - 6 && key == KeyEvent.VK_RIGHT) handler.getPaddle().setDir(1, 0);
			if(key == KeyEvent.VK_SPACE && handler.getPaddle() != null){
				if(handler.getPaddle().canShoot()) {
					handler.getPaddle().shoot(handler);
				}
				handler.releaseBalls();
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(mainGame.getCurrentState() instanceof GameState) {
			if (key == KeyEvent.VK_LEFT && handler.getPaddle().getDir().getX() < 0) handler.getPaddle().setDir(0, 0);
			if (key == KeyEvent.VK_RIGHT && handler.getPaddle().getDir().getX() > 0) handler.getPaddle().setDir(0, 0);
		}
	}
}
