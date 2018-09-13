package DX_Ball.State;

import DX_Ball.MenuButton;

import java.awt.*;
import java.util.LinkedList;

public interface State {
	void tick();
	void render(Graphics2D g2);
	LinkedList<MenuButton> getButtons();
}
