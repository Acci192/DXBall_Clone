package DX_Ball;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/********************
 * MenuButton
 *
 * This is an abstract class with a fixed size and color.
 * The thing that differ buttons apart is the abstract method action() which need to be implemented
 * by a subclass or when creating a anonymous class.
 ********************/
public abstract class MenuButton {
	private static final Font FONT = new Font("Impact", Font.PLAIN, 36);
	private static final Color BUTTON_COLOR = Color.MAGENTA;
	private static final Color TEXT_COLOR = Color.BLACK;
	private static final double WIDTH = 200;
	private static final double HEIGHT = 50;
	private Rectangle2D buttonArea;
	private String buttonString;

	public MenuButton(double y, String buttonString) {
		this.buttonArea = new Rectangle2D.Double(Game.WIDTH/2 - WIDTH/2, y, WIDTH, HEIGHT);
		this.buttonString = buttonString;
	}

	public void render(Graphics2D g2) {
		FontMetrics fm = g2.getFontMetrics(FONT);
		g2.setColor(BUTTON_COLOR);



		g2.fill(buttonArea);
		g2.setFont(FONT);
		g2.setColor(TEXT_COLOR);
		g2.drawString(buttonString, (int)buttonArea.getCenterX()-fm.stringWidth(buttonString)/2, (int)buttonArea.getCenterY() + fm.getHeight()/3);
		g2.setColor(Color.CYAN);
		g2.draw(buttonArea);
	}

	public abstract void action();

	public Rectangle2D getBody() {
		return buttonArea;
	}


}
