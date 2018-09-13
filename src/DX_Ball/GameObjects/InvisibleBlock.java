package DX_Ball.GameObjects;

import java.awt.*;

/********************
 * Invisible block
 * Extends from Block
 *
 * This block starts out as invisible and only get rendered when it got hit once
 ********************/
public class InvisibleBlock extends Block {
	private Color blockColor = new Color(0xCE72BE);

	//Set the health to 2
	public InvisibleBlock(double x, double y) {
		super(x, y, 2);
	}

	//Only render the block when it has 1 life
	public void render(Graphics2D g2) {
		if(health == 1) {
			g2.setColor(blockColor);
			g2.fill(body);
			g2.setColor(BORDER_COLOR);
			g2.draw(body);
		}
	}
}
