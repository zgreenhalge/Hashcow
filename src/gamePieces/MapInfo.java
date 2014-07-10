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
		cursor = ImageManager.getAnimation(ImageManager.getSpriteSheet("res/images/selectedTile.png", 32, 32, 1), 400);
	}
	
	public void update(int delta){
		for(MapTile[] arr: board)
			for(MapTile tile: arr){
				tile.getAnimation().update(delta);
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
		//draw cursor
		if(selectedX >= 0 && selectedX < board.length && selectedY >= 0 && selectedY < board[0].length)
			cursor.draw(X+selectedX*32, Y+selectedY*32);
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
		//Logger.loudLogLine(X + "," + Y);
		ArrayList<Button> ret = new ArrayList<Button>();
		coords = new Coordinate(selectedX, selectedY);
		if(isOccupied(selectedX, selectedY))
			units.getValue(coords).deselect();
		if(isBuilt(selectedX, selectedY))
			buildings.getValue(coords).deselect();
		selectedX = X;
		selectedY = Y;
		coords = new Coordinate(X, Y);
		if(isOccupied(X, Y))
			ret.addAll(units.getValue(coords).select(selector));
		if(isBuilt(X, Y))
			buildings.getValue(coords).select(selector);
		return ret;
	}
	
	public void setVisible(int X, int Y){
		board[X][Y].setVisible(true);
		coords = new Coordinate(X, Y);
		units.getValue(coords).setVisible(true);
		buildings.getValue(coords).setVisible(true);
	}
	
	public void setHidden(int X, int Y){
		board[X][Y].setVisible(false);
		coords = new Coordinate(X, Y);
		units.getValue(coords).setVisible(false);
		buildings.getValue(coords).setVisible(false);
	}
	
	public void addUnit(Unit u){
		units.add(u.getLocation(), u);
	}
	
	public void removeUnit(Unit u){
		units.remove(new Coordinate(u.getColumn(), u.getRow()));
	}
	
	public Unit getUnit(int X, int Y){
		return units.getValue(new Coordinate(X, Y));
	}
	
	public boolean isOccupied(int X, int Y){
		return units.containsKey(new Coordinate(X, Y));
	}
	
	public boolean isBuilt(int X, int Y){
		return buildings.containsKey(new Coordinate(X, Y));
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
				if(currentSight.isVisible(new Coordinate(i, j))){
					board[i][j].setVisible(true);
					//Logger.logLine(i + "," + j + " is visible");
				}
				else{
					board[i][j].setVisible(false);
					//Logger.logLine(i + "," + j + " is hidden");
				}
			}
	}
	
	public void applySightMap(ArrayList<Coordinate> changed){
		for(Coordinate coords: changed){
			if(currentSight.isVisible(coords))
				board[coords.X()][coords.Y()].setVisible(true);
			else
				board[coords.X()][coords.Y()].setVisible(false);
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
