import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.imageio.ImageIO;

public class Player extends GameObject
{
	float previousVelX = 0.0f, previousVelY = 0.0f;
	boolean shooting = false;
	public float health = 100.0f;
	public static final float SPRITE_SCALE = 0.2f;
	private static BufferedImage[] idle, running;
	public static BufferedImage[][] ammo;
	private int count = 0;
	private int index = 0;
	public static int staticIndex = 0;
	public static int rateOfFire = 25;	// lower = faster
	public static int equippedWeapon;
	private int facing = 1;
	public static int fireCount = 0;
	
	public Player(float x, float y, float width, float height, GameObjectID ID, Handler handler)
	{
		super(x, y, width, height, 0.5f, ID);
		try {
			getTextures();
		} catch (IOException e) {
			e.printStackTrace();
		}
		equippedWeapon = 0;
	}
	
	private void decideSpriteNumber()
	{
		if (count < 7)
		{
			count++;
		}
		else
		{
			if (!(index == running.length - 1))
			{
				index++;
				count = 0;
			}
			else 
			{
				index = 0;
			}
		}
	}
	
	public void tick(ConcurrentLinkedQueue <GameObject> obj) 
	{
		
		decideSpriteNumber();
		
		if (health <= 0.0f)
		{
			// game over
		}
		
		x += velX;
		
		// check to stay inside map
		if (y + velY > Game.level.lowerLimitY && y + velY < Game.level.upperLimitY)
		{
			y += velY;
		}
		
		checkCollision(obj);
		
		if (velX > 0.0f)
		{
			facing = 1;
		}
		else if (velX < 0.0f)
		{
			facing = -1;
		}
		
		if (fireCount % rateOfFire != 0)
			fireCount++;
		
		if (isShooting())
		{
			if (fireCount % rateOfFire == 0)
			{
				Game.handler.addObject(new Bullet(x + 50, y + 20, 
						5, 25, 1, getDamage(), GameObjectID.PLAYER_BULLET, 0, 8));
				staticIndex++;
				if (staticIndex == 4)
				{
					staticIndex = 0;
				}
			}
			fireCount++;
		}
		
		// check to see if to spawn enemies
		while (!Enemy.waitingToSpawn.isEmpty() 
				&& Enemy.waitingToSpawn.peek().getX() < getX() + Game.WIDTH * 0.80f)
		{
			Enemy enemy = Enemy.waitingToSpawn.poll();
			Game.handler.addObject(enemy);
		}
	}
	
	private void checkCollision(ConcurrentLinkedQueue <GameObject> objects)
	{
		for (GameObject object : objects)
		{
			if (object.ID == GameObjectID.WALL)
			{
				if (getBounds().intersects(object.getBounds()))
				{
					
					if (getBoundsLeft().intersects(object.getBounds())) 		
					{
						x = object.getX() + object.getWidth();
					}
					else if (getBoundsRight().intersects(object.getBounds())) 
					{
						x = object.getX() - width;
					}
					else if (getBoundsAbove().intersects(object.getBounds())) 
					{
						y = object.getY() + object.getHeight();
					}
					else if (getBoundsBelow().intersects(object.getBounds())) 
					{
						y = object.getY() - height;
					}
				}
			}
			else if (object.ID == GameObjectID.ENEMY)
			{
				if (getBounds().intersects(object.getBounds()))
				{
					takeDamage(0.5f);
				}
			}
			else if (object.ID == GameObjectID.HEALTH_PICKUP)
			{
				if (getBounds().intersects(object.getBounds()))
				{
					healDamage(100.0f);
					Game.handler.removeObject(object);
				}
			}
		}
	}
	
	private void getTextures() throws IOException
	{
		idle = new BufferedImage[10];
		for (int i = 0; i < 10; i++)
		{
			idle[i] = ImageIO.read(new FileInputStream("Assets/player_sprites/idle/00" + i + ".png"));
		}
		
		running = new BufferedImage[10];
		for (int i = 0; i < 10; i++)
		{
			running[i] = ImageIO.read(new FileInputStream("Assets/player_sprites/running/00" + i + ".png"));
		}
		
		ammo = new BufferedImage[4][8];
		
		for (int i = 0; i < 4; i++)
		{
			ammo[0][i] = ImageIO.read(new FileInputStream("Assets/player_sprites/ammo/type_0/00" + i + ".png"));
		}
		
	}
	
	@Override
	public void takeDamage(float damage)
	{
		health -= damage;
		if (health < 0.0f)
		{
			health = 0.0f;
		}
	}
	
	private void healDamage(float amount)
	{
		health += amount;
		if (health > 100.0f)
		{
			health = 100.0f;
		}
	}
	
	public void drawHealthBar(Graphics g)
	{
		g.setColor(Color.YELLOW);
		int borderX = (int) (Game.WIDTH * 0.05);
		int borderY = (int) (Game.HEIGHT * 0.95);
		g.drawRect(borderX,  borderY, 101, 30);
		
		g.setColor(Color.RED);
		g.fillRect(borderX + 1, borderY + 1, (int) health, 28);
	}
	
	public void render(Graphics g) 
	{
		
		if (velX != 0.0f || velY != 0.0f)
		{
			if (facing == 1)
			{
				g.drawImage(Player.running[index], (int) x, (int) y,  Game.game);
			}
			else 
			{
				g.drawImage(Player.running[index], (int) x + 40, (int) y, (int) (-Player.running[index].getWidth()), 
						(int) (Player.running[index].getHeight()), Game.game);
			}
		}
		else if (facing == 1)
		{
			g.drawImage(Player.idle[index], (int) getX(), (int) getY(),  Game.game);	
		}
		else 
		{
			g.drawImage(Player.idle[index], (int) getX() + 46, (int) getY(), (int) (-Player.idle[index].getWidth()), 
					(int) (Player.idle[index].getHeight()), Game.game);
		}
		
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) x + 10, (int) y + 3, (int) width - 10, (int) height + 5);
	}
	
	@Override
	public Rectangle getBoundsLeft() 
	{
		return new Rectangle((int) x, (int) y + (int)height/10, 
				(int) width/10, (int)(height - height/5));
	}

	@Override
	public Rectangle getBoundsRight() 
	{
		return new Rectangle((int) (x + width - width/10), (int) (y + height/10), 
				(int) (width/10), (int) (height - height/5));
	}

	@Override
	public Rectangle getBoundsAbove() 
	{
		return new Rectangle((int) (x + width/10), (int) y + 3, (int) (width * 0.8f), (int) (height/2));
	}

	@Override
	public Rectangle getBoundsBelow() 
	{
		return new Rectangle((int) (x + width/10), (int) (y + height/2), (int) (width * 0.8f), (int) (height/2));
	}

	public boolean isShooting()
	{
		return shooting;
	}

	public void setShooting(boolean bool)
	{
		shooting = bool;
	}
	
}
