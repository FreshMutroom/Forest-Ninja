import java.io.FileInputStream;
import java.io.IOException;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


/**
 * 
 * Things to do:
 *  - implement game over
 */


public class Game extends Canvas implements Runnable 
{

	private static final long serialVersionUID = 1L; 
	
	boolean running = false;
	private Thread thread;
	public static boolean inMenu = true;
	
	static Game game;
	static Handler handler;
	static Player player;
	static Camera camera;
	static Level level;
	
	static int WIDTH = 1024;
	static int HEIGHT = 768;
	BufferedImage background, foreground, menu;
	
	private void init()
	{
		handler = new Handler();
		
		level = new Level("Assets/level_data/level_1.png");
		
		Enemy.loadEnemySprites();
		
		try {
			background = ImageIO.read(new FileInputStream("Assets/backgrounds/level_1.png"));
			foreground = ImageIO.read(new FileInputStream("Assets/backgrounds/background_front.png"));
			menu = ImageIO.read(new FileInputStream("Assets/menu/main_menu.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		camera = new Camera(0.0f, 0.0f);
		
		this.addKeyListener(new KeyInput(handler));
		this.addMouseListener(new MouseInput());
	}
	
	public synchronized void start()
	{
		if (running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void run()
	{
		init();
		this.requestFocus();
		
		// game loop
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0d;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while (delta >= 1)
			{
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				System.out.println("FPS: " +  frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
		
	}

	private void tick()
	{
		
		handler.tick();
		camera.tick(player);
		
	}
	
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) 
		{
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;
		
		// drawing starts here
		
		g2.translate(camera.getX(), camera.getY());	// start of camera
		
		for (int x = -background.getWidth(); x < background.getWidth() * 10; x += background.getWidth())
		{
			g.drawImage(background, x, -500, this);
		}
		
		handler.render(g);
		
		for (int x = -foreground.getWidth(); x < foreground.getWidth() * 10; x += foreground.getWidth())
		{
			g.drawImage(foreground, x, 525, this);
		}
		
		g2.translate(-camera.getX(), -camera.getY());	// end of camera
		
		player.drawHealthBar(g);
		
		// drawing stops here
		
		if (inMenu)
		{
			g.drawImage(menu, 0, 0, this);
		}
		
		g.dispose();
		bs.show();	
		
	}
	
	public static void main(String[] args) {
		new Window(new Game());
	}

	
}

