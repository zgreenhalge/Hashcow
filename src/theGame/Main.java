package theGame;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.SoundManager;
import utils.Logger;
import utils.Settings;

public class Main extends StateBasedGame {

	private static AppGameContainer appgc;
	private static StateBasedGame currentGame;
	
	public static final String GAME_VERSION = "0.01a";
	
	public static void main(String[] args){
		//entry point of program
		try{
			Settings.loadSettings("config.txt");
			}
		catch(FileNotFoundException e){
			//do nothing, Settings will handle init
		}
		
		//prints out all native display modes 
		for(DisplayMode mode: getDisplayModes()){
			System.out.println(mode.getWidth() + " x " 
							+ mode.getHeight() + " x " 
							+ mode.getBitsPerPixel() 
							+ " " + mode.getFrequency() + "Hz");
		}
		
		try{
			currentGame = new Main("This Is The Title");
			Display.setResizable(true);
			appgc = new AppGameContainer(currentGame);
			String[] res = ((String) Settings.getSetting("resolution")).split("x");
			appgc.setDisplayMode(Integer.parseInt(res[0]), Integer.parseInt(res[1]), (Boolean) Settings.getSetting("fullscreen"));
			appgc.setAlwaysRender(true);
			appgc.setIcons(new String[] {"/res/icon32.png", "/res/icon24.png", "/res/icon16.png"});
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
		addState(new GameLobbyState());
	}
	
	public static StateBasedGame getGame(){
		return currentGame;
	}
	
	public static GameContainer getStaticContainer(){
		return appgc;
	}
	
	public static DisplayMode[] getDisplayModes(){
		DisplayMode[] modes = null;
		try {
			modes = Display.getAvailableDisplayModes();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modes;
	}

	/**
	 * Cleanly shut down the game
	 */
	public static void exit(){
		try {
			if(Logger.writeOut())
				System.out.println("Log written to file.");
			else
				System.out.println("Logs not written to file");
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
