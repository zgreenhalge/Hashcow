package utils;

import gamePieces.MapInfo;
import gamePieces.Player;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import theGame.InGame;

public class SaveStruct implements Serializable{

	private static final long serialVersionUID = 5527501721576603651L;
	private static final String saveDir = "saves";
	
	private ArrayList<Player> players;
	private MapInfo map;
	private int turn;
	private int curPlayer;
	
	public SaveStruct(InGame game){
		players = game.getPlayers();
		map = game.getMap();
		turn = game.getTurn();
		curPlayer = game.getCurrentPlayer().getId()-1;
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public MapInfo getMap(){
		return map;
	}
	
	public int getTurn(){
		return turn;
	}
	
	public int getCurPlayer(){
		return curPlayer;
	}
	
	public static void save(SaveStruct target){
		String path = saveDir + "/" + Time.updateCal().fileDateTime() + ".dat";
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(target);
			oos.close();
		}catch(FileNotFoundException e){
			Logger.log(e);
			e.printStackTrace();
		}catch(IOException e){
			Logger.log(e);
			e.printStackTrace();
		}
	}
	
	public static SaveStruct load(String path){
		SaveStruct ret = null;
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			ret = (SaveStruct)(ois.readObject());
			ois.close();
		}catch(FileNotFoundException e){
			Logger.log(e);
			e.printStackTrace();
		}catch(IOException e){
			Logger.log(e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Logger.log(e);
			e.printStackTrace();
		}
		return ret;
	}
	
}
