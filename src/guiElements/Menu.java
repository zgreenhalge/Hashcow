package guiElements;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import utils.Logger;

/**
 * A horizontal menu that will scale dynamically as Buttons are added and removed
 *
 */
public class Menu {

	
	private int X;
	private int Y;
	private int width = 0;
	private int height = 0;
	private int buttonHeight;
	
	private boolean background;
	private boolean border;
	private boolean centeredX;
	private boolean centeredY;
	
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
		if(buttons.isEmpty())
			return;
		prev = g.getColor();
		if(background){
			g.setColor(bg);
			g.fillRect(X-5, Y-2, width+5, height+5);
			g.setColor(prev);
		}
		if(border){
			g.setColor(Color.lightGray);
			g.drawLine(X, Y, X, Y+height);
			g.drawLine(X, Y, X+width, Y);
			g.drawLine(X+width, Y, X+width, Y+height);
			g.drawLine(X, Y+height, X+width, Y+height);
		}
		
		for(int i=0; i<buttons.size(); i++){
			b = buttons.get(i);
			if(b != null){
				if(centeredX)
					X = (container.getWidth()-b.getWidth())/2;
				if(centeredY)
					Y = (container.getHeight() - (buttons.size()*b.getHeight() + b.getHeight()*(buttons.indexOf(b)))/2)/2;
				b.setLocation(X, Y+(buttonHeight+5)*i);
				b.render(container, g);
			}
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
	
	public void clear(){
		for(Button b: buttons)
			b.setHidden(true);
		buttons.clear();
	}
	
	public void toggleBackground(){
		background = !background;
	}
	
	public void toggleBorder(){
		border = !border;
	}

	public int getIndex(Button selectedButton) {
		return buttons.indexOf(selectedButton);
	}
	
	public Button getButton(int i){
		return buttons.get(i);
	}
	
	public void centerX(boolean b){
		centeredX = b;
	}
	
	public void centerY(boolean b){
		centeredY = b;
	}
	
	public void setReporting(boolean b){
		for(Button temp: buttons)
			temp.setReport(b);
	}

	public void hide() {
		for(Button temp: buttons)
			temp.setHidden(true);
	}
	
	public void show(){
		for(Button temp: buttons)
			temp.setHidden(false);
	}

	public void center(boolean b) {
		centerX(b);
		centerY(b);
	}
	
}
