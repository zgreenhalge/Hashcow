package resourceManager;

import gamePieces.Coordinate;
import gamePieces.MapInfo;
import gamePieces.Tile;
import gamePieces.TileTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import utils.Logger;

public class MapManager {
	
	private static Scanner in;
	private static PrintWriter out;
	private static String savePath = "maps";
	private static File saveDir;
	
	public static ArrayList<MapInfo> loadMaps(){
		ArrayList<MapInfo> ret = new ArrayList<MapInfo>();
		String name;
		int players;
		Coordinate[] positions;
		ArrayList<Coordinate> pos = new ArrayList<Coordinate>();
		Tile[][] tiles;
		ArrayList<ArrayList<Tile>> map = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> list = new ArrayList<Tile>();
		if(saveDir == null)
			saveDir = new File(savePath);
		if(!saveDir.exists())
			saveDir.mkdirs();
		for(File f: saveDir.listFiles()){
			try {
				in = new Scanner(new BufferedReader(new FileReader(f)));
				name = in.nextLine();
				for(String s: in.nextLine().split(":")){
					pos.add(new Coordinate(s));
				}
				positions = new Coordinate[pos.size()];
				pos.toArray(positions);
				players = pos.size();
				while(in.hasNext()){
					map.add(new ArrayList<Tile>());
					list = map.get(map.size()-1);
					for(String s: in.nextLine().split(":")){
						list.add(Tile.copy(TileTemplate.getTemplate(s)));
					}
				}
				tiles = new Tile[map.size()][map.get(0).size()];
				for(int i=0; i<map.size(); i++)
					for(int j=0; j<map.get(0).size(); j++)
						tiles[i][j] = map.get(i).get(j);
				ret.add(new MapInfo(name, tiles, positions, players));
			} catch (Exception e) {
				Logger.loudLog(e);
			}
		}	
		in.close();
		return ret;
	}
	
	public static void saveMap(MapInfo map) throws IOException{
		File target = new File(savePath + "/" + map.getName() + ".map");
		new File(savePath).mkdirs();
		if(target.exists())
			target.delete();
		target.createNewFile();
		out = new PrintWriter(new FileWriter(target));
		out.println(map.getName());
		for(int i=0; i<map.getMaxPlayers(); i++){
			out.print(map.getStartingPosition(i).toString());
			if(i != map.getMaxPlayers()-1)
				out.print(":");
			else
				out.println();
		}
		for(int x=0; x<map.getWidth(); x++)
			for(int y=0; y<map.getHeight(); y++){
				out.print(map.getTile(new Coordinate(x, y)).getFileName());
				if(y != map.getHeight()-1)
					out.print(":");
				else if(x != map.getWidth()-1)
					out.println();
			}
		out.flush();
		out.close();	
	}

}
