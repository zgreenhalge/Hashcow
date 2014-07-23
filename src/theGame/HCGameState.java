package theGame;

import java.io.File;
import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import utils.Logger;
import utils.Settings;

public class HCGameState extends BasicGameState{

	private static boolean displayLog;
	private static final int DISPLAY_LENGTH = 3500; //how long to display the log
	
	private Input in;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{
		if(!Logger.isInit()){
			try {
				Logger.init(new File("logs"), container, false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		in = container.getInput();
		Logger.logLine("State " + getID() + " is initializing");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Logger.setContainer(container);
		if(displayLog || Logger.getAge() <= DISPLAY_LENGTH)
			Logger.render();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Logger.update(delta);
		if(Display.wasResized()){
	        try{
	        	Settings.set("resolution", Display.getWidth() + "x" + Display.getHeight());
	        	((AppGameContainer) container).setDisplayMode(Display.getWidth(), Display.getHeight(), false);
	        	for(int i=0; i< game.getStateCount(); i++)
	        		game.getState(i).init(container, game);
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	    }
		if(in.isKeyPressed(Input.KEY_F1))
			displayLog = !displayLog;
		if(in.isKeyPressed(Input.KEY_F10))
			container.setShowFPS(!container.isShowingFPS());
		if(in.isKeyDown(Input.KEY_F) && in.isKeyDown(Input.KEY_LALT))
			try{
				container.setFullscreen(!container.isFullscreen());
			}catch(SlickException e){
				Logger.log(new Exception("Cannot enter fullscreen mode"));
			}
	}

	//MUST BE OVERRIDDEN IN EACH SUB CLASS
	@Override
	public int getID(){
		return -1;
	}

}
