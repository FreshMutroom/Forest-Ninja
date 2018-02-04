import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter
{
	
	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		
		if (x > 648 && x < 934 && y > 383 && y < 526)
		{
			Game.inMenu = false;
			Game.game.removeMouseListener(this);
		}
		else if (x > 650 && x < 933 && y > 574 && y < 723)
		{
			System.exit(1);
		}
	}
	
}
