package guiElements;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.state.StateBasedGame;

public interface GUI extends InputListener {

	public void render(GameContainer container, StateBasedGame game, Graphics g);
	public void update(GameContainer container, StateBasedGame game, int delta);
	public void add(Component c);
	public void remove(Component c);
}
