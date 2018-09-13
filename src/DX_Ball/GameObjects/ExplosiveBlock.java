package DX_Ball.GameObjects;


import DX_Ball.Handler;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

/********************
 * ExplosiveBlock class
 * Extends from Block
 *
 * This block creates an explosive when it gets hit,
 * any block in range of the explosion is killed
 ********************/

public class ExplosiveBlock extends Block {
	//The area of the explosion
	private Rectangle2D explosiveArea;

	public ExplosiveBlock(double x, double y) {
		super(x, y, 1);
		//Set the area that the explosion covers
		this.explosiveArea = new Rectangle2D.Double(body.getX() - 4, body.getY() - 4, body.getWidth() + 8, body.getHeight() + 8);
	}

	//Renders the block with a TNT text to show that it's explosive. When the block is dies it renders the explosion instead
	public void render(Graphics2D g2) {
		if (id == ID.Block) {
			g2.setColor(Color.ORANGE);
			g2.fill(body);
			g2.setColor(Color.RED);
			g2.setFont(new Font("Arial Black", Font.BOLD, 18));
			g2.drawString("TNT", (int) body.getX() + 10, (int) body.getCenterY()+8);
			g2.setColor(BORDER_COLOR);
			g2.draw(body);
		} else {
			g2.setColor(new Color(0xE97E3A));
			g2.fill(body);
		}
	}

	//Changes the ID to become explosive instead and set the explosiveArea as the body instead. Also tells the handler to check for explosion
	public void collide(GameObject object, Handler handler) {
		Timer explosiveTimer = new Timer();
		body.setRect(explosiveArea);
		id = ID.Explosive;
		handler.checkForExplosion();
		//Kills the explosion after 0.5 sec
		explosiveTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				health--;
			}
		}, 500);
	}
}
