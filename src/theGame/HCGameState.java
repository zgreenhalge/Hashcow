package theGame;

import java.io.File;
import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import utils.Logger;
import utils.Observer;

public abstract class HCGameState extends BasicGameState implements Observer {

	private boolean displayLog;
	private int logDelta;
	private static final int DISPLAY_LENGTH = 10000; //how long to display the log 
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException{
		if(!Logger.isInit()){
			try {
				Logger.init(new File("logs"), arg0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Logger.logNote("State " + getID() + " is initializing");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Logger.setContainer(container);
		if(displayLog || logDelta < DISPLAY_LENGTH)
			Logger.render();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		logDelta += delta;
		if(container.getInput().isKeyPressed(Input.KEY_GRAVE))
			logDelta = 0;
	}

	public void alert(int caller_id){
		if(caller_id == Logger.ID)
			logDelta = 0;
	}
	
	@Override
	public abstract int getID();

}
