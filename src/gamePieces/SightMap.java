package gamePieces;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import utils.Logger;
import utils.OneToOneMap;

public class SightMap {

	private ArrayList<Unit> units;
	private OneToOneMap<Coordinate, ArrayList<Unit>> map;
	
	public SightMap(){
		units = new ArrayList<Unit>();
		map = new OneToOneMap<Coordinate, ArrayList<Unit>>();
		reset();
	}
	
	public boolean isVisible(Coordinate c){
		return map.containsKey(c);
	}
	
	public ArrayList<Coordinate> addUnit(Unit u){
		ArrayList<Coordinate> ret = new ArrayList<Coordinate>();
		for(Coordinate coords: getSight(u)){
				if(!map.containsKey(coords)){
					map.add(coords, new ArrayList<Unit>());
					ret.add(coords);
				}
				map.getValue(coords).add(u);
			}
/*		String print = "Visible:";
		for(Coordinate coords: ret)
			print += " " + coords.toString();
		print += "   ("+ret.size()+")";
		Logger.loudLogLine(print);	*/
		return ret;
	}
	
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

	public ArrayList<Coordinate> updateUnit(Unit u, Coordinate oldLoc){
		int range = u.getSightRange();
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
		for(Coordinate coords: getSight(u)){
			arr = map.getValue(coords);
			if(arr == null){
				arr = new ArrayList<Unit>();
				ret.add(coords);
			}
			arr.add(u);
		}
		
		return ret;
	}
	
	
	private ArrayList<Coordinate> getSight(Unit u){
		ArrayList<Coordinate> temp = new ArrayList<Coordinate>();
		int rootX = u.getColumn();
		int rootY = u .getRow();
		int range = u.getSightRange();
		for(int x = rootX-range; x <= rootX+range; x++)
			for(int y = rootY-(range-Math.abs(rootX-x)); y <= rootY+(range-Math.abs(rootX-x)); y++){
				if(y >= 0 && x >= 0)
					temp.add(new Coordinate(x,y));
			}
		return temp;
	}
	
	public void reset(){
		map.clear();
		for(Unit u: units)
			addUnit(u);
	}
	
	public Set<Coordinate> visibleSet(){
		return map.keySet();
	}
}
