package gamePieces;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.TrueTypeFont;

import resourceManager.FontManager;
import utils.Logger;
import utils.OneToOneMap;
import utils.Settings;

/**
 * Holds all relevant information about a game board including the Tile values,
 * a list of all Units and Buildings, the current SightMap, the starting locations for every player,
 * and the number of players active in the game. Provides the interface for the SightMap to
 * influence the rendering of Units, Buildings, and Tiles.
 *
 */
public class MapInfo implements Serializable{

	private static final long serialVersionUID = -4266804244413260883L;
	
	private String name;
	private Tile[][] board;
	private Stack<Selectable> inputStack;
	private OneToOneMap<Coordinate, Unit> units;
	private OneToOneMap<Coordinate, Building> buildings;
	private SightMap currentSight;
	private int MAX_PLAYERS;
	private Coordinate[] positions;
	private int selectedX;
	private int selectedY;
	private int row;
	private int column;
	private int displayMode = 0;
	private String pos;
	private transient Color prev;
	private transient TrueTypeFont f = FontManager.TINY_TRUETYPE;
	private transient Coordinate coords;
	private transient Tile temp;
	
	public MapInfo(String name, Tile[][] map, Coordinate[] start, int max){
		this.name = name;
		selectedX = selectedY = -1;
		board = map;
		positions = start;
		MAX_PLAYERS = max;
		units = new OneToOneMap<Coordinate, Unit>();
		buildings = new OneToOneMap<Coordinate, Building>();
		inputStack = new Stack<Selectable>();
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * Update the entire map - Tiles, Units, Buildings
	 * @param delta - the time since the last update call
	 */
	public void update(int delta){
		for(Tile[] arr: board)
			for(Tile tile: arr){
				tile.update(delta);
			}	
		for(Unit u: units.values()){
			u.update(delta);
		}
		for(Building b: buildings.values())
			b.update(delta);
	}
	
	/**
	 * Render the entire map - Tiles, Units, Buildings
	 * @param g - the current Graphics object being used
	 * @param X - the X offset of the map
	 * @param Y - the Y offset of the map
	 */
	public void render(Graphics g, int X, int Y){
		row = column = 0;
		//draw board
		while(column<board.length){
			while(row<board[column].length){
				temp = board[column][row];
				temp.render(g, X+column*32, Y+row*32);
				if(displayMode > 1){
					g.setFont(f);
					pos = column + "," + row;
					g.drawString(pos, X+(column+1)*32-f.getWidth(pos), Y+(row+1)*32-f.getLineHeight());
					g.setFont(FontManager.DEFAULT_TRUETYPE);
				}
				row++;
			}
			column++;
			row = 0;
		}
		//draw buildings
		for(Building b: buildings.values())
			b.render(g, X, Y);
		//draw units
		for(Unit u: units.values())
			u.render(g, X, Y);
		//draw grid/coordinates
		if(displayMode < 3 && displayMode > 0){
			prev = g.getColor();
			g.setColor(Color.black);
			for(int i=0; i < board.length; i++)
				g.drawLine(X+i*32, Y, X+i*32, Y+board[i].length*32);
			for(int i=0; i<board[0].length; i++)
				g.drawLine(X, Y+i*32, X+board.length*32, Y+i*32);
			g.setColor(prev);
		}
	}
	
	/**
	 * Cycles the display mode. 
	 * 0 = no lines or coordinates
	 * 1 = grid lines only
	 * 2 = grid lines and coordinates
	 * 3 = coordinates only
	 */
	public void cycleDisplayMode(){
		displayMode = (displayMode+1)%4;
	}
	
	/**
	 * Retrieve the tile at the given position on the board.
	 * @param x - the column of the Tile to be retrieved
	 * @param y - the row of the tile to be retrieved
	 * @return the Tile at x,y or null if the Coordinate is outside of the map 
	 */
	public Tile getTile(Coordinate coord){
		Tile ret;
		if(coord.X() >= board.length | coord.X() < 0 | coord.Y() < 0 | coord.Y() >= board[0].length)
			ret = null;
		else
			ret = board[coord.X()][coord.Y()];
		return ret;
	}
	
	/**
	 * Retrieve a Map of adjacent coordinates and tiles
	 * @param coord - starting coordinate to get adj. Tiles.
	 * @return a OneToOneMap of Tile, Coordinate pairs
	 */
	
	public OneToOneMap<Tile, Coordinate> getAdjacentTiles(Coordinate coord){
		OneToOneMap<Tile, Coordinate> tiles = new OneToOneMap<Tile,Coordinate>();
		
		tiles.add(getTile(new Coordinate(coord.X() + 1, coord.Y())), new Coordinate(coord.X() + 1, coord.Y()));
		tiles.add(getTile(new Coordinate(coord.X() - 1, coord.Y())), new Coordinate(coord.X() - 1, coord.Y()));
		tiles.add(getTile(new Coordinate(coord.X(), coord.Y() + 1)), new Coordinate(coord.X(), coord.Y() + 1));
		tiles.add(getTile(new Coordinate(coord.X(), coord.Y() - 1)), new Coordinate(coord.X(), coord.Y() - 1));
		
		return tiles;
	}
	
	/**
	 * Select a given location on the map.
	 * Will first deselect the last location selected before selecting the appropriate layer at the Coordinate given
	 * @param coord - the Coordinate selected
	 * @param selector - the Player who is doing the selecting
	 * @return ArrayList<Button> - the Buttons return by the selected Unit or Building
	 */
	public void select(int X, int Y, Player selector){
		if((boolean)Settings.getSetting(Settings.DEV_MODE))
			Logger.loudLogLine(X + "," + Y);
		if(!inputStack.isEmpty())
			inputStack.pop().select(new Coordinate(X, Y), selector);
		else{
			coords = new Coordinate(selectedX, selectedY);
			if(selectedX >= 0 && selectedX < board.length && selectedY >= 0 && selectedY < board[selectedX].length)
				board[selectedX][selectedY].deselect();
			if(isOccupied(coords))
				units.getValue(coords).deselect();
			if(isBuilt(coords))
				buildings.getValue(coords).deselect();
			selectedX = X;
			selectedY = Y;
			coords= new Coordinate(X, Y);
			if(isOccupied(coords))
				units.getValue(coords).select(coords, selector);
			else if(isBuilt(coords))
				buildings.getValue(coords).select(coords, selector);
			else
				if(selectedX >= 0 && selectedX < board.length && selectedY >= 0 && selectedY < board[selectedX].length)
					board[selectedX][selectedY].select(coords, selector);
		}
	}
	
	/**
	 * Set the given location to visible
	 * @param coord - the location that should be visible
	 */
	public void setVisible(Coordinate coord){
		board[coord.X()][coord.Y()].setVisible(true);
		units.getValue(coords).setVisible(true);
		buildings.getValue(coords).setVisible(true);
	}
	
	/**
	 * Set the given location to hidden
	 * @param coord - the location that should be hidden
	 */
	public void setHidden(Coordinate coord){
		board[coord.X()][coord.Y()].setVisible(false);
		units.getValue(coords).setVisible(false);
		buildings.getValue(coords).setVisible(false);
	}
	
	/**
	 * Register a Unit on the map
	 * @param u - the Unit to register
	 */
	public void addUnit(Unit u){
		units.add(u.getLocation(), u);
	}
	
	/**
	 * Register a Building on the map
	 * @param b - the Building to register
	 */
	public void addBuilding(Building b){
		Coordinate root = b.getLocation();
		int height = b.getHeight();
		int width = b.getWidth();
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				coords = new Coordinate(root.X()+x, root.Y()+y);
				buildings.add(coords, b);
			}
		}
	}
	
	/**
	 * Remove a Building from the map
	 * @param b - the Building to be removed
	 */
	public void removeBuilding(Building b){
		Coordinate root = b.getLocation();
		int height = b.getHeight();
		int width = b.getWidth();
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				coords = new Coordinate(root.X()+x, root.Y()+y);
				buildings.remove(coords);
			}
		}
	}
	
	/**
	 * Retrieve a Building from a specified Coordinate
	 * @param coord - the Coordinate of the Building to be retrieved
	 * @return the Building at coord.X, coord.Y or null if no such building exists
	 */
	public Building g(Coordinate coord){
		return buildings.getValue(coord);
	}
	
	/**
	 * Remove a Unit from the map
	 * @param u - the Unit to be removed
	 */
	public void removeUnit(Unit u){
		units.remove(new Coordinate(u.getColumn(), u.getRow()));
	}
	
	/**
	 * Retrieve a Unit from a specified Coordinate
	 * @param coord - the Coordinate of the Unit to be retrieved
	 * @return the Unit at coord.X, coord.Y or null if no such thing exists
	 */
	public Unit getUnit(Coordinate coord){
		return units.getValue(coord);
	}
	
	/**
	 * Check if the given Coordinate is occupied by a Unit
	 * @param coord - the Corrdinate to check
	 * @return true if the Coordinate is occupied by a Unit
	 */
	public boolean isOccupied(Coordinate coord){
		return units.containsKey(coord);
	}
	
	/**
	 * Check if the given Coordinate is occupied by a Building
	 * @param coord - the Coordinate to check
	 * @return true if the Coordinate is occupied by a Building
	 */
	public boolean isBuilt(Coordinate coord){
		return buildings.containsKey(coord);
	}
	
	/**
	 * Check if the given Coordinate is pathable
	 * @param coord - the Coordinate to check
	 * @return true if coord is traversable terrain and not blocked by a Building
	 */
	public boolean isPathable(Coordinate coord){
		if(!board[coord.X()][coord.Y()].isTraversable())
			return false;
		else if(isBuilt(coord))
			return buildings.getValue(coord).isPathable(coord);
		else return true;
	}
	
	/**
	 * Get the maximum number of player allowed on this map
	 * @return the max players this map supports
	 */
	public int getMaxPlayers(){
		return MAX_PLAYERS;
	}
	
	/**
	 * Get the number of columns in the map
	 * @return the width of the map in Tiles
	 */
	public int getWidth(){
		return board.length;
	}
	
	/**
	 * Get the number of rows in the map
	 * @return the height of the map in Tiles
	 */
	public int getHeight(){
		return board[0].length;
	}

	/**
	 * Retrieve the Coordinate of the starting position for the given player
	 * @param playerNum - the player number
	 * @return the Coordinate starting position associated with the playerNum-th player
	 */
	public Coordinate getStartingPosition(int playerNum){
		return positions[playerNum];
	}
	
	/**
	 * Sets the given SightMap as the SightMap in use
	 * @param sight - the SightMap to be used by ths map
	 */
	public void setSightMap(SightMap sight){
		currentSight = sight;
	}
	
	/**
	 * Applies the current SightMap to the entire board.
	 */
	public void applySightMap(){
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board[i].length; j++){
				coords = new Coordinate(i, j);
				if(currentSight.isVisible(coords)){
					board[coords.X()][coords.Y()].setVisible(true);
					if(isOccupied(coords))
						units.getValue(coords).setVisible(true);
					if(isBuilt(coords))
						buildings.getValue(coords).setVisible(true);
				}
				else{
					board[coords.X()][coords.Y()].setVisible(false);
					if(isOccupied(coords))
						units.getValue(coords).setVisible(false);
					if(isBuilt(coords))
						buildings.getValue(coords).setVisible(false);
				}
			}
	}
	
	/**
	 * Applies the current SightMap to only the given Coordinates
	 * @param changed - the Coordinates the SightMap should be applied to
	 */
	public void applySightMap(ArrayList<Coordinate> changed){
		for(Coordinate coords: changed){
			if(currentSight.isVisible(coords)){
				board[coords.X()][coords.Y()].setVisible(true);
				if(isOccupied(coords))
					units.getValue(coords).setVisible(true);
				if(isBuilt(coords))
					buildings.getValue(coords).setVisible(true);
			}
			else{
				board[coords.X()][coords.Y()].setVisible(false);
				if(isOccupied(coords))
					units.getValue(coords).setVisible(false);
				if(isBuilt(coords))
					buildings.getValue(coords).setVisible(false);
			}
		}
	}

	/**
	 * Set the entire map to hidden, overriding the active SightMap
	 */
	public void hideAll() {
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board[i].length; j++)
				board[i][j].setVisible(false);
		for(Unit u: units.values())
			u.setVisible(false);
		for(Building b: buildings.values())
			b.setVisible(false);
	}
	
	/**
	 * Set the entire map to visible, overriding the active SightMap
	 */
	public void showAll(){
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board[i].length; j++)
				board[i][j].setVisible(true);
		for(Unit u: units.values())
			u.setVisible(true);
		for(Building b: buildings.values())
			b.setVisible(true);
	}

	/**
	 * Get the current SightMap being used
	 * @return the SightMap in use
	 */
	public SightMap getSightMap() {
		return currentSight;
	}

	public void requestNextInput(Selectable waiting) {
		inputStack.push(waiting);
	}
	
	public void cancelInputRequest(Selectable waiting){
		inputStack.remove(waiting);
	}
	
	/*public Image getPlayerMinimap(int width, int height){
		
	}*/
	
	public Image getMinimap(int width, int height){
		ImageBuffer build = new ImageBuffer(board.length*32, board[0].length*32);
		Color temp;
		Image image;
		for(int x=0; x<board.length; x++)
			for(int y=0; y<board[x].length; y++){
				image = board[x][y].getCurrentFrame();
				for(int i=0; i<31; i++)
					for(int j=0; j<31; j++){
						temp = image.getColor(i, j);
						build.setRGBA(x*32+i, y*32+j, temp.getRed(), temp.getGreen(), temp.getBlue(), temp.getAlpha());
					}
			}
		for(Building b: buildings.values()){
			image = b.getCurrentAnimation().getCurrentFrame();
			for(int i=0; i<31; i++)
				for(int j=0; j<31; j++){
					temp = image.getColor(i, j);
					if(temp.getAlpha() != 0)
						temp.add(b.getOwner().getColor());
					if(temp.getAlpha() != 0f)
						build.setRGBA(b.getColumn()*32+i, b.getRow()*32+j, temp.getRed(), temp.getGreen(), temp.getBlue(), temp.getAlpha());
				}
		}
		for(Unit u: units.values()){
			image = u.getCurrentAnimation().getCurrentFrame();
			for(int i=0; i<31; i++)
				for(int j=0; j<31; j++){
					temp = image.getColor(i, j);
					if(temp.getAlpha() != 0)
						temp.add(u.getOwner().getColor());
					if(temp.getAlpha() != 0f)
						build.setRGBA(u.getColumn()*32+i, u.getRow()*32+j, temp.getRed(), temp.getGreen(), temp.getBlue(), temp.getAlpha());
				}
		}
		
		return build.getImage().getScaledCopy(width, height);
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException{
		oos.writeObject(board);
		oos.writeObject(inputStack);
		oos.writeObject(units);
		oos.writeObject(buildings);
		oos.writeObject(currentSight);
		oos.writeInt(MAX_PLAYERS);
		oos.writeObject(positions);
		oos.writeInt(selectedX);
		oos.writeInt(selectedY);
		oos.writeInt(row);
		oos.writeInt(column);
		oos.writeInt(displayMode);
		oos.writeObject(pos);
		
		oos.flush();
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
		board = (Tile[][]) ois.readObject();
		inputStack = (Stack<Selectable>) ois.readObject();
		units = (OneToOneMap<Coordinate, Unit>) ois.readObject();
		buildings = (OneToOneMap<Coordinate, Building>) ois.readObject();
		currentSight = (SightMap) ois.readObject();
		MAX_PLAYERS = ois.readInt();
		positions = (Coordinate[]) ois.readObject();
		selectedX = ois.readInt();
		selectedY = ois.readInt();
		row = ois.readInt();
		column = ois.readInt();
		displayMode = ois.readInt();
		pos = (String) ois.readObject();
		
	}

	public ArrayList<Unit> getAll() {
		ArrayList<Unit> ret = new ArrayList<Unit>();
		ret.addAll(units.values());
		ret.addAll(buildings.values());
		return ret;
	}
	
}
