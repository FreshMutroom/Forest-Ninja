import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bullet extends GameObject
{
	
	int direction;
	int distance = 0;
	final int MAX_DISTANCE = 300;
	int type;
	int index = 0;
	int count = 0;
	
	public Bullet(float x, float y, float width, float height, int direction, 
			float damage, GameObjectID ID, int type, float speed) {
		
		super(x, y, width, height, damage, ID);
		velX = speed * direction;
		this.direction = direction;
		this.type = type;
		index = Player.staticIndex;
	}

	@Override
	public void tick(ConcurrentLinkedQueue <GameObject> obj) {
		
		count++;
		if (count % 10 == 0)
		{
			index++;
			if (index == 4)
				index = 0;
		}
		
		x += velX;
		distance++;
		if (distance > MAX_DISTANCE)
		{
			Game.handler.removeObject(this);
		}
		 
		else if (ID == GameObjectID.PLAYER_BULLET)
		{
			for (GameObject object : obj)
			{
				if (object.ID != GameObjectID.PLAYER_BULLET && object.ID != GameObjectID.PLAYER 
						&& object.ID != GameObjectID.ENEMY_BULLET && object.ID != GameObjectID.RAPIDFIRE_PICKUP
						&& object.ID != GameObjectID.HEALTH_PICKUP && getBounds().intersects(object.getBounds()))
				{
					if (object.ID == GameObjectID.ENEMY ) 
					{
						Game.handler.removeObject(this);
						object.takeDamage(damage);
					}
					// bullet has hit something
					Game.handler.removeObject(this);
					
				}
			}
		}
		else if (ID == GameObjectID.ENEMY_BULLET)
		{
			for (GameObject object : obj)
			{
				if (object.ID != GameObjectID.ENEMY_BULLET && object.ID != GameObjectID.ENEMY 
						&& object.ID != GameObjectID.PLAYER_BULLET&& getBounds().intersects(object.getBounds()))
				{
					if (object.ID == GameObjectID.PLAYER)
					{
						// bullet has hit player
						Game.handler.removeObject(this);
						Game.player.takeDamage(damage);
					}
					else
					{
						// bullet has hit something else
						Game.handler.removeObject(this);
					}
				}
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		
		// type 0 = player, type 1 = enemy 1, type 2 = enemy 2 ...
		switch (type)
		{
			
		case 0:
			int offsetX = 0;
			int offsetY = 0;
			if (index % 2 == 1)
			{
				offsetX = -13;
				offsetY = 10;
			}
			g.drawImage(Player.ammo[0][index], (int) x + offsetX, (int) y + offsetY, Game.game); 
			break;
		
		case 1:
			// image 5 never gets drawn because index stops at 3
			g.drawImage(Enemy.bullet[0][index], (int) x + 30, (int) y, -Enemy.bullet[0][index].getWidth(), 
					Enemy.bullet[0][index].getHeight(), Game.game);
			break;
	
		case 2:
			g.drawImage(Enemy.bullet[1][index], (int) x + 30, (int) y, Game.game);
			break;
			
		default:
			
			break;
			
		
		
		}
	}

	@Override
	public Rectangle getBounds() {
		switch (type)
		{
		case 1:	
			return new Rectangle((int) x, (int) y + 3, (int) width, (int) height);
			
		case 2:
			return new Rectangle((int) x + 36, (int) y + 3, (int) width + 6, (int) height);
			
		default:
			return new Rectangle((int) x, (int) y + 3, (int) width, (int) height);
			
		}
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
