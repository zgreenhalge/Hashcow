package SetUp;
import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import utils.Logger;

public class SimpleSlickGame extends StateBasedGame{
	
	private static AppGameContainer appgc;
	

	public static void main(String[] args) throws Exception{
		try{
			appgc = new AppGameContainer(new SimpleSlickGame("This Is The Title"));
			appgc.setDisplayMode(800, 480, false);
			appgc.setShowFPS(true);
			appgc.start();
		}
		catch (Exception ex){
			Logger.println("CRITICAL EXCEPTION THROWN: " + ex.getMessage());
			Logger.writeOut();
			throw ex;
		}
	}
	
	public SimpleSlickGame(String gamename){
		super(gamename);
	}
	
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new TestState());
	}
	
	public static void exit(){
		try {
			Logger.writeOut();
		} catch (IOException e) {
			System.err.println("Exception thrown while peacefully writing logs");
		}
		appgc.exit();
	}
}