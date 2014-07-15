package gamePieces;

import guiElements.Button;

import java.util.ArrayList;

public interface Selectable {
	
	public ArrayList<Button> select(Player selector);
	public boolean isSelected();
	public void deselect();
	
}
