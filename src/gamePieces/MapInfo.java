package gamePieces;

import interfaceElements.Button;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import resourceManager.FontManager;
import resourceManager.ImageManager;
import utils.Logger;
import utils.OneToOneMap;

public class MapInfo {

	private MapTile[][] board;
	private OneToOneMap<Coordinate, Unit> units;
	private OneToOneMap<Coordinate, Building> buildings;
	private SightMap currentSight;
	private int MAX_PLAYERS;
	private Coordinate[] positions;
	private int selectedX;
	private int selectedY;
	private int row;
	private int column;
	private Animation cursor;
	private int displayMode = 0;
	private String pos;
	private Color prev;
	private TrueTypeFont f = FontManager.TINY_TRUETYPE;
	private Coordinate coords;
	private MapTile temp;
	
	public static final MapInfo TEST_MAP = new MapInfo(
			new MapTile[][] {new MapTile[] {MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS)}, new MapTile[] {MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS)}, new MapTile[] {MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS)}, new MapTile[] {MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS)}},
			new Coordinate[]{new Coordinate(0,0), new Coordinate(3,3)}, //define for max players
			1, 1);
	
	public MapInfo(MapTile[][] map, Coordinate[] start, int max, int num){
		selectedX = selectedY = -1;
		board = map;
		positions = start;
		MAX_PLAYERS = max;
		units = new OneToOneMap<Coordinate, Unit>();
		buildings = new OneToOneMap<Coordinate, Building>();
	}
	
	public void update(int delta){
		for(MapTile[] arr: board)
			for(MapTile tile: arr){
				tile.update(delta);
			}	
		for(Unit u: units.values()){
			u.update(delta);
		}
		for(Building b: buildings.values())
			b.update(delta);
	}
	
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
	
	public void cycleDisplayMode(){
		displayMode = (displayMode+1)%4;
	}
	
	public MapTile getTile(int x, int y){
		MapTile ret;
		if(x >= board.length || y >= board[0].length)
			ret = null;
		else
			ret = board[x][y];
		return ret;
	}
	
	public ArrayList<Button> select(int X, int Y, Player selector){
		//turn on for selection feedback: should probably get a dev switch later on
		//Logger.loudLogLine(X + "," + Y);
		ArrayList<Button> ret = new ArrayList<Button>();
		coords = new Coordinate(selectedX, selectedY);
		if(selectedX >= 0 && selectedX < board.length && selectedY >= 0 && selectedY < board[selectedX].length)
			board[selectedX][selectedY].deselect();
		if(isOccupied(coords))
			units.getValue(coords).deselect();
		if(isBuilt(coords))
			buildings.getValue(coords).deselect();
		selectedX = X;
		selectedY = Y;
		coords = new Coordinate(X, Y);
		if(isOccupied(coords))
			ret.addAll(units.getValue(coords).select(selector));
		else if(isBuilt(coords))
			ret.addAll(buildings.getValue(coords).select(selector));
		else
			if(selectedX >= 0 && selectedX < board.length && selectedY >= 0 && selectedY < board[selectedX].length)
				board[selectedX][selectedY].select();
		return ret;
	}
	
	public void setVisible(Coordinate coord){
		board[coord.X()][coord.Y()].setVisible(true);
		units.getValue(coords).setVisible(true);
		buildings.getValue(coords).setVisible(true);
	}
	
	public void setHidden(Coordinate coord){
		board[coord.X()][coord.Y()].setVisible(false);
		units.getValue(coords).setVisible(false);
		buildings.getValue(coords).setVisible(false);
	}
	
	public void addUnit(Unit u){
		units.add(u.getLocation(), u);
	}
	
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
	
	public Building getBuilding(Coordinate coord){
		return buildings.getValue(coord);
	}
	
	public void removeUnit(Unit u){
		units.remove(new Coordinate(u.getColumn(), u.getRow()));
	}
	
	public Unit getUnit(Coordinate coord){
		return units.getValue(coord);
	}
	
	public boolean isOccupied(Coordinate coord){
		return units.containsKey(coord);
	}
	
	public boolean isBuilt(Coordinate coord){
		return buildings.containsKey(coord);
	}
	
	public boolean isPathable(Coordinate coord){
		if(!board[coord.X()][coord.Y()].isTraversable())
			return false;
		else if(isBuilt(coord))
			return buildings.getValue(coord).isPathable(coord);
		else return true;
	}
	
	public int getMaxPlayers(){
		return MAX_PLAYERS;
	}
	
	public int getWidth(){
		return board.length;
	}
	
	public int getHeight(){
		return board[0].length;
	}

	public Coordinate getStartingPosition(int playerNum){
		return positions[playerNum];
	}
	
	public void setSightMap(SightMap sight){
		currentSight = sight;
	}
	
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

	public void hideAll() {
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board[i].length; j++)
				board[i][j].setVisible(false);
		for(Unit u: units.values())
			u.setVisible(false);
		for(Building b: buildings.values())
			b.setVisible(false);
	}
	
	public void showAll(){
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board[i].length; j++)
				board[i][j].setVisible(true);
		for(Unit u: units.values())
			u.setVisible(true);
		for(Building b: buildings.values())
			b.setVisible(true);
	}

	public SightMap getSightMap() {
		return currentSight;
	}
	
}
