package gamePieces;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A player within the game.
 *
 */
public class Player implements Serializable{

	private static final long serialVersionUID = 7640543740958854322L;

	private int id;
	
	private int lastX;
	private int lastY;
	
	private ArrayList<Unit> units;
	private ArrayList<Building> buildings;
	private ArrayList<Unit> owned;
	private SightMap sight;
	
	//research
	//resources
	//unit cap?
	//commander abilities?
	
	public Player(int id){
		this.id = id;
		units = new ArrayList<Unit>();
		buildings = new ArrayList<Building>();
		owned = new ArrayList<Unit>(units);
		sight = new SightMap(id); 
	}
	
	/**
	 * Age all Units and Buildings owned by the Player one turn
	 */
	public void age(){
		for(Unit u: owned){
			u.age();
		}
	}
	
	/**
	 * Add a Unit under this Player's control
	 * @param u - the Unit to add
	 */
	public void addUnit(Unit u){
		units.add(u);
		owned.add(u);
		sight.addUnit(u);
	}
	
	/**
	 * Add a new Building under this Player's control 
	 * @param b - the Building to add
	 */
	public void addBuilding(Building b){
		buildings.add(b);
		owned.add(b);
		sight.addUnit(b);
	}
	
	/**
	 * Remove a Unit from the Player's control
	 * @param u - the Unit to remove
	 */
	public void removeUnit(Unit u){
		units.remove(u);
		owned.remove(u);
		sight.removeUnit(u);
	}
	
	/**
	 * Remove a Building from the Player's control
	 * @param b - the Building to remove
	 */
	public void removeBuilding(Building b){
		buildings.remove(b);
		owned.remove(b);
		sight.removeUnit(b);
	}
	
	/**
	 * Retrieve the Player's SightMap
	 * @return the SightMap used by the Player
	 */
	public SightMap getSightMap(){
		return sight;
	}
	
	/**
	 * Retrieve this Player's ID
	 * @return the ID of the Player
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Get all Units owned by the Player
	 * @return ArrayList<Unit> - all the Units under the Player's control
	 */
	public ArrayList<Unit> getUnits(){
		return units;
	}
	
	/**
	 * Get all Buildings owned by the Player
	 * @return ArrayList<Building> - all the Buildings under the Player's control
	 */
	public ArrayList<Building> getBuildings(){
		return buildings;
	}

	/**
	 * Get all Units and Buildings the Player owns
	 * @return ArrayList<Unit> - all Buildings and Units owned by the Player
	 */
	public ArrayList<Unit> owned() {
		return owned;
	}

	public int getLastX() {
		return lastX;
	}

	public void setLastX(int x) {
		this.lastX = x;
	}

	public int getLastY() {
		return lastY;
	}

	public void setLastY(int y) {
		this.lastY = y;
	}
}
