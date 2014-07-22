package guiElements;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import actions.Action;

/**
 * An common interface so both TextButton and ImageButton can be grouped together and addressed equally 
 *
 */
public interface Button {
	
	public void setAction(Action a);
	public Action getAction();
	public int getHeight();
	public int getWidth();
	public String getName();
	public Button setName(String s);
	public void setReport(boolean b);
	public boolean isReporting();
	public void setHidden(boolean b);
	public boolean isHidden();
	public abstract void setEnabled(boolean b);
	public abstract boolean isEnabled();
	public void render(GUIContext guic, Graphics g);
	public int getX();
	public int getY();
	public void setLocation(int X, int Y);
}
