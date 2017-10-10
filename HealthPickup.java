import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

public class HealthPickup extends GameObject
{
	Rectangle bounds;
	BufferedImage sprite;
	
	public HealthPickup(float x, float y, float width, float height, GameObjectID ID)
	{
		super(x, y, width, height, 0, ID);
		bounds = new Rectangle((int) x, (int) y, (int) width, (int) height);
		loadSprite();
	}

	public void loadSprite()
	{
		try {
			sprite = ImageIO.read(new FileInputStream("Assets/pickups/health.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void tick(ConcurrentLinkedQueue<GameObject> obj) {
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(sprite, (int) x, (int) y, Game.game);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public Rectangle getBoundsLeft() {
		return null;
	}

	@Override
	public Rectangle getBoundsRight() {
		return null;
	}

	@Override
	public Rectangle getBoundsAbove() {
		return null;
	}

	@Override
	public Rectangle getBoundsBelow() {
		return null;
	}

	@Override
	public void takeDamage(float damage) {
		
	}
}
