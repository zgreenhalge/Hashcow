package theGame;

import gamePieces.MapInfo;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.SoundManager;
import utils.Logger;

public class Main extends StateBasedGame {

	private static AppGameContainer appgc;
	private static StateBasedGame currentGame;
	
	public static void main(String[] args){
		//entry point of program
		try{
			currentGame = new Main("This Is The Title");
			appgc = new AppGameContainer(currentGame);
			appgc.setDisplayMode(800, 480, false);
			appgc.setShowFPS(true);
			appgc.start();			
		}catch(SlickException se){
			//Logger.logException(se)
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
		addState(new MainMenu());
		addState(new InGame(MapInfo.TEST_MAP, 0));
	}
	
	public static StateBasedGame getGame(){
		return currentGame;
	}

	/**
	 * Cleanly shut down the game
	 */
	public static void exit(){
		try {
			Logger.writeOut();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.streamLog("Clean exit achieved.");
		appgc.exit();
	}
	
}
