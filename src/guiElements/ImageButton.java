package guiElements;
     
import guiElements.buttonActions.ButtonAction;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;

import resourceManager.SoundManager;
import utils.Logger;

/**
 * A Button based on an Image 
 *
 */
public class ImageButton extends MouseOverArea implements Button {
 
    private boolean enabled = false;
    private boolean lastMouseOver = false;
    private StateBasedGame sbg;
    private int stateID;
    private ButtonAction action;
    private String name;
    private boolean report;
    private boolean hidden;
    private boolean noClick;
 
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
        StateBasedGame sbg, int stateID, ButtonAction action) throws SlickException {
	    super((GUIContext)gc, ss.getSubImage(0, 0), x, y);
	    super.setMouseDownColor(Color.red);
	    super.setMouseOverColor(Color.blue);
	    super.setMouseOverImage(ss.getSubImage(0, 1));
	    super.setMouseDownImage(ss.getSubImage(0, 2));
	    this.sbg = sbg;
	    this.stateID = stateID;
	    this.action = action;
	    name = ss.getSubImage(0, 0).getName()+"Button";
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
    
    @Override
    public void setUnclickable(boolean b) {
    	hidden = b;
    	enabled = !b;
    	noClick = b;	
    }
    
    @Override
    public boolean isUnclickable() {
    	return noClick;
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
    	if (sbg.getCurrentStateID() == stateID) {
    		if (isMouseOver() && !lastMouseOver && !isEnabled()) {
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
    	if(enabled){
           if (isMouseOver() && sbg.getCurrentStateID() == stateID) {
               SoundManager.getManager().playSound(SoundManager.BUTTON_CLICK);
               if(action != null) action.activate();
           	if(report) Logger.logLine(name + " pressed.");
	           }
	           super.mouseClicked(button, x, y, clickCount);
       	}
       	else{
       		if(!noClick)
       			SoundManager.getManager().playSound(SoundManager.BUTTON_DISABLED);
      	}
    }

}