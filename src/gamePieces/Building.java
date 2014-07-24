package gamePieces;

import java.util.ArrayList;
import java.util.TreeSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import resourceManager.UnitImage;

/**
 * A generic building class. 
 *
 */
public class Building extends Unit {

	private static final Color fogMask = new Color(0.9f, 0.9f, 0.9f, 0.35f);
	private TreeSet<Integer> seenBy;
	private int HEIGHT;
	private int WIDTH;
	private boolean[][] pathable;
	private boolean globallyVisible;
	
	public Building(Coordinate location, Player owner, MapInfo map, MovementType moveType){
		super(location, owner, map, moveType);
		super.baseMoveRange = 0;
		super.baseSightRange = 1;
		super.baseAttackRange = 0;
		super.baseAttack = 0;
		super.baseDefense = 5;
		super.baseHealth = 50;
		super.currentHealth = 50;
		super.name = "Unknown Building";
		HEIGHT = 1;
		WIDTH = 1;
		seenBy = new TreeSet<Integer>();
		pathable = new boolean[WIDTH][HEIGHT];
	}
	
	@Override
	public void register(){
		super.owner.addBuilding(this);
		super.map.addBuilding(this);
	}
	
	/**
	 * @Override
	 * Update the Building.
	 * @param delta - the time since last update
	 */
	public void update(int delta){
		current.update(delta);
		if(currentHealth == 0 && !dead){
			kill();
			return;
		}
		else if(current.equals(image.getAnimation(UnitImage.DEATH)) && current.isStopped()){
			map.removeUnit(this);
			owner.removeBuilding(this);
			return;
		}
	}	
	
	/**
	 * @Override
	 * Render the Building
	 * @param g - the current Graphics object
	 * @param X - the X offset of the map
	 * @param Y - the Y offset of the map
	 */
	public void render(Graphics g, int X, int Y){
		if((visible || globallyVisible) && selected)
			cursor.draw(X+currentX, Y+currentY);
		if(visible){
			current.draw(X+currentX, Y+currentY);
			seenBy.add(map.getSightMap().getId());
		}else if(globallyVisible || seenBy.contains(map.getSightMap().getId())){
			current.getCurrentFrame().draw(X+currentX, Y+currentY, fogMask);
		}
	}

	/**
	 * Get the width of the building
	 * @return width of the building in number of squares it occupies.
	 */
	public int getWidth(){
		return WIDTH;
	}
	
	/**
	 * Get the height of the building
	 * @return height of the building in number of squares it occupies
	 */
	public int getHeight(){
		return HEIGHT;
	}

	/**
	 * Check if the given location within the building is pathable.
	 * @param coord - the location of the square to check
	 * @return if the coordinate is pathable
	 */
	public boolean isPathable(Coordinate coord){
		if(coord.X() < location.X()+WIDTH && coord.X() >= location.X())
			if(coord.Y() < location.Y()+HEIGHT && coord.Y() >= location.Y())
				return pathable[coord.X()][coord.Y()];
		//throw new Exception(coord.X() +"," + coord.Y() + " is not within " + name + " at " + location.X() +"," + location.Y());
		return false;
	}
	
	/**
	 * Mark this building seen by a player.
	 * Once it is marked it will render even if it is in fog of war for that player's SightMap.
	 * @param id - the id of the Player that has seen the building
	 */
	public void setSeenBy(int id){
		seenBy.add(id);
	}
	
	/**
	 * Mark this building as globally visible. 
	 * Globally visible buildings will render as if every player has seen them. 
	 * @param b - whether the building should be globally visible or not.
	 */
	public void setGloballyVisible(boolean b){
		globallyVisible = b;
	}
	
	/**
	 * @Override
	 * Get Coordinate locations of all visible tiles to the Unit
	 * @return ArrayList<Coordinate> - all Tiles visible to the Unit
	 */
	public ArrayList<Coordinate> getSight(){
		ArrayList<Coordinate> temp = new ArrayList<Coordinate>();
		int range = getBaseSightRange();
		for(int curX = location.X(); curX < location.X()+WIDTH; curX++)
			for(int curY = location.Y(); curY < location.Y()+HEIGHT; curY++)
				for(int x = curX-range; x <= curX+range; x++)
					for(int y = curY-(range-Math.abs(curX-x)); y <= curY+(range-Math.abs(curX-x)); y++){
						if(y >= 0 && x >= 0 && y < map.getHeight() && x < map.getWidth())
							temp.add(new Coordinate(x,y));
			}
		return temp;
	}

	
}
