package DX_Ball.State;

import DX_Ball.Game;
import DX_Ball.Handler;
import DX_Ball.MenuButton;

import java.awt.*;
import java.util.LinkedList;

/********************
 * Pause
 * Implements State
 *
 * Pause state, uses the Handler to render the game. But doesn't run the tick method
 * which make it look like the game is Paused.
 ********************/
public class Pause implements State {
	private Handler handler;
	private LinkedList<MenuButton> buttons;
	private String pauseText;
	private Font textFont;
	public Pause(Game mainGame, Handler handler) {
		this.handler = handler;
		buttons = new LinkedList<>();
		buttons.add(new MenuButton(150, "Continue") {
			@Override
			public void action() {
				mainGame.setCurrentState(new GameState(handler));
			}
		});
		buttons.add(new MenuButton(250, "Back to Menu") {
			@Override
			public void action() {
				mainGame.setCurrentState(new MainMenu(mainGame));
			}
		});
		pauseText = "PAUSE";
		textFont = new Font("Impact", Font.PLAIN, 64);
	}

	@Override
	public void tick() {}

	@Override
	public void render(Graphics2D g2) {
		handler.render(g2);
		g2.setColor(new Color(0, 0, 0, 150));
		g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		FontMetrics fm = g2.getFontMetrics(textFont);
		g2.setFont(textFont);
		g2.setColor(Color.MAGENTA);
		g2.drawString(pauseText, Game.WIDTH/2 - fm.stringWidth(pauseText)/2, 100);
		for(MenuButton button : buttons) {
			button.render(g2);
		}
	}

	@Override
	public LinkedList<MenuButton> getButtons() {
		return buttons;
	}
}
