package gamePieces;

import java.util.ArrayList;
import java.util.Set;

import utils.OneToOneMap;

public class SightMap {

	private ArrayList<Unit> units;
	private OneToOneMap<int[], ArrayList<Unit>> map;
	
	public SightMap(ArrayList<Unit> units){
		this.units = units;
		map = new OneToOneMap<int[], ArrayList<Unit>>();
		reset();
	}
	
	public boolean isVisible(int X, int Y){
		return map.containsKey(new int[] {X, Y});
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
		int[] temp = new int[2];
		ArrayList<Unit> arr;
		for(int x = oldX-range; x <= oldX+range; x++)
			for(int y = oldY-range; y<= oldY+range; y++){
				temp[0] = x;
				temp[1] = y;
				if(map.containsKey(temp)){
					arr = map.getValue(temp);
					arr.remove(u);
					if(arr.isEmpty())
						map.remove(temp);
				}
			}
		for(int[] coords: getSight(u)){
			arr = map.getValue(coords);
			if(arr == null)
				arr = new ArrayList<Unit>();
			arr.add(u);
		}
	}
	
	public void removeUnit(Unit u){
		for(ArrayList<Unit> list: map.values())
			list.remove(u);
	}
	
	private ArrayList<int[]> getSight(Unit u){
		ArrayList<int[]> temp = new ArrayList<int[]>();
		int rootX = u.getColumn();
		int rootY = u .getRow();
		int range = u.getSightRange();
		for(int x = rootX-range; x <= rootX+range; x++)
			for(int y = range-Math.abs(rootX-x); y <= rootY+Math.abs(rootX-x); y++)
				//if(Math.abs(rootX-x) + Math.abs(rootY - y) <= range)
					temp.add(new int[] {x,y});
		return temp;
	}
	
	public void reset(){
		map.clear();
		for(Unit u: units)
			addUnit(u);
	}
	
	public Set<int[]> visibleSet(){
		return map.keySet();
	}
}
