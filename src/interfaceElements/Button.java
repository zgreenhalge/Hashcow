package interfaceElements;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public interface Button {
	
	public int getHeight();
	public int getWidth();
	public String getName();
	public Button setName(String s);
	public void setReport(boolean b);
	public boolean isReporting();
	public void setHidden(boolean b);
	public boolean isHidden();
	public void setUnclickable(boolean b);
	public boolean isUnclickable();
	public abstract void setEnabled(boolean b);
	public abstract boolean isEnabled();
	public void render(GUIContext guic, Graphics g);
	public void setLocation(int X, int Y);
}
