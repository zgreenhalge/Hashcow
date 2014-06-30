package gamePieces;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import resourceManager.FontManager;

public class MapInfo {

	private MapTile[][] board;
	private HashMap<int[], Unit> units;
	private int MAX_PLAYERS;
	private int[][] positions;
	private int displayMode = 0;
	private String pos;
	private Color prev;
	private TrueTypeFont f = FontManager.TINY_TRUETYPE;
	
	public static final MapInfo TEST_MAP = new MapInfo(
			new MapTile[][] {new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}, new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}, new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}, new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}},
			new int[1][1],
			1, 1);
	
	public MapInfo(MapTile[][] map, int[][] start, int max, int num){
		board = map;
		positions = start;
		MAX_PLAYERS = max;
	}
	
	public void update(int delta){
		for(MapTile[] arr: board)
			for(MapTile tile: arr){
				tile.getAnimation().update(delta);
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
