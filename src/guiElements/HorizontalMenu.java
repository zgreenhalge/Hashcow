package guiElements;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A horizontal menu structure that scales dynamically as Buttons are added or removed
 *
 */
public class HorizontalMenu implements Menu{

	private static int lastId = 1;
	
	private String name;
	private int X;
	private int Y;
	private int width;
	private boolean hidden;
	private ArrayList<Button> buttons;
	
	public HorizontalMenu(int X, int Y) {
		this.X = X;
		this.Y = Y;
		width = 0;
		buttons = new ArrayList<Button>();
		name = "HorizontalMenu" + (lastId++);
	} 
	
	/**
	 * Add a Button to the HorizontalMenu
	 * @param b - the Button to be added
	 */
	public void addButton(Button b){
		buttons.add(b);
	}
	
	/**
	 * Remove a Button from the HorizontalMenu
	 * @param b - the Button to be removed
	 */
	public void removeButton(Button b){
		buttons.remove(b);
	}

	/**
	 * Clear the HorizontalMenu. 
	 * More specifically, sets each button to unclickable and then clears the list of buttons.
	 */
	public void clear(){
		for(Button b: buttons)
			b.setHidden(true);
		buttons.clear();
	}

	@Override
	public int getX() {
		return X;
	}

	@Override
	public int getY() {
		return Y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return buttons.get(0).getHeight();
	}

	@Override
	public void setLocation(int x, int y) {
		X = x;
		Y = y;
	}

	@Override
	public void setHidden(boolean b) {
		hidden = b;
		for(Button button: buttons)
			button.setHidden(hidden);
		
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public boolean contains(int x, int y) {
		return new Rectangle(X, Y, getWidth(), getHeight()).contains(x, y);
	}

	@Override
	public boolean mouseClick(int button, int x, int y) {
		for(Button b: buttons)
			if(b.mouseClick(button, x, y))
				return true;
		return false;
	}

	@Override
	public boolean mouseMove(int oldx, int oldy, int newx, int newy) {
		return false;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(buttons.isEmpty())
			return;
		width = 0;
		for(Button b: buttons){
			b.setLocation(X + width, Y);
			b.render(container, game, g);
			if(!b.isHidden())
				width += b.getWidth();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		for(Button b: buttons)
			b.update(container, game, delta);
	}

	@Override
	public void setName(String s) {
		name = s;		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isReporting() {
		return false;
	}

	@Override
	public void setReport(boolean b) {		
	}
	
}
