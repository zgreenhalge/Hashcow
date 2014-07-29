package guiElements;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface Component {

	public void setName(String s);
	public String getName();
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public void setLocation(int x, int y);
	public void setHidden(boolean b);
	public boolean isHidden();
	public boolean isReporting();
	public void setReport(boolean b);
	public boolean contains(int x, int y);
	public boolean mouseClick(int button, int x, int y);
	public boolean mouseMove(int oldx, int oldy, int newx, int newy);
	public void render(GameContainer container, StateBasedGame game, Graphics g);
	public void update(GameContainer container, StateBasedGame game, int delta);
	
}
