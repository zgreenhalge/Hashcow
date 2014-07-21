package theGame;

import gamePieces.MapInfo;
import gamePieces.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.SoundManager;
import utils.Logger;
import utils.Settings;

public class Main extends StateBasedGame {

	private static AppGameContainer appgc;
	private static StateBasedGame currentGame;
	
	public static void main(String[] args){
		//entry point of program
		try{
			Settings.loadSettings("config.txt");
			}
		catch(FileNotFoundException e){
			//do nothing, Settings will handle init
		}
		
		try{
			currentGame = new Main("This Is The Title");
			appgc = new AppGameContainer(currentGame);
			appgc.setDisplayMode(800, 480, false);
			appgc.setAlwaysRender(true);
			GameContainer.enableSharedContext();
			appgc.setShowFPS(true);
			appgc.start();			
		}catch(SlickException se){
			se.printStackTrace();
		}
	}
	
	public Main(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * Wraps the super method to provide logging and sound management between states 
	 */
	public void enterState(int id){
		Logger.logNote("Transitioning to state " + id);
		SoundManager.stopAll();
		super.enterState(id);
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MainMenuState());
		addState(new LoadGameState());
	}
	
	public static StateBasedGame getGame(){
		return currentGame;
	}
	
	public static GameContainer getGameContainer(){
		return appgc;
	}

	/**
	 * Cleanly shut down the game
	 */
	public static void exit(){
		try {
			Logger.writeOut();
			System.out.println("Log written to file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try{
			Settings.save("config.txt");
			System.out.println("Settings saved");
		}catch(Exception e){
			e.printStackTrace();
		}
		Logger.println("Clean exit achieved.");
		appgc.exit();
	}
	
}
