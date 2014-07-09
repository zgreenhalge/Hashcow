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
	private OneToOneMap<int[], Unit> units;
	private OneToOneMap<int[], Building> buildings;
	private SightMap currentSight;
	private int MAX_PLAYERS;
	private int[][] positions;
	private int selectedX;
	private int selectedY;
	private int row;
	private int column;
	private Animation cursor;
	private int displayMode = 0;
	private String pos;
	private Color prev;
	private TrueTypeFont f = FontManager.TINY_TRUETYPE;
	private int[] tempArr;
	private MapTile temp;
	
	public static final MapInfo TEST_MAP = new MapInfo(
			new MapTile[][] {new MapTile[] {MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS)}, new MapTile[] {MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS)}, new MapTile[] {MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS)}, new MapTile[] {MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS), MapTile.copy(MapTile.GRASS)}},
			new int[1][2], //[maxPlayers][2] ALWAYS
			1, 1);
	
	public MapInfo(MapTile[][] map, int[][] start, int max, int num){
		selectedX = selectedY = -1;
		board = map;
		positions = start;
		MAX_PLAYERS = max;
		units = new OneToOneMap<int[], Unit>();
		buildings = new OneToOneMap<int[], Building>();
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
		while(column<board.length){
			while(row<board[column].length){
				temp = board[column][row];
				temp.render(g, X+column*32, Y+row*32);
				if(isOccupied(column, row))
					units.getValue(new int[] {column, row}).render(g, X, Y);
				if(isBuilt(column, row))
					buildings.getValue(new int[] {column, row}).render(g, X, Y);
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
		if(displayMode < 3 && displayMode > 0){
			prev = g.getColor();
			g.setColor(Color.black);
			for(int i=0; i < board.length; i++)
				g.drawLine(X+i*32, Y, X+i*32, Y+board[i].length*32);
			for(int i=0; i<board[0].length; i++)
				g.drawLine(X, Y+i*32, X+board.length*32, Y+i*32);
			g.setColor(prev);
		}
		
		if(selectedX >= 0 && selectedX < board.length && selectedY >= 0 && selectedY < board[0].length){
			cursor.draw(X+selectedX*32, Y+selectedY*32);
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
		tempArr = new int[] {selectedX, selectedY};
		if(isOccupied(selectedX, selectedY))
			units.getValue(tempArr).deselect();
		if(isBuilt(selectedX, selectedY))
			buildings.getValue(tempArr).deselect();
		selectedX = X;
		selectedY = Y;
		tempArr = new int[] {X, Y};
		if(isOccupied(X, Y))
			ret.addAll(units.getValue(tempArr).select(selector));
		if(isBuilt(X, Y))
			buildings.getValue(tempArr).select(selector);
		return ret;
	}
	
	public void setVisible(int X, int Y){
		board[X][Y].setVisible(true);
		tempArr = new int[] {X, Y};
		units.getValue(tempArr).setVisible(true);
		buildings.getValue(tempArr).setVisible(true);
	}
	
	public void setHidden(int X, int Y){
		board[X][Y].setVisible(false);
		tempArr = new int[] {X, Y};
		units.getValue(tempArr).setVisible(false);
		buildings.getValue(tempArr).setVisible(false);
	}
	
	public void addUnit(Unit u, int X, int Y){
		units.add(new int[] {X, Y}, u);
	}
	
	public void removeUnit(Unit u){
		units.remove(new int[] {u.getColumn(), u.getRow()});
	}
	
	public Unit getUnit(int X, int Y){
		return units.getValue(new int[]{X, Y});
	}
	
	public boolean isOccupied(int X, int Y){
		return units.containsKey(new int[] {X, Y});
	}
	
	public boolean isBuilt(int X, int Y){
		return buildings.containsKey(new int[] {X, Y});
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

	public int[] getStartingPosition(int playerNum){
		return positions[playerNum];
	}
	
	public void setSightMap(SightMap sight){
		currentSight = sight;
	}
	
	public void applySightMap(){
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board[i].length; j++)
				if(currentSight.isVisible(i, j))
					board[i][j].setVisible(true);
				else
					board[i][j].setVisible(false);
	}

	public void hideAll() {
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board[i].length; j++)
				board[i][j].setVisible(false);
	}
	
	public void showAll(){
		for(int i=0; i<board.length; i++)
			for(int j=0; j<board[i].length; j++)
				board[i][j].setVisible(true);
	}

	public SightMap getSightMap() {
		return currentSight;
	}
	
}
