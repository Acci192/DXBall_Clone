package DX_Ball;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
	public Window(int width, int height, String title, Game game) {
		super(title);

		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.add(game);
		this.setVisible(true);
		//Open the window at the center of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width/2 - this.getWidth()/2, screenSize.height/2 - this.getHeight()/2);
		game.start();
	}
}