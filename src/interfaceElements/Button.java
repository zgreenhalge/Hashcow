package interfaceElements;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public interface Button {

	public void setEnabled(boolean b);
	public boolean isEnabled();
	public void render(GUIContext guic, Graphics g);
	
}
