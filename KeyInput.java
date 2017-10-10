import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KeyInput extends KeyAdapter
{
	Handler handler;
	HashSet <Integer> set = new HashSet <Integer> ();
	Set <Integer> currentlyPressedKeys;
	
	final float deltaX = 4.0f;
	
	public KeyInput(Handler handler)
	{
		this.handler = handler;
		currentlyPressedKeys = Collections.synchronizedSet(set);
	}
	
	public void keyPressed(KeyEvent e)
	{
		int keyPress = e.getKeyCode();
		
		if (Game.inMenu)
		{
			if (keyPress == KeyEvent.VK_ENTER)
			{
				Game.inMenu = false;
				Game.game.removeMouseListener(Game.game.getMouseListeners()[0]);
			}
			return;
		}
		
		if (keyPress == KeyEvent.VK_SPACE)
		{
			switch (Player.equippedWeapon)
			{
				case 0:
					if (!currentlyPressedKeys.contains(KeyEvent.VK_SPACE))
					{
						currentlyPressedKeys.add(KeyEvent.VK_SPACE);
					}
					if (Player.fireCount % Player.rateOfFire == 0)
					{	Game.handler.addObject(new Bullet(Game.player.getX() + 50, Game.player.getY() + 20, 5, 25, 1, 6, 
								GameObjectID.PLAYER_BULLET, 0, 8));
					}
					Game.player.setShooting(true);
					break;
					
				default:
					break;
			}
			
		}
		else if (keyPress == KeyEvent.VK_A)
		{
			//Game.player.velX -= deltaX;
			currentlyPressedKeys.add(keyPress);
			if (currentlyPressedKeys.contains(KeyEvent.VK_D))
				Game.player.setVelX(0.0f);
			else	
				Game.player.setVelX(-deltaX);
		}
		else if (keyPress == KeyEvent.VK_D)
		{
			currentlyPressedKeys.add(keyPress);
			if (currentlyPressedKeys.contains(KeyEvent.VK_A))
				Game.player.setVelX(0.0f);
			else	
				Game.player.setVelX(deltaX);
		}
		else if (keyPress == KeyEvent.VK_W)
		{
			currentlyPressedKeys.add(keyPress);
			if (currentlyPressedKeys.contains(KeyEvent.VK_S))
				Game.player.setVelY(0.0f);
			else	
				Game.player.setVelY(-deltaX);
		}
		else if (keyPress == KeyEvent.VK_S)
		{
			currentlyPressedKeys.add(keyPress);
			if (currentlyPressedKeys.contains(KeyEvent.VK_W))
				Game.player.setVelY(0.0f);
			else	
				Game.player.setVelY(deltaX);
		}
		else if (keyPress == KeyEvent.VK_ESCAPE)
			System.exit(1);
	}
	
	
	public void keyReleased(KeyEvent e)
	{
		if (Game.inMenu)
			return;
		
		int keyPress = e.getKeyCode();
		
		if (keyPress == KeyEvent.VK_SPACE)
		{
			Game.player.setShooting(false);
			currentlyPressedKeys.remove(KeyEvent.VK_SPACE);
		}
		else if (keyPress == KeyEvent.VK_A)
		{
			currentlyPressedKeys.remove(keyPress);
			if (!currentlyPressedKeys.contains(KeyEvent.VK_D))
			{
				Game.player.setVelX(0.0f);
			}
			else 
			{
				Game.player.setVelX(deltaX);
			}
			
		}
		else if (keyPress == KeyEvent.VK_D)
		{
			currentlyPressedKeys.remove(keyPress);
			
			
			if (!currentlyPressedKeys.contains(KeyEvent.VK_A))
			{
				Game.player.setVelX(0.0f);
			}
			else 
			{
				Game.player.setVelX(-deltaX);
			}
		}
		else if (keyPress == KeyEvent.VK_W)
		{
			currentlyPressedKeys.remove(keyPress);
			if (!currentlyPressedKeys.contains(KeyEvent.VK_S))
			{
				Game.player.setVelY(0.0f);
			}
			else 
			{
				Game.player.setVelY(deltaX);
			}
		}
		else if (keyPress == KeyEvent.VK_S)
		{
			currentlyPressedKeys.remove(keyPress);
			if (!currentlyPressedKeys.contains(KeyEvent.VK_W))
			{
				Game.player.setVelY(0.0f);
			}
			else 
			{
				Game.player.setVelY(-deltaX); 
			}
		}
	}
}














