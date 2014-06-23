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

public class HCGameState extends BasicGameState implements Observer {

	private boolean displayLog;
	private final Object displayLock = new Object();
	private int logDelta;
	private static final int DISPLAY_LENGTH = 3000; //how long to display the log 
	
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
		Logger.registerObserver(this);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Logger.setContainer(container);
		if(displayLog || logDelta < DISPLAY_LENGTH)
			Logger.render();
		g.drawString("logDelta: " + logDelta, 0, 40);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		synchronized(displayLock){logDelta += delta;}
		if(container.getInput().isKeyPressed(Input.KEY_GRAVE))
			displayLog = !displayLog;
	}

	public void alert(int caller_id){
		if(caller_id == Logger.ID){
			Logger.logLine("Alert recieved from Logger");
			synchronized(displayLock){logDelta = 0;}
		}
	}
	
	//MUST BE OVERRIDDEN IN EACH SUB CLASS
	@Override
	public int getID(){
		return -1;
	}

}
