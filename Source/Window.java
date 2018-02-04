import java.awt.Dimension;

import javax.swing.JFrame;

public class Window 
{
	
	public Window(Game game)
	{
		game.setPreferredSize(new Dimension(1024, 768));
		game.setMaximumSize(new Dimension(1024, 768));
		game.setMinimumSize(new Dimension(1024, 768));
		
		JFrame frame = new JFrame("Test window");
		frame.add(game);
		frame.pack();
    	frame.setResizable(false);
    	frame.setLocationRelativeTo(null);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
		
    	Game.game = game;
    	game.start();
	}
	
}
