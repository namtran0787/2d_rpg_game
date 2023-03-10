package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
	
	GamePanel gp;
	public Tile[] tile; 
	public int mapTileNum[][];
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		tile = new Tile[10];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		loadMap("/maps/world01.txt");
	}
	
	public void getTileImage() {
		
		try {
			
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass01.png"));
			
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			tile[1].collision = true;
			
			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water01.png"));
			tile[2].collision = true;

			tile[3] = new Tile();
			tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
			
			tile[4] = new Tile();
			tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
			tile[4].collision = true;

			tile[5] = new Tile();
			tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String filePathMap) {
		try {
			InputStream is = getClass().getResourceAsStream(filePathMap);
			// We are going to use lot of maps in project, so we should add parameter is filePath
			// and assign it in above.
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
				
				String line = br.readLine();
//				System.out.println(line);
				while(col < gp.maxWorldCol) {
					
					String numbers[] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[col][row] = num; // NOTE: mapTileNum is using in draw method
					col++;
				}
					
				if (col == gp.maxWorldCol) {
					col = 0;
					row ++;
				}
			}
			br.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;
//		int x = 0;
//		int y = 0;
		
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			
			int tileNum = mapTileNum[worldCol][worldRow];
			
			int worldMapX = worldCol * gp.tileSize;
			int worldMapY = worldRow * gp.tileSize;
			int screenX = worldMapX - gp.player.worldX + gp.player.screenX;
			int screenY = worldMapY - gp.player.worldY + gp.player.screenY;
		
			if ( worldMapX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
				 worldMapX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
				 worldMapY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
				 worldMapY - gp.tileSize < gp.player.worldY + gp.player.screenY) 
			{
				g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			}
			// Draw specified tile in map more and more ...
			
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;				
				worldRow++;
			} 
		}
		// we draw map by using while loop, because the max col of map is 16 tiles so if 
		// it come maximum col of map, it will drop under with col = 0 and row++ ( it's mean
		// + 48px per row.

	}
}
