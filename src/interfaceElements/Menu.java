package interfaceElements;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import resourceManager.FontManager;

public class Menu {

	
	private int X;
	private int Y;
	private int width = 0;
	private int height = 0;
	private int buttonHeight;
	private ArrayList<Button> buttons;
	private Color prev;
	private Color bg = new Color(128, 128, 128, 0.3f); 
	private Button b;
	
	
	public Menu(int X, int Y){
		this.X = X;
		this.Y = Y;
		buttons = new ArrayList<Button>();
	}
	
	public void render(GameContainer container, Graphics g){
		prev = g.getColor();
		g.setColor(bg);
		g.fillRect(X-10, Y+10, width, height);
		g.setColor(prev);
		
		for(int i=0; i<buttons.size(); i++){
			b = buttons.get(i);
			b.setLocation(X, Y+(buttonHeight+1)*i);
			b.render((GUIContext)container, g);
		}
	}
	
	public void addButton(Button b){
		buttons.add(b);
		if(b.getHeight() > buttonHeight)
			buttonHeight = b.getHeight();
		if(b.getWidth() > width)
			width = b.getWidth();
		height += b.getHeight();
	}
	
	public void removeButton(Button b){
		if(buttons.remove(b)){
			height -= b.getHeight();
		}
		if(!buttons.isEmpty()){
			width = buttons.get(0).getWidth();
			for(Button t: buttons)
				if(t.getWidth() > width)
					width = t.getWidth();
		}else width = 0;
	}
}
