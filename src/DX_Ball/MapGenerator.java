package DX_Ball;

import DX_Ball.GameObjects.Block;
import DX_Ball.GameObjects.ObjectFactory;

import java.io.File;
import java.util.Scanner;

/********************
 * MapGenerator
 *
 * The mapGenerator is given a .txt file and translate the data into a blockLayout
 * which is later given to the handler to create the level.
 *
 * " " = no block
 * g = green StandardBlock
 * c = cyan StandardBlock
 * p = purple StandardBlock
 * b = blue StandardBlock
 * r = red StandardBlock
 * t = ThoughBlock
 * i = InvisibleBlock
 * e = ExplosiveBlock
 ********************/
public class MapGenerator {


	private File mapGenerator;
	private Scanner input;
	private String[][] levelLayout = new String[10][10];
	private Block[][] blockLayout = new Block[10][10];

	//Loads the the File for the level that was given and reads the file.
	public MapGenerator(int level) {
		mapGenerator = new File("./Levels/level" + level);
		try{
			input = new Scanner(mapGenerator);
		} catch (Exception e) {
			System.out.println("Problem loading file");
		}
		readFile();
	}

	//Reads the file
	private void readFile() {
		try {
			int row = 0;
			while (input.hasNextLine()) {
				//Split the string at every '|' and put each string between those characters into an array
				String[] formattedString = input.nextLine().split("\\|");
				for(int column = 0; column < 10; column++) {
					levelLayout[row][column] = formattedString[column];
				}
				row++;
			}
			//Translate the strings into object using the format specified at the top of this file
			translateLayout();
		} catch (Exception e) {
			System.out.println("Problem reading file");
		}
	}

	//Translate the layout into a 2D Array of Blocks
	private void translateLayout() {
		for(int row = 0; row < 10; row++) {
			for (int column = 0; column < 10; column++) {
				blockLayout[row][column] = ObjectFactory.makeBlock(levelLayout[row][column], column * Block.WIDTH, row * Block.HEIGHT);
			}
		}
	}

	//Return the 2D Block array
	public Block[][] getBlockLayout() {
		return blockLayout;
	}
}
