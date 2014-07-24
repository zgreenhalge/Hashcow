package gamePieces;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.newdawn.slick.Color;

/**
 * A player within the game.
 *
 */
public class Player implements Serializable{

	private static final long serialVersionUID = 7640543740958854322L;

	private int id;
	
	private int lastX;
	private int lastY;
	
	private Color teamColor;
	private Race race;
	private String name;
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
		name = "Player " + id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String n){
		name = n;
	}
	
	/**
	 * Check if the Player is ready to start the game
	 * @return true iff the Player has all the required Objects to start a game
	 */
	public boolean ready(){
		return (race != null && teamColor != null);
	}
	
	/**
	 * Get the Player's team color
	 * @return Color of the player's team
	 */
	public Color getColor(){
		return teamColor;
	}
	
	/**
	 * Set the Player's team color
	 * @param color - the color of the Player's team
	 */
	public void setColor(Color color) {
		teamColor = color;		
	}
	
	/**
	 * Get the Race the Player is playing as
	 * @return the Player's Race
	 */
	public Race getRace(){
		return race;
	}
	
	/**
	 * Set the Player's Race
	 * @param r - the Race that the Player should be
	 */
	public void setRace(Race r){
		race = r;
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
	
	private void writeObject(ObjectOutputStream oos) throws IOException{
		oos.writeInt(id);
		oos.writeInt(lastX);
		oos.writeInt(lastY);
		oos.writeInt(teamColor.getRed());
		oos.writeInt(teamColor.getGreen());
		oos.writeInt(teamColor.getBlue());
		oos.writeInt(teamColor.getAlpha());
		oos.writeObject(units);
		oos.writeObject(buildings);
		oos.writeObject(owned);
		oos.writeObject(sight);
		
		oos.flush();
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
		id = ois.readInt();
		lastX = ois.readInt();
		lastY = ois.readInt();
		teamColor = new Color(ois.readInt(), ois.readInt(), ois.readInt(), ois.readInt());
		units = (ArrayList<Unit>) ois.readObject();
		buildings = (ArrayList<Building>) ois.readObject();
		owned = (ArrayList<Unit>) ois.readObject();
		sight = (SightMap) ois.readObject();
	}
	
}
