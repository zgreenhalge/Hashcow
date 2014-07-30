package guiElements;
     
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;

import actions.Action;
import resourceManager.SoundManager;
import theGame.Main;
import utils.Logger;

/**
 * A Button based on an Image 
 *
 */
public class ImageButton extends MouseOverArea implements Button, Component {
 
    private boolean enabled = true;
    private boolean lastMouseOver = false;
    private StateBasedGame sbg;
    private int stateID;
    private Action action;
    private String name;
    private boolean report;
    private boolean hidden;
 
    /**
	 * 
	 * @param guic - the GameContainer
	 * @param animation - Animation consisting of three states: 1-the normal button image, 2-the mouse over image, 3-the button clicked image
	 * @param x - x value of top left
	 * @param y - y value of top left
	 * @param sbg - the current game
	 * @param stateID - the state in which this button exists
	 * @param action - the action the button will take when pressed
	 * @throws SlickException - when something goes wrong
	 */
    public ImageButton(GameContainer gc, SpriteSheet ss, int x, int y,
        StateBasedGame sbg, int stateID, Action action) throws SlickException {
	    super((GUIContext)gc, ss.getSubImage(0, 0), x, y);
	    if(ss.getVerticalCount() > 1)
		    super.setMouseOverImage(ss.getSubImage(0, 1));
	    if(ss.getVerticalCount() > 2)
		    super.setMouseDownImage(ss.getSubImage(0, 2));
	    this.sbg = sbg;
	    this.stateID = stateID;
	    this.action = action;
	    name = ss.getName()+"Button";
    }
    
    public void setHidden(boolean b){
    	hidden = b;
    }
    
    public boolean isHidden(){
    	return hidden;
    }
 
    public boolean isEnabled() {
        return enabled;
    }
 
    public void setEnabled(boolean b) {
        enabled = b;
    }
    
    public void setReport(boolean b){
    	report = b;
    }
    
    public boolean isReporting(){
    	return report;
    }

    @Override
    public String getName() {
    	return name;
    }
    
    @Override
    public void setName(String s) {
    	name = s;
    }
 
    public void setLocation(int x, int y){
    	super.setLocation(x, y);
    }

	@Override
	public void setAction(Action a) {
		action = a;
	}

	@Override
	public Action getAction() {
		return action;
	}

	@Override
	public boolean contains(int x, int y) {
		if(x < getX() || x > getX() + getWidth())
			return false;
		if(y < getY() || y > getY() + getHeight())
			return false;
		return true;
	}

	@Override
	public boolean mouseClick(int button, int x, int y) {
		if(hidden)
    		return false;
    	if(isMouseOver()){
           if(sbg.getCurrentStateID() == stateID){
	           if(enabled){
	        	   if(report) 
	        		   Logger.logLine(name + " pressed.");
	               SoundManager.getManager().playSound(SoundManager.BUTTON_CLICK);
	               if(action != null) 
	            	   action.activate();
	           }else{
	        	   SoundManager.getManager().playSound(SoundManager.BUTTON_DISABLED);
	           }
	           super.mouseClicked(button, x, y, 1);
	           return true;
           }
    	}
    	return false;
	}

	@Override
	public boolean mouseMove(int oldx, int oldy, int newx, int newy){
		if(hidden)
			return false;
		if(isMouseOver()){
			if(sbg.getCurrentStateID() == stateID){
	    		if(!lastMouseOver){
	    			SoundManager.getManager().playSound(SoundManager.BUTTON_OVER);
	    			lastMouseOver = true;
	    			super.mouseMoved(oldx, oldy, newx, newy);
	    			return true;
	    		}
	    	}
			super.mouseMoved(oldx, oldy, newx, newy);
		}
		lastMouseOver = false;
		return false;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(!hidden)
    		super.render(container, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		//does nothing until we have animated buttons
	}

	@Override
	public boolean mouseWheelMove(int change) {
		// does nothing in this component 
		return false;
	}

}