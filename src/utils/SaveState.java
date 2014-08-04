package utils;

import gamePieces.MapInfo;
import gamePieces.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import theGame.GameState;

public class SaveState implements Serializable{

	private static final long serialVersionUID = -8496319193868800112L;

	private static final String saveDir = "saves";
	
	private ArrayList<Player> players;
	private MapInfo map;
	private int turn;
	private int turnLength;
	private int curPlayer;
	
	public SaveState(GameState game){
		players = game.getPlayers();
		map = game.getMap();
		turn = game.getTurn();
		turnLength = 4;
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
	
	public static void save(SaveState target){
		File path = new File(saveDir + "/" + Time.updateCal().fileDateTime() + ".dat");
		if(!path.exists())
			try{
				(new File(saveDir)).mkdirs();
				path.createNewFile();
			}
			catch(Exception e){Logger.loudLog(e);}
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(target);
			oos.close();
			Logger.logNote(path.getName() + " saved");
		}catch(FileNotFoundException e){
			Logger.log(e);
			e.printStackTrace();
		}catch(IOException e){
			Logger.log(e);
			e.printStackTrace();
		}
	}
	
	public static SaveState load(String path){
		SaveState ret = null;
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			ret = (SaveState) ois.readObject();
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

	public int getTurnLength() {
		return turnLength;
	}
	
}
