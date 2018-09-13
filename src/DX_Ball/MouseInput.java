package DX_Ball;

import DX_Ball.State.GameState;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;


public class MouseInput extends MouseAdapter {
	private Game mainGame;
	private LinkedList<MenuButton> buttons;

	public MouseInput(Game mainGame) {
		this.mainGame = mainGame;
	}

	//Calls the MenuButton action() function when user clicks the button
	public void mouseClicked(MouseEvent e) {
		if(!(mainGame.getCurrentState() instanceof GameState)){
			for(MenuButton button : buttons) {
				if(button.getBody().contains(e.getX(), e.getY())) {
					button.action();
				}
			}
		}
	}

	public void setButtons(LinkedList<MenuButton> buttons) {
		this.buttons = buttons;
	}
}
