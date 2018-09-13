package DX_Ball;

import DX_Ball.State.GameState;
import DX_Ball.State.MainMenu;
import DX_Ball.State.State;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

/********************
 * Main class
 *
 * The Main class initialize the handler and keep track of which state the program is in.
 * It is also in charge of the GameLoop
 ********************/
public class Game extends Canvas implements Runnable {
	//Final size of the gameWindow
	public static final int WIDTH = 640;
	public static final int HEIGHT = WIDTH / 12 * 9;

	private Thread gameThread;
	private boolean running = false;

	private State currentState;
	private Handler handler;
	private LinkedList<MenuButton> menuButtons;
	private MouseInput mouseInput = new MouseInput(this);

	//Set the window size and the application name
	//Also sets the current State to MainMenu and initialize the handler and adds a KeyListener & Mouselistener
	public Game() {
		new Window(WIDTH, HEIGHT, "DX_BAALL", this);
		this.addMouseListener(mouseInput);
		setCurrentState(new MainMenu(this));
		this.handler = new Handler(this);
		this.addKeyListener(new KeyInput(handler, this));
	}

	public synchronized void start() {
		gameThread = new Thread(this);
		gameThread.start();
		running = true;
	}


	//GameLoop
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			render();
		}
	}

	//Runs the currentStates tick function
	private void tick() {
		if (currentState != null) {
			currentState.tick();
		}
	}

	//Runs the currentState render function
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;

		if (currentState != null) {
			currentState.render(g2);
		}

		g2.dispose();
		bs.show();
	}

	//Lets other classes change the current state and get which state is the current one
	public void setCurrentState(State state) {
		currentState = state;
		if (!(currentState instanceof GameState)) {
			menuButtons = state.getButtons();
			mouseInput.setButtons(menuButtons);
		}
	}

	public State getCurrentState() {
		return currentState;
	}

	public Handler getHandler() {
		return handler;
	}

	public static void main(String[] args) {
		new Game();
	}
}
