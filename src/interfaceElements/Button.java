package interfaceElements;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public interface Button {
	
	public String getName();
	public Button setName(String s);
	public void setReport(boolean b);
	public boolean isReporting();
	public abstract void setEnabled(boolean b);
	public abstract boolean isEnabled();
	public void render(GUIContext guic, Graphics g);
}
