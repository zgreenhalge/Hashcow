package gamePieces;

public interface Selectable {
	
	public void select(Coordinate selected, Player selector);
	public boolean isSelected();
	public void deselect();
	
}
