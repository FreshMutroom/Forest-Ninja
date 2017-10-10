
public class Camera 
{
	
	private float x, y;
	
	public Camera(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void tick(GameObject player)
	{
		
		float xTarg = -player.getX() - player.getWidth()/2.0f + Game.WIDTH/3;	
		x += (xTarg - x) * 0.05;
		
		float yTarg = -player.getY() - player.getHeight()/2.0f + Game.HEIGHT/2;
		y += (yTarg - y) * 0.05;
		
	}
	
}
