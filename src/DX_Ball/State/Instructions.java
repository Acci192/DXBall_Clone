package DX_Ball.State;

import DX_Ball.Game;
import DX_Ball.MenuButton;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
/********************
 * Instructions
 * Implements State
 *
 * This is a state which the games goes to when "How to play" button is pressed on the MainMenu
 * This state explain how the game works
 ********************/

public class Instructions implements State {
	private LinkedList<MenuButton> buttons;
	private Font titleFont;
	private ArrayList<String> instructions;

	public Instructions(Game mainGame) {
		buttons = new LinkedList<>();
		buttons.add(new MenuButton(300, "Back to Menu") {
			@Override
			public void action() {
				mainGame.setCurrentState(new MainMenu(mainGame));
			}
		});
		instructions = new ArrayList<>();
		instructions.add("Move the paddle with Left and Right arrow key");
		instructions.add("Shoot and release the ball with Space");
		instructions.add("Destroy all the blocks!");
		instructions.add("Don't die!");
		instructions.add("Can you find the Secret Level?");
		titleFont = new Font("Arial", Font.PLAIN, 24);
	}

	@Override
	public void tick() {}

	@Override
	public void render(Graphics2D g2) {

		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		FontMetrics fm = g2.getFontMetrics(titleFont);
		g2.setFont(titleFont);
		g2.setColor(Color.MAGENTA);
		for (int i = 0; i < instructions.size(); i++) {
			g2.drawString(instructions.get(i), Game.WIDTH / 2 - fm.stringWidth(instructions.get(i)) / 2, 100 + fm.getHeight() * i);
		}
		for (MenuButton button : buttons) {
			button.render(g2);
		}

	}

	@Override
	public LinkedList<MenuButton> getButtons() {
		return buttons;
	}
}
