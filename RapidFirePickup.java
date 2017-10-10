import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

public class RapidFirePickup extends GameObject
{
	final int DURATION = 15;
	final int SPEED_AMPLIFICATION = 3;
	BufferedImage sprite;
	
	int duration = 60 * DURATION;
	boolean pickedUp = false;
	
	public RapidFirePickup(float x, float y, float width, float height, GameObjectID ID)
	{
		super(x, y, width, height, 0, ID);
		loadSprites();
	}
	
	public void loadSprites()
	{
		try {
			sprite = ImageIO.read(new FileInputStream("Assets/pickups/rapid_fire.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void tick(ConcurrentLinkedQueue<GameObject> obj) {

		if (!pickedUp && getBounds().intersects(Game.player.getBounds()))
		{
			pickedUp = true;
			Player.rateOfFire /= SPEED_AMPLIFICATION;
		}
		else if (pickedUp)
		{
			duration--;
			if (duration == 0)
			{
				Player.rateOfFire *= SPEED_AMPLIFICATION;
				Game.handler.removeObject(this);
			}
		}
	}

	@Override
	public void render(Graphics g) 
	{
		if (!pickedUp) 
		{
			g.drawImage(sprite, (int) x, (int) y, Game.game);
		}
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}

	@Override
	public Rectangle getBoundsLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBoundsRight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBoundsAbove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBoundsBelow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void takeDamage(float damage) {
		// TODO Auto-generated method stub
		
	}
	
	
}
