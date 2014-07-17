package gamePieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import utils.Logger;
import utils.OneToOneMap;
import utils.Settings;

/**
 * Contains information about a particular view of the board.
 * 
 */
public class SightMap implements Serializable{

	private static final long serialVersionUID = 5901439913345276852L;
	private ArrayList<Unit> units;
	private OneToOneMap<Coordinate, ArrayList<Unit>> map;
	private int playerId;
	
	public SightMap(int id){
		units = new ArrayList<Unit>();
		map = new OneToOneMap<Coordinate, ArrayList<Unit>>();
		playerId = id;
		reset();
	}
	
	/**
	 * Check if a specific Coordinate is visible
	 * @param c - the Coordinate to check
	 * @return true if the Coordinate is visible
	 */
	public boolean isVisible(Coordinate c){
		return map.containsKey(c);
	}
	
	/**
	 * Add a Unit to the SightMap
	 * @param u - the Unit to add
	 * @return ArrayList<Coordinate> - Coordinate locations of everywhere the visibility has changed
	 */
	public ArrayList<Coordinate> addUnit(Unit u){
		ArrayList<Coordinate> ret = new ArrayList<Coordinate>();
		for(Coordinate coords: u.getSight()){
				if(!map.containsKey(coords)){
					map.add(coords, new ArrayList<Unit>());
					ret.add(coords);
				}
				map.getValue(coords).add(u);
			}
		if((boolean)Settings.getSetting(Settings.DEV_MODE)){
			String print = "Visible from " + u.getLocation().toString() + ":";
			for(Coordinate coords: ret)
				print += " " + coords.toString();
			print += "   ("+ret.size()+")";
			Logger.loudLogLine(print);
		}
		return ret;
	}

	/**
	 * Remove a Unit from the SightMap
	 * @param u - the Unit to remove
	 * @return ArrayList<Coordinate> - Coordinate locations of everywhere the visibility has changed
	 */
	public ArrayList<Coordinate> removeUnit(Unit u){
		ArrayList<Coordinate> ret = new ArrayList<Coordinate>();
		for(Entry<Coordinate, ArrayList<Unit>> entry: map.entrySet()){
			if(entry.getValue().remove(u))
				if(entry.getValue().isEmpty())
					ret.add(entry.getKey());
		}
		for(Coordinate c: ret)
			map.remove(c);
		return ret;			
	}

	/**
	 * Alert the SightMap to a Unit's change in location
	 * @param u - the Unit that has moved
	 * @param oldLoc - the previous Coordinate location of the given Unit
	 * @return ArrayList<Coordinate> - Coordinate locations of everywhere the visibility has changed
	 */
	public ArrayList<Coordinate> updateUnit(Unit u, Coordinate oldLoc){
		int range = u.getBaseSightRange();
		ArrayList<Coordinate> ret = new ArrayList<Coordinate>();
		Coordinate temp;
		ArrayList<Unit> arr;
		for(int x = oldLoc.X()-range; x <= oldLoc.X()+range; x++)
			for(int y = oldLoc.Y()-range; y<= oldLoc.Y()+range; y++){
				temp = new Coordinate(x, y);
				if(map.containsKey(temp)){
					arr = map.getValue(temp);
					arr.remove(u);
					if(arr.isEmpty()){
						map.remove(temp);
						ret.add(temp);
					}
				}
			}
		for(Coordinate coords: u.getSight()){
			arr = map.getValue(coords);
			if(arr == null){
				arr = new ArrayList<Unit>();
				ret.add(coords);
			}
			arr.add(u);
		}
		
		return ret;
	}
	
	/**
	 * Rewrite the SightMap from scratch
	 */
	public void reset(){
		map.clear();
		for(Unit u: units)
			addUnit(u);
	}
	
	/**
	 * The set of visible Coordinates
	 * @return
	 */
	public Set<Coordinate> visibleSet(){
		return map.keySet();
	}
	
	/**
	 * Get the ID of the Player who owns this SightMap
	 * @return the id of the owning Player
	 */
	public int getId(){
		return playerId;
	}
}
