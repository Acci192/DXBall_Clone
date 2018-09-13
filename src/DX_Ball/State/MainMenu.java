package DX_Ball.State;

import DX_Ball.Game;
import DX_Ball.GameObjects.Ball;
import DX_Ball.GameObjects.ObjectFactory;
import DX_Ball.GameObjects.Paddle;
import DX_Ball.MenuButton;

import java.awt.*;
import java.util.LinkedList;

/********************
 * MainMenu
 * Implements State
 *
 * State for the Main Menu, It has buttons to take the user to different states.
 * Also has a bouncing ball and a paddle that follows the ball.
 ********************/
public class MainMenu implements State {
	private LinkedList<MenuButton> buttons;
	private String gameTitle;
	private Font titleFont;
	private Paddle paddle;
	private Ball bouncingBall;

	public MainMenu(Game mainGame) {
		buttons = new LinkedList<>();
		buttons.add(new MenuButton(125, "NEW GAME") {
			@Override
			public void action() {
				mainGame.setCurrentState(new GameState(mainGame.getCurrentState(), mainGame.getHandler()));
			}
		});
		buttons.add(new MenuButton(215, "HOW TO PLAY") {
			@Override
			public void action() {
				mainGame.setCurrentState(new Instructions(mainGame));
			}
		});
		buttons.add(new MenuButton(305, "QUIT") {
			@Override
			public void action() {
				System.exit(0);
			}
		});
		gameTitle = "DX BAAALL";
		titleFont = new Font("Impact", Font.PLAIN, 64);
		paddle = ObjectFactory.makePaddle();
		bouncingBall = new Ball(50, 100, paddle);
	}

	@Override
	public void tick() {
		if(bouncingBall.getBody().intersects(paddle.getBody())){
			bouncingBall.bounce(paddle.getBody());
		}
		for(MenuButton button : buttons) {
			if(bouncingBall.getBody().intersects(button.getBody())){
				bouncingBall.bounce(button.getBody());
			}
		}
		bouncingBall.tick();
		paddle.followBall(bouncingBall);

	}

	@Override
	public void render(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		bouncingBall.render(g2);
		paddle.render(g2);

		FontMetrics fm = g2.getFontMetrics(titleFont);
		g2.setFont(titleFont);
		g2.setColor(Color.MAGENTA);
		g2.drawString(gameTitle, Game.WIDTH/2 - fm.stringWidth(gameTitle)/2, 100);
		for (MenuButton button : buttons) {
			button.render(g2);
		}

	}

	@Override
	public LinkedList<MenuButton> getButtons() {
		return buttons;
	}
}
