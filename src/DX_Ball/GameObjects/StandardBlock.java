package DX_Ball.GameObjects;

import java.awt.*;

/********************
 * StandardBlock
 * Extends from Block
 *
 * This block is the standard type of block, can be created in different colors but it only has 1 health
 ********************/
public class StandardBlock extends Block{
	private Color blockColor;

	//Sets the color of the block at creation
	public StandardBlock(double x, double y, Color blockColor) {
		super(x, y, 1);
		this.blockColor = blockColor;
	}

	public void render(Graphics2D g2) {
		g2.setColor(blockColor);
		g2.fill(body);
		g2.setColor(BORDER_COLOR);
		g2.draw(body);
	}
}
