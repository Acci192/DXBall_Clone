package DX_Ball.GameObjects;

import java.awt.*;

/********************
 * ToughBlock
 * Extends from Block
 *
 * This block is tougher than the other blocks since it has 3 health.
 * It still dies if it get hit by an explosive
 ********************/

public class ToughBlock extends Block{
	public ToughBlock(double x, double y) {
		super(x, y, 3);
	}

	//Render the block in different shades of gray depending on how much health is remaining (darker = more health remaining)
	public void render(Graphics2D g2) {
		if(health == 3) {
			g2.setColor(new Color(0x3B3B3B));
		} else if (health == 2) {
			g2.setColor(new Color(0x535353));
		} else {
			g2.setColor(new Color(0x777777));
		}
		g2.fill(body);
		g2.setColor(BORDER_COLOR);
		g2.draw(body);
	}
}
