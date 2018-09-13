package DX_Ball.GameObjects;

import javafx.geometry.Point2D;

/********************
 * Interface that implements the possibility for an object to move
 ********************/
public interface Movable {
	void move();
	void setDir(double x, double y);
	Point2D getDir();
}

