package gamePieces;

public class MapInfo {

	private MapTile[][] board;
	private int MAX_PLAYERS;
	private int NUM_PLAYERS;
	private int[][] positions;
	
	public static final MapInfo TEST_MAP = new MapInfo(
			new MapTile[][] {new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}, new MapTile[] {MapTile.GRASS, MapTile.GRASS, MapTile.GRASS, MapTile.GRASS}},
			new int[1][1],
			1, 1);
	
	public MapInfo(MapTile[][] map, int[][] start, int max, int num){
		board = map;
		positions = start;
		MAX_PLAYERS = max;
		NUM_PLAYERS = num;
	}
	
	public void update(int delta){
		for(MapTile[] arr: board)
			for(MapTile tile: arr){
				tile.getAnimation().update(delta);
			}
	}
	
	public void render(int X, int Y){
		int row=0, column=0;
		while(column<board[0].length){
			while(row<board.length){
				board[column][row].getAnimation().draw(X+column*32, Y+row*32);
				row++;
			}
			column++;
			row = 0;
		}
	}
	
	public MapTile getTile(int x, int y){
		MapTile ret;
		if(x >= board.length || y >= board[0].length)
			ret = null;
		else
			ret = board[x][y];
		return ret;
	}
	
	public int getNumPlayers(){
		return NUM_PLAYERS;
	}
	
	public int getMaxPlayers(){
		return MAX_PLAYERS;
	}
	
	public int getWidth(){
		return board.length*32;
	}
	
	public int getHeight(){
		return board[0].length*32;
	}
	
	public int[] getStartingPosition(int playerNum){
		return positions[playerNum];
	}
	
}
