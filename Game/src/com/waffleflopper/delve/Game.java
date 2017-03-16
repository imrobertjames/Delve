package com.waffleflopper.delve;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.waffleflopper.delve.graphics.Screen;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	public static String title = "Delve";

	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private Screen screen;

	// create the image
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	// access the image
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size); // this method is in Canvas

		screen = new Screen(width, height);

		frame = new JFrame();
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, Game.title);
		thread.start(); // run() method called automatically when we create this
						// thread, because we implement runnable
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // because why not, GET THAT STACK TRACE BITCH
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0; // 60 is how many times i want
												// this to happen a second
		double delta = 0;

		int frames = 0; //how many frames we render in a second
		int updates = 0; //how many times we update every second - should be 60.
		
		while (running) {
			// need an update or tick method (handles logic)
			/*
			 * Basically what we are doing is calculating how long is taking place during each loop
			 * and when that amount of time is greater than or equal to 1 second, update the logic
			 */
			long now = System.nanoTime();  
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}

			render(); // whatever
			frames++;
			
			if (System.currentTimeMillis() - timer > 1){
				timer += 1000;  //make sure we don't get stuck in here.
				//System.out.println("FPS: " + frames + " Updates: " + updates);
				frame.setTitle(title + " | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
	}

	public void update() { // we will limit this to running 60 times a second,
							// for continuity between different systems.

	}

	public void render() { // can run as many times as it can, for better
							// performance across computers
		BufferStrategy bs = getBufferStrategy(); // getBufferStrategy retrieves
													// parent 'Canvas'
													// BufferStrategy object
		if (bs == null) { // only want to create it once, so only do it if it's
							// null
			createBufferStrategy(3);
			// Triple buffering - pretty much always want to do this (allows 2
			// frames to be ready to go after current displayed one)
			return;
		}
		screen.clear(); // clear the screen before we render it again
		screen.render();

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		{
			// all your graphics are done here, before g.dispose();
			// g.setColor(Color.BLACK); // sets graphic color to black, call
			// this
			// before you draw rectangle
			// g.fillRect(0, 0, getWidth(), getHeight()); // makes sure it fill
			// the
			// whole screen area

			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}
		g.dispose(); // disposes of the current graphics. Releases system
						// resources. Don't crash your computer by leaving this
						// out.
		bs.show(); // show the buffer that's waiting

	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false); // first thing that should be applied to
										// frame!
		game.frame.setTitle("Delve");
		game.frame.add(game); // adds the game component to the frame
		game.frame.pack(); // sets the resolution I believe
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true); // actually shows the frame.

		game.start();
	}
}
