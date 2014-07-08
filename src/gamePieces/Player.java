package gamePieces;

import java.util.ArrayList;

public class Player {

	
	private int id;
	private ArrayList<Unit> units;
	private ArrayList<Building> buildings;
	
	//research
	//commander abilities?
	
	public Player(int id){
		this.id = id;
		units = new ArrayList<Unit>();
		buildings = new ArrayList<Building>();
	}
}
