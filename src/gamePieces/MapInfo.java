package gamePieces;

import interfaceElements.Menu;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import resourceManager.FontManager;
import resourceManager.ImageManager;

public class MapInfo {

	private MapTile[][] board;
	private HashMap<int[], Unit> units;
	private int MAX_PLAYERS;
	private int[][] positions;
	private int selectedX;
	private int selectedY;
	private Animation cursor;
	private int displayMode = 0;
	private String pos;
	private Color prev;
	private TrueTypeFont f = FontManager.TINY_TRUETYPE;
	
	public static final MapInfo TEST_MAP = new MapInfo(
			new MapTile[][] {new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}, new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}, new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}, new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}},
			new int[1][1],
			1, 1);
	
	public MapInfo(MapTile[][] map, int[][] start, int max, int num){
		selectedX = selectedY = -1;
		board = map;
		positions = start;
		MAX_PLAYERS = max;
		units = new HashMap<int[], Unit>();
		cursor = ImageManager.getAnimation(ImageManager.getSpriteSheet("res/images/selectedTile.png", 32, 32, 1), 400);
	}
	
	public void update(int delta){
		//update 
		for(MapTile[] arr: board)
			for(MapTile tile: arr){
				tile.getAnimation().update(delta);
			}
		
		for(Unit u: units.values()){
			u.update(delta);
		}
	}
	
	public void render(Graphics g, int X, int Y){
		int row=0, column=0;
		while(column<board.length){
			while(row<board[column].length){
				board[column][row].getAnimation().draw(X+column*32, Y+row*32);
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
			
		
		for(Unit u: units.values())
			u.render(g, X, Y);
	}
	
	public Menu select(int X, int Y){
		selectedX = X;
		selectedY = Y;
		Menu ret = new Menu(X, Y);
		if(isOccupied(X, Y)){
			//for(Ability a: units.get(new int[] {X, Y}))
				//ret.addButton(a.getButton());
		}
		return ret;
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
	
	public void addUnit(Unit u, int X, int Y){
		units.put(new int[] {X, Y}, u);
	}
	
	public void removeUnit(int X, int Y){
		units.remove(new int[] {X, Y});
	}
	
	public Unit getUnit(int X, int Y){
		return units.get(new int[]{X, Y});
	}
	
	public boolean isOccupied(int X, int Y){
		return units.containsKey(new int[] {X, Y});
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
	
}
