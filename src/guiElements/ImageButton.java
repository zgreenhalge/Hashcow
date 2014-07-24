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
import utils.Logger;

/**
 * A Button based on an Image 
 *
 */
public class ImageButton extends MouseOverArea implements Button {
 
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
    public Button setName(String s) {
    	name = s;
    	return this;
    }
    
    @Override
    public void render(GUIContext guic, Graphics g){
    	if(!hidden)
    		super.render(guic, g);
    }
    
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    	if (sbg.getCurrentStateID() == stateID && !hidden) {
    		if (isMouseOver() && !lastMouseOver) {
    			SoundManager.getManager().playSound(SoundManager.BUTTON_OVER);
    			lastMouseOver = true;
    		} else if (!isMouseOver()) {
    			lastMouseOver = false;
    		}
    	}
    	super.mouseMoved(oldx, oldy, newx, newy);
    }
 
    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
    	if(hidden)
    		return;
    	if(enabled){
           if(isMouseOver() && sbg.getCurrentStateID() == stateID) {
        	   if(report) 
        		   Logger.logLine(name + " pressed.");
               SoundManager.getManager().playSound(SoundManager.BUTTON_CLICK);
               if(action != null) 
            	   action.activate();
           }
	           super.mouseClicked(button, x, y, clickCount);
       	}
       	else{
       		SoundManager.getManager().playSound(SoundManager.BUTTON_DISABLED);
      	}
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

}