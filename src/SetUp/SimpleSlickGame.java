package SetUp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class SimpleSlickGame extends StateBasedGame{
	
	private static AppGameContainer appgc;
	

	public static void main(String[] args){
		try{
			appgc = new AppGameContainer(new SimpleSlickGame("This Is The Title"));
			appgc.setDisplayMode(800, 480, false);
			appgc.setShowFPS(true);
			appgc.start();
		}
		catch (SlickException ex){
			Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public SimpleSlickGame(String gamename){
		super(gamename);
	}
	
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new TestState());
	}
}