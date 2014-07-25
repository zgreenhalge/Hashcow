package guiElements;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface Renderable {

	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public void render(GameContainer container, Graphics g);
	
}
