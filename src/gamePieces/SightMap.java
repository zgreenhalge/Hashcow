package gamePieces;

import java.util.ArrayList;

import utils.OneToOneMap;

public class SightMap {

	private ArrayList<Unit> units;
	private OneToOneMap<int[], ArrayList<Unit>> map;
	
	public SightMap(ArrayList<Unit> units){
		this.units = units;
		map = new OneToOneMap<int[], ArrayList<Unit>>();
		reset();
	}
	
	public void addUnit(Unit u){
		for(int[] coords: getSight(u)){
				if(!map.containsKey(coords)){
					map.add(coords, new ArrayList<Unit>());
				}
				map.getValue(coords).add(u);
			}
	}
	
	public void updateUnit(Unit u, int oldX, int oldY){
		int range = u.getSightRange();
		for(int x = oldX-range; x <= oldX+range; x++)
			for(int y = oldY-range; y<= oldY+range; y++)
				if(map.containsKey(new int[] {x, y})){
					//temp = map.getValue(coords);
				}
		for(int[] coords: getSight(u)){
			}
	}
	
	private ArrayList<int[]> getSight(Unit u){
		ArrayList<int[]> temp = new ArrayList<int[]>();
		int rootX = u.getColumn();
		int rootY = u .getRow();
		int range = u.getSightRange();
		for(int x = rootX-range; x <= rootX+range; x++)
			for(int y = rootY-range; y<= rootY+range; y++)
				if(Math.abs(rootX-x) + Math.abs(rootY - y) <= range)
					temp.add(new int[] {x,y});
		return temp;
	}
	
	public void reset(){
		map.clear();
		for(Unit u: units)
			addUnit(u);
	}
}
