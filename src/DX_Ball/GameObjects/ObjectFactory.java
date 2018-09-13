package DX_Ball.GameObjects;

import DX_Ball.Game;

import java.awt.*;
/********************
 * A class that has functions that creates certain objects depending on the input
 ********************/
public class ObjectFactory {
	//Function that creates a new Block depending on the character that is given
	public static Block makeBlock(String blockType, double x, double y) {
		switch (blockType) {
			case "g":
				return new StandardBlock(x, y, Color.GREEN);
			case "c":
				return new StandardBlock(x, y, Color.CYAN);
			case "p":
				return new StandardBlock(x, y, new Color(0xCD097A));
			case "b":
				return new StandardBlock(x, y, Color.BLUE);
			case "r":
				return new StandardBlock(x, y, Color.RED);
			case "y":
				return new StandardBlock(x, y, Color.YELLOW);
			case "t":
				return new ToughBlock(x, y);
			case "i":
				return new InvisibleBlock(x, y);
			case "e":
				return new ExplosiveBlock(x, y);
			default:
				return null;
		}
	}

	//Creates a PowerUp with different IDs depending on the integer that is given
	public static PowerUp makePowerUp(int type, double x, double y) {
		switch (type) {
			case 0:
				return new PowerUp(x, y, ID.PU_IncreasePaddle, new Color(0x007BFF));
			case 1:
				return new PowerUp(x, y, ID.PU_Magnetic, new Color(0x0054FF));
			case 2:
				return new PowerUp(x, y, ID.PU_Guns, new Color(0x003FFF));
			case 3:
				return new PowerUp(x, y, ID.PU_SplitBall, new Color(0x888987));
			case 4:
				return new PowerUp(x, y, ID.PU_DecreasePaddle, new Color(0xFF3E00));
			default:
				return null;
		}
	}

	public static Paddle makePaddle() {
		return new Paddle(Game.WIDTH / 2, (Game.HEIGHT / 20) * 18);
	}


}
