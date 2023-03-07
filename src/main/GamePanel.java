package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	
	/* 
	 * SCREEN SETTINGS 
	 */
	
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;
	final int tileSize = originalTileSize * scale; // 48x48 tile
	final int maxScreenCol = 16; 
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768px
	final int screenHeigh = tileSize * maxScreenRow; // 576px
	
	// FPS
	int FPS = 60;
	
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	
	// Set player's default position
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeigh));
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
////		As long as this gameThread exits, 
////		it repeats the process that is written inside of these brackets
//	}
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
	 * The purpose of this loop is to maintain a consistent frame rate (FPS) for the
	 * game by regulating the speed at which the game logic updates and the graphics
	 * are redrawn. The delta variable ensures that the game logic is updated only
	 * when the desired frame rate is achieved. The update() method is responsible
	 * for updating the game state (e.g., moving objects, checking for collisions),
	 * and the repaint() method is responsible for redrawing the graphics based on
	 * the updated state.
	 */
	
	public void update() {
		
		if (keyH.upPressed == true) {
			playerY -= playerSpeed;
		}
		else if (keyH.downPressed == true) {
			playerY += playerSpeed;
		}
		else if (keyH.leftPressed == true) {
			playerX -= playerSpeed;
		}
		else if (keyH.rightPressed == true) {
			playerX += playerSpeed;
		}
	}
	
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g; // We change this Graphics g to this Graphics2D
		
		g2.setColor(Color.white);
		g2.fillRect(playerX, playerY, tileSize, tileSize);
		// Draw a rectangle and fills it with the specified color
		g2.dispose();
		// Dispose of this graphics context and release any system resources that it's using
	}
	
}
