import java.awt.Graphics;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Handler 
{
	
	ConcurrentLinkedQueue <GameObject> objects = new ConcurrentLinkedQueue <GameObject> ();
	
	public void tick()
	{
		for (GameObject obj : objects)
		{
			obj.tick(objects);
		}
	}
	
	public void render(Graphics g)
	{
		for (GameObject obj : objects)
		{
			obj.render(g);
		}
	}
	
	public void addObject(GameObject obj)
	{
		objects.add(obj);
	}
	
	public void removeObject(GameObject obj)
	{
		objects.remove(obj);
	}
}
