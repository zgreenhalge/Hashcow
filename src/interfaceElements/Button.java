package interfaceElements;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import utils.Logger;

public interface Button {
	
	public String getName();
	public void setName(String s);
	public abstract void setEnabled(boolean b);
	public abstract boolean isEnabled();
	public void render(GUIContext guic, Graphics g);
}
