package interfaceElements;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

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
	
	public void addButton(Button b){
		buttons.add(b);
	}
	
	public void removeButton(Button b){
		buttons.remove(b);
	}

	public void clear(){
		for(Button b: buttons)
			b.setUnclickable(true);
		buttons.clear();
	}
	
}
