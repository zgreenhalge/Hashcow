package interfaceElements;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * A horizontal menu structure that scales dynamically as Buttons are added or removed
 *
 */
public class HorizontalMenu{

	private int X;
	private int Y;
	private int width;
	private ArrayList<Button> buttons;
	
	public HorizontalMenu(int X, int Y) {
		this.X = X;
		this.Y = Y;
		width = 0;
		buttons = new ArrayList<Button>();
	} 
	
	/**
	 * Render the HorizontalMenu
	 * @param container - the container that the game is rendered in
	 * @param g - the current Graphics object
	 */
	public void render(GameContainer container, Graphics g){
		if(buttons.isEmpty())
			return;
		width = 0;
		for(Button b: buttons){
			b.setLocation(X + width, Y);
			b.render(container, g);
			if(!b.isHidden())
				width += b.getWidth();
		}
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
			b.setUnclickable(true);
		buttons.clear();
	}
	
}
