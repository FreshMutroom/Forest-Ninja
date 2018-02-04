import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Level 
{
	
	public static int DRAWING_SCALE = 26;
	float lowerLimitY;
	float upperLimitY;
	
	public Level(String path)
	{
		File file = new File(path);
		init(file, Game.WIDTH, Game.HEIGHT);
		initFields(path);
	}
	
	private void initFields(String level)
	{
		switch (level) 
		{
		case "Assets/level_data/level_1.png":
			DRAWING_SCALE = 26;
			lowerLimitY = 105.0f;
			upperLimitY = 600.0f;
		}
	}
	
	public boolean init(File file, int width, int height)
	{
		BufferedImage image = null;
		try 
		{
			image = ImageIO.read(file);
		} 
		catch (IOException e) 
		{
			System.out.println("File " + file + " not found");
			e.printStackTrace();
			return false;
		}
		   
		for (int y = 0; y < height; y++)  
		{
			for (int x = 0; x < width; x++)
			{
				int clr = image.getRGB(x,y); 
				Color color = new Color(clr, true);
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				
				
				if (red == 0 && green == 0 && blue == 255)	// player
				{
					Game.player = new Player(x * DRAWING_SCALE, y * DRAWING_SCALE, 1.5f * DRAWING_SCALE, 3 * DRAWING_SCALE, 
							GameObjectID.PLAYER, Game.handler);
					Game.handler.addObject(Game.player); 
				}
				else if (red == 255 && green == 0 && blue == 0)	// type 1 enemy
				{
					Enemy.waitingToSpawn.add(new Enemy(x * DRAWING_SCALE, y * DRAWING_SCALE, 1.7f * DRAWING_SCALE, 3.75f * DRAWING_SCALE, 
							GameObjectID.ENEMY, 1, Game.handler)); 
				}
				else if (red == 255 && green == 255 && blue == 0)	// type 2 enemy
				{
					Enemy.waitingToSpawn.add(new Enemy(x * DRAWING_SCALE, y * DRAWING_SCALE, 2 * DRAWING_SCALE, 3 * DRAWING_SCALE, 
							GameObjectID.ENEMY, 2, Game.handler));
				}
				else if (red == 0 && green == 255 && blue == 255)	// health pickup
				{
					Game.handler.addObject(new HealthPickup(x * DRAWING_SCALE, y * DRAWING_SCALE, 45, 45, 
							GameObjectID.HEALTH_PICKUP));
				}
				else if (red == 128 && green == 128 && blue == 128)	// rapid fire pickup
				{
					Game.handler.addObject(new RapidFirePickup(x * DRAWING_SCALE, y * DRAWING_SCALE, 45, 
							45, GameObjectID.RAPIDFIRE_PICKUP));
				}
			}
		} 
				
		return true;	
	}
	
}
