import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

public class Enemy extends GameObject
{
	int type;
	int health, bulletX, bulletY;
	float bulletSpeed;
	Random random = new Random();
	int chanceToFire;
	int numMovesBack = 0;
	boolean movingBack = false;
	boolean slowingDown = false;
	boolean destroyed = false;
	boolean movingSideways = false;
	float xTarget, yTarget;
	static PriorityQueue <Enemy> waitingToSpawn = new PriorityQueue <Enemy> (new EnemyPriority());
	int count = 0;
	int index = 0;
	static BufferedImage[][] destroy, bullet, running, melee;
	
	public Enemy(float x, float y, float width, float height, 
			GameObjectID ID, int type, Handler handler) {
		super(x, y, width, height, 0, ID);
		this.type = type;
		init(type);
	}
	
	public static void loadEnemySprites()
	{
		
		destroy = new BufferedImage[4][10];
		running = new BufferedImage[4][10];
		bullet = new BufferedImage[4][5];
		
		for (int i = 0; i < 10; i++)
		{
			try {
				destroy[0][i] = ImageIO.read(new FileInputStream("Assets/enemy_sprites/type_1/destroyed/00" + i + ".png"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < 9; i++)
		{
			try {
				running[0][i] = ImageIO.read(new FileInputStream("Assets/enemy_sprites/type_1/running_and_shooting/00" + i + ".png"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < 5; i++)
		{
			try {
				bullet[0][i] = ImageIO.read(new FileInputStream("Assets/enemy_sprites/type_1/ammo/00" + i + ".png"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		for (int i = 0; i < 10; i++)
		{
			try {
				destroy[1][i] = ImageIO.read(new FileInputStream("Assets/enemy_sprites/type_2/destroy/00" + i + ".png"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < 10; i++)
		{
			try {
				running[1][i] = ImageIO.read(new FileInputStream("Assets/enemy_sprites/type_2/running/00" + i + ".png"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < 5; i++)
		{
			try {
				bullet[1][i] = ImageIO.read(new FileInputStream("Assets/enemy_sprites/type_2/ammo/00" + i + ".png"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void init(int type)
	{
		switch (type) 
		{
			case 1:
				health = 20;
				setDamage(3.0f);
				bulletX = 20;
				bulletY = 20;
				chanceToFire = 3;
				bulletSpeed = 3.5f;
				break;
			
			case 2:
				health = 30;
				setDamage(5.0f);
				bulletX = 5;
				bulletY = 3;
				chanceToFire = 9;
				bulletSpeed = 5.0f;
				break;
			
			default:
				break;
		}
	}
	
	public void destroy()
	{
		Game.handler.removeObject(this);
	}
	
	public void takeDamage(float damage)
	{
		if (!destroyed)
		{
			health -= damage;
			if (health <= 0.0f)
			{
				count = 0;
				index = 0;
				destroyed = true;
			}
		}
	}
	
	@Override
	public void tick(ConcurrentLinkedQueue<GameObject> obj) {
		if (destroyed)
		{
			if (count < 7)
			{
				count++;
			}
			else
			{
				if (!(index == destroy[type - 1].length - 1))
				{
					index++;
					count = 0;
				}
				else
				{
					destroy();
				}
				
			}
			return;
		}
		
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
		
		if (random.nextInt(100) < chanceToFire)
		{
			Bullet bullet = new Bullet(x, y + 44, bulletX, bulletY, -1, damage, 
					GameObjectID.ENEMY_BULLET, type, bulletSpeed);
			Game.handler.addObject(bullet);
		}
		
		// move backwards?
		if (movingBack || slowingDown || getX() - Game.player.getX() < 375)
		{
			
			if (!slowingDown)
			{
				movingBack = true;
				velX += 0.1f;
				
				if (velX > 6.0f)
				{
					velX = 6.0f;
				}
				
				x += velX;
				numMovesBack++;
			}
			else
			{
				velX -= 0.12f;
				if (velX < 0.0f)
				{
					velX = 0.0f;
					slowingDown = false;
				}
				x += velX;
			}
			if (numMovesBack == 60)
			{
				// start slowing down
				movingBack = false;
				slowingDown = true;
				numMovesBack = 0;
			}
		}
		
		// move sideways?
		if (movingSideways || random.nextInt(100) < 2)
		{
			if (!movingSideways)
			{
				yTarget = Game.player.getY();
			}
				
			movingSideways = true;
			
			if (Game.player.getY() > y)
			{
				velY = 2.0f;
			}
			else
			{
				velY = -2.0f;
			}
			y += velY;
			
			if (y < yTarget + 1.5f && y > yTarget - 1.5f)
			{
				movingSideways = false;
			}
		}
	
	}
	
	@Override
	public void render(Graphics g) 
	{
		
		if (!destroyed) 
		{
			g.drawImage(running[type - 1][index], (int) (x + running[type - 1][index].getWidth()/2), (int) y, 
					(int) -running[type - 1][index].getWidth(), running[type - 1][index].getHeight(),  Game.game);
		}
		else
		{
			g.drawImage(destroy[type - 1][index], (int) (x + destroy[type - 1][index].getWidth()/2.5f), (int) y, 
					(int) -destroy[type - 1][index].getWidth(), destroy[type - 1][index].getHeight(),  Game.game);
		}
		
	}
	
	@Override
	public Rectangle getBounds() {
		if (!destroyed)
			return new Rectangle((int) x - 20, (int) y + 10, (int) width, (int) height);
		else
			return new Rectangle(0, 0, 0, 0);
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
	
}


class EnemyPriority implements Comparator <Enemy>
{
	public int compare(Enemy e1, Enemy e2)
	{
		return (int) (e1.x - e2.x);
	}
}



























