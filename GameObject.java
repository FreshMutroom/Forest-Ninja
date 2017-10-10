import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class GameObject 
{

	protected float x, y;
	protected float velX, velY;
	protected float width, height;
	protected GameObjectID ID;
	protected Handler handler;
	protected float damage;
	
	public GameObject(float x, float y, float width, float height, 
			float damage, GameObjectID ID)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.ID = ID;
		this.damage = damage;
	}
	
	public abstract void tick(ConcurrentLinkedQueue <GameObject> obj);
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	public abstract Rectangle getBoundsLeft();
	public abstract Rectangle getBoundsRight();
	public abstract Rectangle getBoundsAbove();
	public abstract Rectangle getBoundsBelow();
	public abstract void takeDamage(float damage);
	
	public float getX() 
	{
		return x;
	}
	public float getY()
	{
		return y;
	}
	public void setX(float x)
	{
		this.x = x;
	}
	public void setY(float y)
	{
		this.y = y;
	}
	
	public float getVelX() 
	{
		return velX;
	}
	public float getVelY()
	{
		return velY;
	}
	public void setVelX(float velX)
	{
		this.velX = velX;
	}
	public void setVelY(float velY)
	{
		this.velY = velY;
	}
	public float getWidth()
	{
		return width;
	}
	public float getHeight()
	{
		return height;
	}
	public float getDamage()
	{
		return damage;
	}
	public void setDamage(float damage)
	{
		this.damage = damage;
	}
	
}
