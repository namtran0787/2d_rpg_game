package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	
	/* 
	 * SCREEN SETTINGS 
	 */
	
	final int originalTileSize = 16; // 16x16 tile
	final float scale = (float) 2.5;
	public final int tileSize = (int) (originalTileSize * scale); // 48x48 tile
	public final int maxScreenCol = 16; 
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768px
	public final int screenHeight = tileSize * maxScreenRow; // 576px
	
	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxScreenCol;
	public final int worldHeight = tileSize * maxScreenRow;
	
	// FPS
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	public Player player = new Player(this, keyH);
	
	// Set player's default position
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true); 
		// With this, this gamePanel can be focused to receive key input
	}
	
	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
	}

	/*
	 * CÁCH 1
	 *
//	public void run() {	

//		double drawInterval = 1000000000/FPS; // 0.01666 seconds
//		double nextDrawTime = System.nanoTime() + drawInterval;
//		
//		
// 		while(gameThread != null) {
//			
//			
//			// 1 UPDATE: update information such as character positions
//			update();
//			
//			// 2 DRAW: draw the screen with the updated information
//			repaint();
//			
//			
//			try {
//				double remainingTime = nextDrawTime - System.nanoTime();
//				remainingTime = remainingTime/1000000;
//				
//				if(remainingTime < 0) {
//					remainingTime = 0;
//				}
//				
//				Thread.sleep((long) remainingTime);
//				System.out.println(remainingTime);
//				
//				nextDrawTime += drawInterval;
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		As long as this gameThread exits, 
//		it repeats the process that is written inside of these brackets
//	}
 */
	
	/*
	 * CÁCH 2
	 */	
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		long timer = 0;
		int drawCount = 0;
		
		while (gameThread != null) {
			
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if (delta >= 1) {
				
				update();
				repaint();
				
				delta--;
				drawCount++;
			}
			
			if(timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
//			System.out.println("current time :" + currentTime);
//			System.out.println("last time :" + lastTime);
//			System.out.println("delta :" + delta);
		}
	}
		
	/*
	 * CÁCH 3
	 *	
//	public void run() {
//		
//		final int TARGET_FPS = 60;
//		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
//		long lastUpdateTime = System.nanoTime();
//		long lastRenderTime = System.nanoTime();
//
//		while (gameThread != null) {
//		    long now = System.nanoTime();
//		    long updateLength = now - lastUpdateTime;
//		    lastUpdateTime = now;
//
//		    long renderTime = System.nanoTime() - lastRenderTime;
//		    lastRenderTime = System.nanoTime();
//		    long sleepTime = (OPTIMAL_TIME - renderTime) / 1000000;
//
//		    if (sleepTime > 0) {
//		        try {
//					update();
//					repaint();
//		            Thread.sleep(sleepTime);
//		        } catch (InterruptedException e) {
//		            e.printStackTrace();
//		        }
//		    }
//		}
//	}
	
	/*
	 * The purpose of this loop is to maintain a consistent frame rate (FPS) for the
	 * game by regulating the speed at which the game logic updates and the graphics
	 * are redrawn. The delta variable ensures that the game logic is updated only
	 * when the desired frame rate is achieved. The update() method is responsible
	 * for updating the game state (e.g., moving objects, checking for collisions),
	 * and the repaint() method is responsible for redrawing the graphics based on
	 * the updated state.
	 */
	
	public void update() {
		
		player.update();
	}
	
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g; // We change this Graphics g to this Graphics2D
		
		tileM.draw(g2); // Draw tiles first, then player
		player.draw(g2);
		
		g2.dispose();
		// Dispose of this graphics context and release any system resources that it's using
	}
	
}
