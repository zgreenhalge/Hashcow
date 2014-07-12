package gamePieces;

import java.util.ArrayList;

import utils.Logger;

public class Player {

	
	private int id;
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
	
	public void age(){
		for(Unit u: owned){
			u.age();
		}
	}
	
	public void addUnit(Unit u){
		units.add(u);
		owned.add(u);
		sight.addUnit(u);
	}
	
	public void addBuilding(Building b){
		buildings.add(b);
		owned.add(b);
		sight.addUnit(b);
	}
	
	public void removeUnit(Unit u){
		units.remove(u);
		owned.remove(u);
		sight.removeUnit(u);
	}
	
	public void removeBuilding(Building b){
		buildings.remove(b);
		owned.remove(b);
		sight.removeUnit(b);
	}
	
	public SightMap getSightMap(){
		return sight;
	}
	
	public int getId(){
		return id;
	}
	
	public ArrayList<Unit> getUnits(){
		return units;
	}
	
	public ArrayList<Building> getBuildings(){
		return buildings;
	}

	public ArrayList<Unit> owned() {
		return owned;
	}
}
